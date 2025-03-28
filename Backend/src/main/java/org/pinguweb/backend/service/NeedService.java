package org.pinguweb.backend.service;

import org.pinguweb.backend.model.Need;
import org.pinguweb.backend.repository.NeedRepository;
import org.springframework.stereotype.Service;

@Service
public class NeedService {
    private final NeedRepository needRepository;
    public NeedService(NeedRepository needRepository) {this.needRepository = needRepository;}
    public Need saveNeed(Need need) {
        return needRepository.save(need);
    }
}

