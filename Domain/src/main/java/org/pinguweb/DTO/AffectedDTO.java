package org.pinguweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AffectedDTO implements DTO{
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private int phone;
    private String homeAddress;
    private String password;
    private String affectedAddress;
    private Double latitude;
    private Double longitude;
    private boolean disability;
    private List<Integer> needs;
}
