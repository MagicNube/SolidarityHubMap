package org.pinguweb.frontend.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AffectedDTO {
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private int phone;
    private String homeAddress;
    private String password;
    private String affectedAddress;
    private int gpsCoordinates;
    private boolean disability;
    private List<Integer> needs;
}
