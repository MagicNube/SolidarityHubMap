package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Need;
import org.pinguweb.backend.repository.NeedRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NeedService {
    @Getter
    private final NeedRepository needRepository;

    public NeedService(NeedRepository needRepository) {this.needRepository = needRepository;}
    public Need saveNeed(Need need) {
        return needRepository.save(need);
    }
    public List<Need> findAll(){return needRepository.findAll();}
    public Optional<Need> findByID(Integer ID){return needRepository.findById(ID);}
    public void deleteNeed(Need need){needRepository.delete(need);}
}

