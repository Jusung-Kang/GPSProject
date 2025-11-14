package com.jskang.backend.controller;

import com.jskang.backend.domain.AvailableSports;
import com.jskang.backend.dto.AvailableSportsResponseDto;
import com.jskang.backend.dto.SaveAvailableSportsRequestDto;
import com.jskang.backend.service.AvailableSportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AvailableSportsApiController {

    private final AvailableSportsService availableSportsService;

    @PostMapping("api/sport")
    public ResponseEntity<AvailableSports> save(@RequestBody SaveAvailableSportsRequestDto saveAvailableSportsRequestDto){
        AvailableSports saved = availableSportsService.save(saveAvailableSportsRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saved);
    }

    @GetMapping("api/sport")
    public ResponseEntity<List<AvailableSports>> getAll(){
        List<AvailableSports> saved = availableSportsService.findAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(saved);
    }

    @GetMapping("api/sport/{id}")
    public ResponseEntity<AvailableSportsResponseDto> getSportById(@PathVariable String id){
        AvailableSports sport = availableSportsService.findById(id);

        return ResponseEntity.ok()
                .body(new AvailableSportsResponseDto(sport));
    }

}
