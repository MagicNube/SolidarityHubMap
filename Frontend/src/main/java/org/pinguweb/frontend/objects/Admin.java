package org.pinguweb.frontend.objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter

@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "dni"
)
public class Admin {
    
    private String dni;

    @Setter
        private String password;

    public Admin(String dni, String password) {
        this.dni = dni;
        this.password = password;
    }
}
