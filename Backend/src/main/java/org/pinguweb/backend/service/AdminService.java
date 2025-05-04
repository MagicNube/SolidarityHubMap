package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Admin;
import org.pinguweb.backend.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {this.adminRepository = adminRepository;}
    //Meter admin a mano, no se pueden crear desde la web
    public Admin saveAdmin(Admin admin) {return adminRepository.save(admin);}
    public Optional<Admin> findByDni(String dni) {return adminRepository.findByDni(dni);}
}
