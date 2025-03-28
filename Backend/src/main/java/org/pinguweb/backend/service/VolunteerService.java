package org.pinguweb.backend.service;

import org.pinguweb.backend.model.Volunteer;
import org.springframework.stereotype.Service;
import org.pinguweb.backend.repository.VolunteerRepository;

@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;
    public VolunteerService(VolunteerRepository volunteerRepository) {this.volunteerRepository = volunteerRepository;}
    public Volunteer saveVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }
}