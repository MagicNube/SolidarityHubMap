package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Volunteer;
import org.pinguweb.backend.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerService {
    @Getter
    private final VolunteerRepository volunteerRepository;
    public VolunteerService(VolunteerRepository volunteerRepository) {this.volunteerRepository = volunteerRepository;}
    public List<Volunteer> findAll(){return volunteerRepository.findAll();}
    public Optional<Volunteer> findByID(String ID){return  volunteerRepository.findById(ID);}
}