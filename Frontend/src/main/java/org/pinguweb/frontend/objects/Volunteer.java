package org.pinguweb.frontend.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Volunteer extends Person {

    private List<Skill> skills;

    private List<ScheduleAvailability> scheduleAvailabilities;

    private List<Preference> preferences;

    private List<Task> tasks;

    private List<Donation> donations;

    private List<Certificate> certificates;

    private List<Notification> notifications;

    public Volunteer(String dNI, String firstName, String lastName, String email,
                     int phone, String address, String password, List<Skill> skills,
                     List<ScheduleAvailability> scheduleAvailabilities, List<Preference> preferences) {
        super(dNI, firstName, lastName, email, phone, address, password);
        this.tasks = new ArrayList<>();
        this.donations = new ArrayList<>();
        this.certificates = new ArrayList<>();
        this.skills = skills;
        this.scheduleAvailabilities = scheduleAvailabilities;
        this.preferences = preferences;
        for (ScheduleAvailability s : this.scheduleAvailabilities) {
            s.setVolunteer(this);
        }
    }
}
