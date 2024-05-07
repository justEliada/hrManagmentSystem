package com.example.backend.service;

import com.example.backend.dto.timeSheetDTO.TimeSheetDto;
import com.example.backend.dto.userDTO.UserDto;
import com.example.backend.dto.userDTO.UserResponseDto;
import com.example.backend.entity.Timesheet;
import com.example.backend.entity.User;
import com.example.backend.enums.UserRole;
import com.example.backend.repository.TimeSheetRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.exception.CustomExceptionHandler;
import com.example.backend.exception.UserAlreadyExistsException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    @Autowired
    private PasswordEncoder encoder;

    public UserDto addUser(UserDto userDto) throws Exception {

        Optional<User> existingUser = repository.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with username: " + userDto.getUsername());
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRole(UserRole.valueOf("USER"));
        repository.save(user);
        return userDto;
    }

    public UserResponseDto editUser(Long id, UserResponseDto userResponseDto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setUsername(userResponseDto.getUsername());
        user.setFirstName(userResponseDto.getFirstName());
        user.setLastName(userResponseDto.getLastName());

        repository.save(user);
        return userResponseDto;
    }

    public String deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        repository.delete(user);
        return "User deleted successfully";
    }

    public List<UserResponseDto> getAllUsers() {
        return repository.findAllByOrderByCreatedAtAsc().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getUsername(), user.getFirstName(),
                        user.getLastName(), user.getDaysOff(), user.getRole()))
                .collect(Collectors.toList());
    }

    public List<UserResponseDto> getUsersByLeastCreatedTimeSheet() {
        List<Timesheet> timesheets = timeSheetRepository.findAllByOrderByCreatedAtDesc();
        Set<User> uniqueUsers = timesheets.stream()
                .map(Timesheet::getUser)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return uniqueUsers.stream()
                .map(user -> new UserResponseDto(user.getId(), user.getUsername(), user.getFirstName(),
                        user.getLastName(), user.getDaysOff(), user.getRole()))
                .collect(Collectors.toList());
    }

    public List<UserDto> getAllUsersById() {
        return repository.findAllByOrderByCreatedAtAsc().stream()
                .map(user -> new UserDto(user.getUsername(), user.getFirstName(), user.getLastName(), null))
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        return userResponseDto;
    }

    public Integer getUserDaysOff(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        Integer daysOff = user.getDaysOff();

        return daysOff;
    }

    public UserResponseDto verifyUser(String username, String password) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new CustomExceptionHandler("Credentials doesn't match!"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new CustomExceptionHandler("Password incorrect!");
        }

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .daysOff(user.getDaysOff())
                .build();

        return userResponseDto;
    }

}
