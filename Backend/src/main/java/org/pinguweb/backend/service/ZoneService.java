package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Zone;
import org.pinguweb.backend.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZoneService {
    @Getter
    private final  ZoneRepository  zoneRepository;
    public ZoneService( ZoneRepository zoneRepository) {this. zoneRepository =  zoneRepository;}
    public Zone saveZone(Zone zone) { return this. zoneRepository.save(zone);}
    public List<Zone> findAll(){return this.zoneRepository.findAll();}
    public Optional<Zone> findByID(Integer ID){return this.zoneRepository.findById(ID);}
    public void delete(Zone zone){this.zoneRepository.delete(zone);}
}