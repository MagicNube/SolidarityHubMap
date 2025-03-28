package org.pinguweb.backend.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@MappedSuperclass
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "dni"
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Volunteer.class, name = "Volunteer"),
        @JsonSubTypes.Type(value = Affected.class, name = "Affected")
})
public abstract class Person {
    @Id
    @JsonProperty("dni")
    private String dNI;

    @Setter
    @Column(nullable = false)
    private String firstName;

    @Setter
    @Column(nullable = false)
    private String lastName;

    @Setter
    @Column(unique=true, nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private int phone;

    @Setter
    @Column(nullable = false)
    private String homeAddress;

    @Setter
    @Column(nullable = false)
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