package com.example.backend.controllers;

import com.example.backend.dto.timeSheetDTO.TimeSheetDto;
import com.example.backend.dto.timeSheetDTO.TimeSheetResponseDto;
import com.example.backend.dto.timeSheetDTO.TimeStatusResponseSheetDto;
import com.example.backend.service.TimeSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/timesheet")
public class TimeSheetController {
}
