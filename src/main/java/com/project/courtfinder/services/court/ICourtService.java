package com.project.courtfinder.services.court;

import com.project.courtfinder.dto.CourtDto;
import com.project.courtfinder.model.Court;
import com.project.courtfinder.request.CreateCourtRequest;
import com.project.courtfinder.request.UpdateCourtRequest;
import java.util.List;

public interface ICourtService {
    Court createCourt(CreateCourtRequest courtRequest);
    Court getCourtById(Long id);
    List<Court> getAllCourts();
    void deleteCourtById(Long id);
    Court updateCourt(UpdateCourtRequest court, Long courtId);
    CourtDto convertCourtToDto(Court court);
    List<Court> getCourtsByOwnerId(Long ownerId);
}
