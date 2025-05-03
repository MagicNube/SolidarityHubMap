package org.pinguweb.backend;

import lombok.extern.slf4j.Slf4j;
import org.pingu.persistence.model.Admin;
import org.pingu.persistence.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@SpringBootApplication(scanBasePackages = "org.pingu")
public class BackendApplication implements CommandLineRunner {

    @Autowired
    private AdminService adminService;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Admin admin = new Admin("admin", encoder.encode("admin"));

        adminService.saveAdmin(admin);

        log.debug("Admin guardado correctamente");
    }
}
