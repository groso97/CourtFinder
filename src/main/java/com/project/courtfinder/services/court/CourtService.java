package com.project.courtfinder.services.court;

import com.project.courtfinder.dto.CourtDto;
import com.project.courtfinder.exceptions.AlreadyExistsException;
import com.project.courtfinder.exceptions.ResourceNotFoundException;
import com.project.courtfinder.model.Court;
import com.project.courtfinder.model.User;
import com.project.courtfinder.repository.CourtRepository;
import com.project.courtfinder.repository.UserRepository;
import com.project.courtfinder.request.CreateCourtRequest;
import com.project.courtfinder.request.UpdateCourtRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CourtService implements ICourtService{

    private final CourtRepository courtRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Court createCourt(CreateCourtRequest courtRequest) {
        if(courtRepository.existsByName(courtRequest.getName())){
            throw new AlreadyExistsException("Court with name " + courtRequest.getName() + " already exists!");
        }

        User owner = userRepository.findById(courtRequest.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner with id " + courtRequest.getOwnerId() + " not found!"));

        Court court = new Court(
                courtRequest.getName(),
                courtRequest.getCourtType(),
                courtRequest.getLocation(),
                courtRequest.getDescription(),
                courtRequest.getPricePerHour(),
                courtRequest.getAvailableFrom(),
                courtRequest.getAvailableTo(),
                owner,
                new ArrayList<>()
        );
        return courtRepository.save(court);
    }

    @Override
    public Court getCourtById(Long id) {
        return courtRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Court with id " + id + " not found!"));
    }

    @Override
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    @Override
    public void deleteCourtById(Long id) {
        courtRepository.findById(id)
                .ifPresentOrElse(courtRepository::delete,
                        ()-> {
                            throw new ResourceNotFoundException("Court with id " + id + " not found!");
                        });
    }

    @Override
    public Court updateCourt(UpdateCourtRequest court, Long courtId) {
        Court updatedCourt = courtRepository.findById(courtId)
                .orElseThrow(()-> new ResourceNotFoundException("Court with id " + courtId + " not found!"));

        updatedCourt.setName(court.getName());
        updatedCourt.setLocation(court.getLocation());
        updatedCourt.setDescription(court.getDescription());
        updatedCourt.setPricePerHour(court.getPricePerHour());
        updatedCourt.setAvailableFrom(court.getAvailableFrom());
        updatedCourt.setAvailableTo(court.getAvailableTo());

        return courtRepository.save(updatedCourt);
    }

    @Override
    public CourtDto convertCourtToDto(Court court) {
        return modelMapper.map(court, CourtDto.class);
    }

    @Override
    public List<Court> getCourtsByOwnerId(Long ownerId) {
        boolean exists = userRepository.existsById(ownerId);

        if(!exists){
            throw new ResourceNotFoundException("User with id: " + ownerId + " does not exist!");
        }
        return courtRepository.findByOwnerId(ownerId);
    }
}
