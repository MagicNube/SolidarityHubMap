package org.pinguweb.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.pinguweb.model.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class VolunteerDTO implements DTO{
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


    public VolunteerDTO(Volunteer volunteer) {
        dni = volunteer.getDNI();
        firstName = volunteer.getFirstName();
        lastName = volunteer.getLastName();
        email = volunteer.getEmail();
        phone = volunteer.getPhone();
        homeAddress = volunteer.getHomeAddress();
        password = volunteer.getPassword();
        skills = volunteer.getSkills().stream().map(Skill::getName).collect(Collectors.toList());
        scheduleAvailabilities = volunteer.getScheduleAvailabilities().stream().map(ScheduleAvailability::getId).collect(Collectors.toList());
        preferences = volunteer.getPreferences().stream().map(Preference::getName).collect(Collectors.toList());
        tasks = volunteer.getTasks().stream().map(Task::getId).collect(Collectors.toList());
        donations = volunteer.getDonations().stream().map(Donation::getId).collect(Collectors.toList());
        certificates = volunteer.getCertificates().stream().map(Certificate::getId).collect(Collectors.toList());
        notifications = volunteer.getNotifications().stream().map(Notification::getId).collect(Collectors.toList());
    }
}
