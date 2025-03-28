package org.pinguweb.backend.repository;
import org.pinguweb.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {

}
