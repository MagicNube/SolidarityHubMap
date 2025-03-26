package org.pinguweb.backend.DTO;

import lombok.Data;
import org.pinguweb.backend.model.Admin;
import org.pinguweb.backend.model.Need;
import org.pinguweb.backend.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AdminDTO {
    private String dni;
    private String password;

    public AdminDTO(Admin admin) {
        dni = admin.getDni();
        password = admin.getPassword();
    }
}
