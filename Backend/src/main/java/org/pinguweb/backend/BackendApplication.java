package org.pinguweb.backend;

import org.pinguweb.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

    @Autowired
    private AdminService adminService;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /* CODIGO A EJECUTAR PREVIO A LANZAR EL BACKEND
        Admin admin = new Admin("admin", "admin");

        adminService.saveAdmin(admin);

        System.out.println("Admin guardado correctamente");
         */
    }

}