package org.pinguweb.backend;

import org.pingu.persistence.model.Admin;
import org.pingu.persistence.repository.AdminRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService(AdminRepository adminRepository,
                                 BCryptPasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String dni, String password) {
        Optional<Admin> admin = adminRepository.findByDni(dni);
        return admin.filter(value -> passwordEncoder.matches(password, value.getPassword())).isPresent();
    }
}
