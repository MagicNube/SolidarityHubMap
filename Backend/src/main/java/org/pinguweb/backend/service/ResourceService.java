package org.pinguweb.backend.service;

import org.pinguweb.backend.model.Resource;
import org.pinguweb.backend.repository.ResourceRepository;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;
    public ResourceService(ResourceRepository resourceRepository) {this.resourceRepository = resourceRepository;}
    public Resource saveResource(Resource resource) {return resourceRepository.save(resource);}
}
