package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Storage;
import org.pinguweb.backend.repository.StorageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StorageService {
    @Getter
    private final StorageRepository storageRepository;
    public StorageService(StorageRepository storageRepository) {this.storageRepository = storageRepository;}
    public Storage saveStorage(Storage storage) {
        return storageRepository.save(storage);
    }
    public Optional<Storage> findByID(Integer ID){return this.storageRepository.findById(ID);}
    public List<Storage> findAll(){return this.storageRepository.findAll();}
    public void delete(Storage storage){this.storageRepository.delete(storage);}
}