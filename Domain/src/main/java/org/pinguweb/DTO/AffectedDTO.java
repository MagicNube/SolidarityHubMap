package org.pinguweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.util.Tuple;

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
    private Tuple<Double, Double> gpsCoordinates;
    private boolean disability;
    private List<Integer> needs;
}
