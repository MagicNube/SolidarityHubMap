package org.pinguweb.frontend.DTO;

import lombok.Data;

import java.util.List;

@Data
public class VolunteerDTO {
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private int phone;
    private String homeAddress;
    private String password;
    private List<String> skills;
    private List<Long> scheduleAvailabilities;
    private List<String> preferences;
    private List<Integer> tasks;
    private List<Integer> donations;
    private List<Integer> certificates;
    private List<Integer> notifications;
}
