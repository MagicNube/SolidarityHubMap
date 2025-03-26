package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pinguweb.backend.DTO.AdminDTO;
import org.pinguweb.backend.DTO.TaskDTO;

@Getter
@Entity
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "dni"
)
public class Admin {
    @Id
    private String dni;

    @Setter
    @Column(nullable = false)
    private String password;

    public Admin(String dni, String password) {
        this.dni = dni;
        this.password = password;
    }

    public AdminDTO toDTO() {
        return new AdminDTO(this);
    }

    public static Admin fromDTO(AdminDTO dto) {
        Admin admin = new Admin();
        admin.dni = dto.getDni();
        admin.password = dto.getPassword();
        return admin;
    }
}
