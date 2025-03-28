package org.pinguweb.DTO;

import lombok.Data;
import org.pinguweb.model.Affected;
import org.pinguweb.model.Need;

import java.util.List;
import java.util.stream.Collectors;

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

    public AffectedDTO(Affected afectado) {
        dni = afectado.getDNI();
        firstName = afectado.getFirstName();
        lastName = afectado.getLastName();
        email = afectado.getEmail();
        phone = afectado.getPhone();
        homeAddress = afectado.getHomeAddress();
        password = afectado.getPassword();
        affectedAddress = afectado.getAffectedAddress();
        gpsCoordinates = afectado.getGpsCoordinates().getId();
        disability = afectado.isDisability();
        needs = afectado.getNeeds().stream().map(Need::getId).collect(Collectors.toList());
    }
}
