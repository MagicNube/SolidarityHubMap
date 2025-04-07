package org.pinguweb.backend.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String dni;
    private String password;
}
