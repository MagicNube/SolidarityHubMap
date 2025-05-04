package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Catastrophe;
import org.pinguweb.backend.repository.CatastropheRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Getter
@Service
public class CatastropheService {
    private final CatastropheRepository catastropheRepository;

    public CatastropheService(CatastropheRepository catastropheRepository) {this.catastropheRepository = catastropheRepository;}
    public List<Catastrophe> findAll(){return catastropheRepository.findAll();}
    public Optional<Catastrophe> findByID(Integer ID){return catastropheRepository.findByID(ID);}
}
