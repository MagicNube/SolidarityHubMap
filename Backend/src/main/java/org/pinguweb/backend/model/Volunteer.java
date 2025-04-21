package org.pinguweb.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pingu.domain.enums.TaskType;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Volunteer extends Person {

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL)
    private List<ScheduleAvailability> scheduleAvailabilities;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<TaskType> taskTypes;

    @ManyToMany
    @JoinTable(name = "volunteer_tasks",
            joinColumns = @JoinColumn(name = "volunteer_dni"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> tasks;

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL)
    private List<Donation> donations;

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL)
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "volunteer")
    private List<Notification> notifications;

    @OneToOne(cascade = CascadeType.ALL)
    private GPSCoordinates location;

    public Volunteer(String dNI, String firstName, String lastName, String email,
                     int phone, String address, String password, List<ScheduleAvailability> scheduleAvailabilities,
                     List<TaskType> taskTypes) {
        super(dNI, firstName, lastName, email, phone, address, password);
        this.tasks = new ArrayList<>();
        this.donations = new ArrayList<>();
        this.certificates = new ArrayList<>();
        this.scheduleAvailabilities = scheduleAvailabilities;
        this.taskTypes = taskTypes;
        for (ScheduleAvailability s : this.scheduleAvailabilities) {
            s.setVolunteer(this);
        }
    }
}