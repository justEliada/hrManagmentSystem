package com.example.backend.controllers;

import com.example.backend.dto.timeSheetDTO.TimeSheetDto;
import com.example.backend.dto.userDTO.UserDto;
import com.example.backend.dto.userDTO.UserLoginDto;
import com.example.backend.dto.userDTO.UserResponseDto;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addNewUser(@RequestBody UserDto userDto) {
        try {
            UserDto newUser = userService.addUser(userDto);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<UserResponseDto> editUser(@PathVariable Long id,
                                                    @RequestBody UserResponseDto userResponseDto) {
        UserResponseDto updatedUser = userService.editUser(id, userResponseDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        return ResponseEntity.ok(userResponseDto);
    }
    @GetMapping("/days-off/{id}")
    public ResponseEntity<Integer> getUserDaysOff(@PathVariable Long id) {
        Integer daysOff = userService.getUserDaysOff(id);
        return ResponseEntity.ok(daysOff);
    }
    @GetMapping("/")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<UserResponseDto>> getUsersByTimeSheet() {
        List<UserResponseDto> users = userService.getUsersByLeastCreatedTimeSheet();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserLoginDto userLoginDto) {
        UserResponseDto userResponseDto = userService.verifyUser(userLoginDto.getUsername(), userLoginDto.getPassword());
        return ResponseEntity.ok(userResponseDto);
    }
}
