package org.pinguweb.backend.DTO;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.pinguweb.backend.model.*;
import org.pinguweb.backend.model.enums.EmergencyLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
