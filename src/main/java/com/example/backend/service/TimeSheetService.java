package com.example.backend.service;
import com.example.backend.dto.timeSheetDTO.TimeSheetDto;
import com.example.backend.dto.timeSheetDTO.TimeSheetResponseDto;
import com.example.backend.dto.timeSheetDTO.TimeStatusResponseSheetDto;
import com.example.backend.entity.Timesheet;
import com.example.backend.entity.User;
import com.example.backend.enums.TimeSheetStatus;
import com.example.backend.repository.TimeSheetRepository;
import com.example.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.exception.CustomExceptionHandler;
import com.example.backend.exception.TimeSheetAlreadyApproved;
import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.utils.TimeSheetDaysUtil;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class TimeSheetService {

    @Autowired
    private TimeSheetRepository repository;

    @Autowired
    private UserRepository userRepository;

    public TimeSheetResponseDto addTimeSheet(TimeSheetResponseDto timeSheetResponseDto) throws Exception {

        Long userId = timeSheetResponseDto.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        if (user.getDaysOff() < 1) {
            throw new CustomExceptionHandler("You have no days off left to apply!");
        }

        if (timeSheetResponseDto.getToDate().isBefore(timeSheetResponseDto.getFromDate())) {
            throw new IllegalArgumentException("Life goes forward. Pick your dates accordingly.");
        }

        boolean existingTimeSheet = repository
                .existsByUserAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                        user, timeSheetResponseDto.getFromDate(),
                        timeSheetResponseDto.getToDate());
        if (existingTimeSheet) {
            throw new UserAlreadyExistsException("You already have applied for these dates: "
                    + timeSheetResponseDto.getFromDate() + " - "
                    + timeSheetResponseDto.getToDate());
        }

        Long days = TimeSheetDaysUtil.calculateDaysBetween(timeSheetResponseDto.getFromDate(),
                timeSheetResponseDto.getToDate());

        if (days + 1 > user.getDaysOff()) {
            throw new CustomExceptionHandler("You are requesting more days that you have left: "
                    + user.getDaysOff() + " days");
        }

        Timesheet timeSheet = new Timesheet();
        timeSheet.setFromDate(timeSheetResponseDto.getFromDate());
        timeSheet.setToDate(timeSheetResponseDto.getToDate());
        timeSheet.setCreatedBy(user.getFirstName() + " " + user.getLastName());
        timeSheet.setNotes(timeSheetResponseDto.getNotes());
        timeSheet.setStatus(TimeSheetStatus.PENDING);
        timeSheet.setUser(user);
        repository.save(timeSheet);

        return timeSheetResponseDto;
    }

    public TimeSheetResponseDto addTimeSheetManager(TimeSheetResponseDto timeSheetResponseDto) throws Exception {

        Long userId = timeSheetResponseDto.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (timeSheetResponseDto.getToDate().isBefore(timeSheetResponseDto.getFromDate())) {
            throw new IllegalArgumentException("Life goes forward. Pick your dates accordingly.");
        }

        Long days = TimeSheetDaysUtil.calculateDaysBetween(timeSheetResponseDto.getFromDate(),
                timeSheetResponseDto.getToDate());

        if (days + 1 > user.getDaysOff()) {
            throw new CustomExceptionHandler("You are requesting more days that you have left: "
                    + user.getDaysOff() + " days");
        }

        Timesheet timeSheet = new Timesheet();
        timeSheet.setFromDate(timeSheetResponseDto.getFromDate());
        timeSheet.setToDate(timeSheetResponseDto.getToDate());
        timeSheet.setCreatedBy(user.getFirstName() + " " + user.getLastName());
        timeSheet.setNotes(timeSheetResponseDto.getNotes());
        timeSheet.setStatus(TimeSheetStatus.PENDING);
        timeSheet.setUser(user);
        repository.save(timeSheet);

        return timeSheetResponseDto;
    }

    public TimeSheetResponseDto editTimeSheet(Long id, TimeSheetResponseDto timeSheetResponseDto) {

        Long userId = timeSheetResponseDto.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Timesheet timeSheet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeSheet not found: " + id));
        timeSheet.setFromDate(timeSheetResponseDto.getFromDate());
        timeSheet.setToDate(timeSheetResponseDto.getToDate());
        timeSheet.setNotes(timeSheetResponseDto.getNotes());
        timeSheet.setStatus(TimeSheetStatus.valueOf("PENDING"));
        timeSheet.setModifiedBy(user.getFirstName() + " " + user.getLastName());
        timeSheet.setUser(timeSheetResponseDto.getUser());
        repository.save(timeSheet);

        return timeSheetResponseDto;
    }

    public String deleteTimeSheet(Long id) {
        Timesheet timesheet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeSheet not found: " + id));
        repository.delete(timesheet);
        return "TimeSheet deleted successfully";
    }

    public List<TimeSheetDto> getAllTimeSheets() {
        return repository.findAll().stream()
                .map(timeSheet -> new TimeSheetDto(timeSheet.getId(), timeSheet.getFromDate(),
                        timeSheet.getToDate(), timeSheet.getStatus(), timeSheet.getNotes(),
                        timeSheet.getCreatedBy()))
                .collect(Collectors.toList());
    }

    public TimeSheetDto getTimeSheetById(Long id) {
        Timesheet timeSheet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeSheet not found: " + id));

        TimeSheetDto timeSheetDto = TimeSheetDto.builder()
                .id(timeSheet.getId())
                .fromDate(timeSheet.getFromDate())
                .toDate(timeSheet.getToDate())
                .status(timeSheet.getStatus())
                .notes(timeSheet.getNotes())
                .createdBy(timeSheet.getCreatedBy())
                .build();

        return timeSheetDto;
    }

    public List<TimeSheetDto> getTop3TimeSheetById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return repository.findTop3ByUserOrderByCreatedAtDesc(user).stream()
                .map(timeSheet -> new TimeSheetDto(timeSheet.getId(), timeSheet.getFromDate(),
                        timeSheet.getToDate(), timeSheet.getStatus(), timeSheet.getNotes(),
                        timeSheet.getCreatedBy()))
                .collect(Collectors.toList());
    }

    public List<TimeSheetDto> getAllTimeSheetById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return repository.findByUser(user).stream()
                .map(timeSheet -> new TimeSheetDto(timeSheet.getId(), timeSheet.getFromDate(),
                        timeSheet.getToDate(), timeSheet.getStatus(), timeSheet.getNotes(),
                        timeSheet.getCreatedBy()))
                .collect(Collectors.toList());
    }

    public TimeStatusResponseSheetDto approveTimeSheetById(Long id) {
        Timesheet timeSheet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeSheet not found: " + id));

        if (timeSheet.getStatus() == TimeSheetStatus.APPROVED) {
            throw new TimeSheetAlreadyApproved("Time Sheet is already approved!");
        }

        User user = userRepository.findById(timeSheet.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with ID: " + timeSheet.getUser()));
        Long days = TimeSheetDaysUtil.calculateDaysBetween(timeSheet.getFromDate(),
                timeSheet.getToDate());
        if (days + 1 >= user.getDaysOff()) {
            throw new CustomExceptionHandler("You only have: " + user.getDaysOff() + " days left!");
        }
        user.setDaysOff((int) (user.getDaysOff() - days - 1));

        timeSheet.setStatus(TimeSheetStatus.APPROVED);
        repository.save(timeSheet);
        TimeStatusResponseSheetDto timeStatusResponseSheetDto = new TimeStatusResponseSheetDto(
                "Time Sheet approved successfully!");
        return timeStatusResponseSheetDto;
    }

    public TimeStatusResponseSheetDto rejectTimeSheetById(Long id) {

        Timesheet timeSheet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeSheet not found: " + id));

        if (timeSheet.getStatus() == TimeSheetStatus.REJECTED) {
            throw new TimeSheetAlreadyApproved("Time Sheet is already rejected!");
        }

        User user = userRepository.findById(timeSheet.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with ID: " + timeSheet.getUser()));

        timeSheet.setStatus(TimeSheetStatus.REJECTED);
        repository.save(timeSheet);

        Long days = TimeSheetDaysUtil.calculateDaysBetween(timeSheet.getFromDate(),
                timeSheet.getToDate());

        user.setDaysOff((int) (user.getDaysOff() + days + 1));
        userRepository.save(user);

        TimeStatusResponseSheetDto timeStatusResponseSheetDto = new TimeStatusResponseSheetDto(
                "Time Sheet rejected!");
        return timeStatusResponseSheetDto;
    }

    public TimeSheetResponseDto changeTimeSheetDateById(Long id, TimeSheetResponseDto timeSheetResponseDto) {

        Long userId = timeSheetResponseDto.getUser().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        boolean existingTimeSheet = repository.existsByUserAndFromDateAndToDate(user,
                timeSheetResponseDto.getFromDate(), timeSheetResponseDto.getToDate());
        if (existingTimeSheet) {
            throw new UserAlreadyExistsException("You already have applied for these dates: "
                    + timeSheetResponseDto.getFromDate() + " - "
                    + timeSheetResponseDto.getToDate());
        }

        Timesheet timeSheet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeSheet not found: " + id));
        timeSheet.setFromDate(timeSheetResponseDto.getFromDate());
        timeSheet.setToDate(timeSheetResponseDto.getToDate());
        repository.save(timeSheet);

        return timeSheetResponseDto;
    }

}
