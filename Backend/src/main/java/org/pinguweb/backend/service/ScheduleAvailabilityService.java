package org.pinguweb.backend.service;

import org.pinguweb.backend.model.ScheduleAvailability;
import org.springframework.stereotype.Service;
import org.pinguweb.backend.repository.ScheduleAvailabilityRepository;

@Service
public class ScheduleAvailabilityService {
    private final ScheduleAvailabilityRepository scheduleAvailabilityRepository;
    public ScheduleAvailabilityService(ScheduleAvailabilityRepository scheduleAvailabilityRepository) {this.scheduleAvailabilityRepository = scheduleAvailabilityRepository;}
    public ScheduleAvailability saveScheduleAvailability(ScheduleAvailability scheduleAvailability) {return scheduleAvailabilityRepository.save(scheduleAvailability);}
}
