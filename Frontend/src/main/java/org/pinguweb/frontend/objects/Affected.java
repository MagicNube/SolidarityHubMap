package org.pinguweb.frontend.objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter

@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "dni",
        scope = Affected.class
)
public class Affected extends Person {
    @Setter
    private String affectedAddress;

    @Setter
    private GPSCoordinates gpsCoordinates;

    @Setter
        private boolean disability;

    
    private List<Need> needs;

    public Affected(String dNI, String firstName, String lastName, String email, int phone,
                    String address, String password, boolean disability) {
        super(dNI, firstName, lastName, email, phone, address, password);
        this.disability = disability;
        this.needs = new ArrayList<>();
    }

    public void addNeed(Need need) {
        this.needs.add(need);
    }

}

