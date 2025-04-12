package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Admin;
import org.pinguweb.backend.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    @Getter
    private final AdminRepository adminRepository;
    public AdminService(AdminRepository adminRepository) {this.adminRepository = adminRepository;}
    public Admin saveAdmin(Admin admin) {return adminRepository.save(admin);}
    public Optional<Admin> findByDni(String dni) {return adminRepository.findByDni(dni);}
}
