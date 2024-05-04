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


    @Autowired
    private TimeSheetService timeSheetService;

    @PostMapping("/add")
    public ResponseEntity<?> addNewTimeSheet(@RequestBody TimeSheetResponseDto timeSheetDto) {
        try {
            TimeSheetResponseDto newTimeSheet = timeSheetService.addTimeSheet(timeSheetDto);
            return new ResponseEntity<>(newTimeSheet, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
    @PostMapping("/timesheet-manager-add")
    public ResponseEntity<?> addNewTimeSheetManager(@RequestBody TimeSheetResponseDto timeSheetDto) {
        try {
            TimeSheetResponseDto newTimeSheet = timeSheetService.addTimeSheetManager(timeSheetDto);
            return new ResponseEntity<>(newTimeSheet, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
    @PostMapping("/edit/{id}")
    public ResponseEntity<TimeSheetResponseDto> editTimeSheet(@PathVariable Long id,
                                                              @RequestBody TimeSheetResponseDto timeSheetResponseDto) {
        TimeSheetResponseDto updatedTimeSheet = timeSheetService.editTimeSheet(id, timeSheetResponseDto);
        return ResponseEntity.ok(updatedTimeSheet);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTimeSheet(@PathVariable Long id) {
        timeSheetService.deleteTimeSheet(id);
        return ResponseEntity.ok("TimeSheet deleted successfully");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TimeSheetDto> getTimeSheet(@PathVariable Long id) {
        TimeSheetDto timeSheetResponseDto = timeSheetService.getTimeSheetById(id);
        return ResponseEntity.ok(timeSheetResponseDto);
    }

    @GetMapping("/approve/{id}")
    public ResponseEntity<TimeStatusResponseSheetDto> approveTimeSheet(@PathVariable Long id) {
        TimeStatusResponseSheetDto timeStatusResponseSheetDto = timeSheetService.approveTimeSheetById(id);
        return ResponseEntity.ok(timeStatusResponseSheetDto);
    }

    @GetMapping("/reject/{id}")
    public ResponseEntity<TimeStatusResponseSheetDto> rejectTimeSheet(@PathVariable Long id) {
        TimeStatusResponseSheetDto timeStatusResponseSheetDto =timeSheetService.rejectTimeSheetById(id);
        return ResponseEntity.ok(timeStatusResponseSheetDto);
    }

    @GetMapping("/date-update/{id}")
    public ResponseEntity<TimeSheetResponseDto> changeTimeSheetDateById(@PathVariable Long id, @RequestBody TimeSheetResponseDto timeSheetResponseDto) {
        TimeSheetResponseDto updatedTimeSheet = timeSheetService.changeTimeSheetDateById(id, timeSheetResponseDto);
        return ResponseEntity.ok(updatedTimeSheet);
    }

    @GetMapping("/{id}/get-least-created")
    public ResponseEntity<List<TimeSheetDto>> getTop3TimeSheetById(@PathVariable Long id) {
        List<TimeSheetDto> timeSheets = timeSheetService.getTop3TimeSheetById(id);
        return ResponseEntity.ok(timeSheets);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TimeSheetDto>> getAllTimeSheets() {
        List<TimeSheetDto> timeSheets = timeSheetService.getAllTimeSheets();
        return ResponseEntity.ok(timeSheets);
    }

    @GetMapping("/{id}/all")
    public ResponseEntity<List<TimeSheetDto>> getAllTimeSheetById(@PathVariable Long id) {
        List<TimeSheetDto> timeSheets = timeSheetService.getAllTimeSheetById(id);
        return ResponseEntity.ok(timeSheets);
    }


}
