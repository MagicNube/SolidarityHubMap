package org.pinguweb.backend.service;

import org.springframework.stereotype.Service;
import org.pinguweb.backend.repository.AffectedRepository;
import org.pinguweb.backend.model.Affected;

@Service
public class AffectedService {
    private final AffectedRepository AffectedRepository;
    public AffectedService(AffectedRepository AffectedRepository) {this.AffectedRepository = AffectedRepository;}
    public Affected saveAffected(Affected Affected) {
        return AffectedRepository.save(Affected);
    }
}
