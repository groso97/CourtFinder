package com.project.courtfinder.controller;

import com.project.courtfinder.dto.CourtDto;
import com.project.courtfinder.exceptions.AlreadyExistsException;
import com.project.courtfinder.exceptions.ResourceNotFoundException;
import com.project.courtfinder.model.Court;
import com.project.courtfinder.request.CreateCourtRequest;
import com.project.courtfinder.request.UpdateCourtRequest;
import com.project.courtfinder.response.ApiResponse;
import com.project.courtfinder.services.court.ICourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/courts")
public class CourtController {

    private final ICourtService iCourtService;

    @GetMapping("/{courtId}/court")
    public ResponseEntity<ApiResponse> getCourtById(@PathVariable Long courtId){
        try {
            Court court = iCourtService.getCourtById(courtId);
            CourtDto courtDto = iCourtService.convertCourtToDto(court);
            return ResponseEntity.ok(new ApiResponse("Success", courtDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getCourts(){
        List<Court> courts = iCourtService.getAllCourts();
        return ResponseEntity.ok(new ApiResponse("Success", courts));
    }

    @DeleteMapping("/delete/court/{courtId}")
    public ResponseEntity<ApiResponse> deleteCourtById(@PathVariable Long courtId){
        try {
            iCourtService.deleteCourtById(courtId);
            return ResponseEntity.ok(new ApiResponse("Court deleted successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/create-court")
    public ResponseEntity<ApiResponse> createCourt(@RequestBody CreateCourtRequest request){
        try {
            Court court = iCourtService.createCourt(request);
            CourtDto courtDto = iCourtService.convertCourtToDto(court);
            return ResponseEntity.ok(new ApiResponse("Court created successfully!", courtDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/court/{courtId}")
    public ResponseEntity<ApiResponse> updateCourt(@RequestBody UpdateCourtRequest request, @PathVariable Long courtId){
        try {
            Court court = iCourtService.updateCourt(request, courtId);
            CourtDto courtDto = iCourtService.convertCourtToDto(court);
            return ResponseEntity.ok(new ApiResponse("Court updated successfully!", courtDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
