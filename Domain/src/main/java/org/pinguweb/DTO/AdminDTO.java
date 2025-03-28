package org.pinguweb.DTO;

import lombok.Data;
import org.pinguweb.model.Admin;

@Data
public class AdminDTO {
    private String dni;
    private String password;

    public AdminDTO(Admin admin) {
        dni = admin.getDni();
        password = admin.getPassword();
    }
}
