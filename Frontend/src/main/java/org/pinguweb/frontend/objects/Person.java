package org.pinguweb.frontend.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public abstract class Person {

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