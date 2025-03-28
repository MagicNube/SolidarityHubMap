package org.pinguweb.backend.service;

import org.pinguweb.model.Admin;
import org.pinguweb.backend.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    public AdminService(AdminRepository adminRepository) {this.adminRepository = adminRepository;}
    public Admin saveAdmin(Admin admin) {return adminRepository.save(admin);}
}
