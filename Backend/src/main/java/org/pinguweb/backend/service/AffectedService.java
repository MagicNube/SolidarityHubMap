package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Affected;
import org.pinguweb.backend.repository.AffectedRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AffectedService {
    @Getter
    private final AffectedRepository AffectedRepository;

    public AffectedService(AffectedRepository AffectedRepository) {this.AffectedRepository = AffectedRepository;}
    public Optional<Affected> findByDni(String dNI) {return AffectedRepository.findById(dNI);}
    public List<Affected> findAll() {return AffectedRepository.findAll();}
}
