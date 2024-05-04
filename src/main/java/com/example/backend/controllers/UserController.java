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
}
