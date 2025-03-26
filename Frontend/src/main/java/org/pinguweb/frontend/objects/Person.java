package org.pinguweb.frontend.objects;

import com.fasterxml.jackson.annotation.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "dni"
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Admin.class, name = "Admin"),
        @JsonSubTypes.Type(value = Affected.class, name = "Affected")
})
public abstract class Person {
    @JsonProperty("dni")
    private String dNI;

    @Setter
        private String firstName;

    @Setter
        private String lastName;

    @Setter
    private String email;

    @Setter
        private int phone;

    @Setter
        private String homeAddress;

    @Setter
        private String password;

    public Person(String dNI, String firstName, String lastName, String email,
                     int phone, String address, String password) {
        this.dNI = dNI;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.homeAddress = address;
        this.password = password;
    }
}