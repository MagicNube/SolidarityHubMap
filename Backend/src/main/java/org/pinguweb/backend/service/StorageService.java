package org.pinguweb.backend.service;

import org.pinguweb.model.Storage;
import org.springframework.stereotype.Service;
import org.pinguweb.backend.repository.StorageRepository;

@Service
public class StorageService {
    private final StorageRepository storageRepository;
    public StorageService(StorageRepository storageRepository) {this.storageRepository = storageRepository;}
    public Storage saveStorage(Storage storage) {
        return storageRepository.save(storage);
    }
}