package org.pinguweb.backend.service;

import org.pinguweb.model.Zone;
import org.pinguweb.backend.repository.ZoneRepository;
import org.springframework.stereotype.Service;

@Service
public class ZoneService {
    private final  ZoneRepository  zoneRepository;
    public ZoneService( ZoneRepository zoneRepository) {this. zoneRepository =  zoneRepository;}
    public Zone saveZone(Zone zone) { return this. zoneRepository.save(zone);}
}