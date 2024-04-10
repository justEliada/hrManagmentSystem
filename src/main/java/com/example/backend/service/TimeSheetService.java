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
}
