package org.pinguweb.backend.controller;

import org.pinguweb.backend.model.Admin;
import org.pinguweb.backend.repository.AdminRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {

    AdminRepository repository;

    @GetMapping("/admin/{id}")
    public ResponseEntity<Admin> getAdmin(@PathVariable String id) {
        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.getReferenceById(id));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<Admin> addAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(repository.save(admin));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void>  deleteAdmin(@PathVariable String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/admin")
    public ResponseEntity<Admin>  updateAdmin(@RequestBody Admin admin) {
        if (repository.existsById(admin.getDni())) {
            return ResponseEntity.ok(repository.save(admin));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admin/login/")
    public ResponseEntity<Void> login(@RequestParam String ID, @RequestParam String password) {
        if (repository.existsById(ID)) {
            Admin admin = repository.getReferenceById(ID);
            if (admin.getPassword().equals(password))
            {
                return ResponseEntity.accepted().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
