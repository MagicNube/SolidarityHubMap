package org.pinguweb.backend.service;

import org.pinguweb.backend.model.Admin;
import org.pinguweb.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AdminRepository adminRepository;

    //private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean authenticate(String dni, String password) {

        Admin admin = adminRepository.findByDni(dni);

        if (admin != null) {
            System.out.println("CONTRASEÑA: " + password);
            return admin.getPassword().equals(password);
        }
        return false;
    }
}
