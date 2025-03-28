package org.pinguweb.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pinguweb.model.Admin;

@NoArgsConstructor
@Data
public class AdminDTO implements DTO{
    private String dni;
    private String password;

    protected AdminDTO(Admin admin) {
        dni = admin.getDni();
        password = admin.getPassword();
    }
}
