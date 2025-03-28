package org.pinguweb.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pinguweb.model.Affected;
import org.pinguweb.model.Need;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class AffectedDTO implements DTO{
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
        disability = afectado.isDisability();

        if (afectado.getGpsCoordinates() != null) {
            System.out.println(afectado.getGpsCoordinates());
        }
        if (afectado.getNeeds() != null) {
            needs = afectado.getNeeds().stream().map(Need::getId).collect(Collectors.toList());
        }
    }
}
