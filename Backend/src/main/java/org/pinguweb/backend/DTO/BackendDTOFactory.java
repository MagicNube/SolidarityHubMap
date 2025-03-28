package org.pinguweb.backend.DTO;

import lombok.NoArgsConstructor;
import org.pinguweb.DTO.*;
import org.pinguweb.backend.model.*;
import org.yaml.snakeyaml.util.Tuple;

import java.util.stream.Collectors;

@NoArgsConstructor
public class BackendDTOFactory{

    public AdminDTO createAdminDTO(Admin admin){
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setDni(admin.getDni());
        adminDTO.setPassword(admin.getPassword());
        return adminDTO;
    }

    public AffectedDTO createAffectedDTO(Affected afectado) {
        AffectedDTO affectedDTO = new AffectedDTO();
        affectedDTO.setDni(afectado.getDNI());
        affectedDTO.setFirstName(afectado.getFirstName());
        affectedDTO.setLastName(afectado.getLastName());
        affectedDTO.setEmail(afectado.getEmail());
        affectedDTO.setPhone(afectado.getPhone());
        affectedDTO.setHomeAddress(afectado.getHomeAddress());
        affectedDTO.setPassword(afectado.getPassword());
        affectedDTO.setAffectedAddress(afectado.getAffectedAddress());
        affectedDTO.setDisability(afectado.isDisability());
        affectedDTO.setDisability(afectado.isDisability());

        if (afectado.getGpsCoordinates() != null) {
            affectedDTO.setLatitude(afectado.getGpsCoordinates().getLatitude());
            affectedDTO.setLongitude(afectado.getGpsCoordinates().getLongitude());
        }
        if (afectado.getNeeds() != null) {
            affectedDTO.setNeeds(afectado.getNeeds().stream().map(Need::getId).collect(Collectors.toList()));
        }
        return affectedDTO;
    }

    public CatastropheDTO createCatastropheDTO(Catastrophe catastrophe) {
        CatastropheDTO catastropheDTO = new CatastropheDTO();
        catastropheDTO.setId(catastrophe.getID());
        catastropheDTO.setName(catastrophe.getName());
        catastropheDTO.setDescription(catastrophe.getDescription());
        catastropheDTO.setStartDate(catastrophe.getStartDate());
        catastropheDTO.setEmergencyLevel(catastrophe.getEmergencyLevel().name());
        catastropheDTO.setLatitude(catastrophe.getLocation().getLatitude());
        catastropheDTO.setLongitude(catastrophe.getLocation().getLongitude());

        if (catastrophe.getNeeds() != null) {
            catastropheDTO.setNeeds(catastrophe.getNeeds().stream().map(Need::getId).collect(Collectors.toList()));
        }
        if (catastrophe.getZones() != null) {
            catastropheDTO.setZones(catastrophe.getZones().stream().map(Zone::getId).collect(Collectors.toList()));
        }

        return catastropheDTO;
    }

    public NeedDTO createNeedDTO(Need need) {
        NeedDTO needDTO = new NeedDTO();
        needDTO.setId(need.getId());
        needDTO.setDescription(need.getDescription());

        if (need.getNeedType() != null) {
            needDTO.setNeedType(need.getNeedType().name());
        }
        if (need.getLocation() != null) {
            needDTO.setLatitude(need.getLocation().getLatitude());
            needDTO.setLongitude(need.getLocation().getLongitude());
        }
        if (need.getAffected() != null) {
            needDTO.setAffected(need.getAffected().getDNI());
        }
        if (need.getUrgency() != null) {
            needDTO.setUrgency(need.getUrgency().name());
        }
        if (need.getCatastrophe() != null) {
            needDTO.setCatastrophe(need.getCatastrophe().getID());
        }
        if (need.getTask() != null) {
            needDTO.setTask(need.getTask().getId());
        }

        return needDTO;
    }

    public SkillDTO createSkillDTO(Skill skill) {
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setName(skill.getName());
        return skillDTO;
    }

    public TaskDTO createTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName(task.getTaskName());
        taskDTO.setTaskDescription(task.getTaskDescription());
        taskDTO.setStartTimeDate(task.getStartTimeDate());
        taskDTO.setEstimatedEndTimeDate(task.getEstimatedEndTimeDate());

        if (task.getNeed() != null) {
            taskDTO.setNeed(task.getNeed().stream().map(Need::getId).collect(Collectors.toList()));
        }
        if (task.getZone() != null) {
            taskDTO.setZone(task.getZone().getId());
        }
        if (task.getPriority() != null) {
            taskDTO.setPriority(task.getPriority().name());
        }
        if (task.getStatus() != null) {
            taskDTO.setStatus(task.getStatus().name());
        }
        if (task.getType() != null) {
            taskDTO.setType(task.getType().name());
        }

        return taskDTO;
    }

    public VolunteerDTO createVolunteerDTO(Volunteer volunteer) {
        VolunteerDTO volunteerDTO = new VolunteerDTO();
        volunteerDTO.setDni(volunteer.getDNI());
        volunteerDTO.setFirstName(volunteer.getFirstName());
        volunteerDTO.setLastName(volunteer.getLastName());
        volunteerDTO.setEmail(volunteer.getEmail());
        volunteerDTO.setPhone(volunteer.getPhone());
        volunteerDTO.setHomeAddress(volunteer.getHomeAddress());
        volunteerDTO.setPassword(volunteer.getPassword());

        if (volunteer.getSkills() != null) {
            volunteerDTO.setSkills(volunteer.getSkills().stream().map(Skill::getName).collect(Collectors.toList()));
        }
        if (volunteer.getScheduleAvailabilities() != null) {
            volunteerDTO.setScheduleAvailabilities(volunteer.getScheduleAvailabilities().stream().map(ScheduleAvailability::getId).collect(Collectors.toList()));
        }
        if (volunteer.getPreferences() != null) {
            volunteerDTO.setPreferences(volunteer.getPreferences().stream().map(Preference::getName).collect(Collectors.toList()));
        }
        if (volunteer.getTasks() != null) {
            volunteerDTO.setTasks(volunteer.getTasks().stream().map(Task::getId).collect(Collectors.toList()));
        }
        if (volunteer.getDonations() != null) {
            volunteerDTO.setDonations(volunteer.getDonations().stream().map(Donation::getId).collect(Collectors.toList()));
        }
        if (volunteer.getCertificates() != null) {
            volunteerDTO.setCertificates(volunteer.getCertificates().stream().map(Certificate::getId).collect(Collectors.toList()));
        }
        if (volunteer.getNotifications() != null) {
            volunteerDTO.setNotifications(volunteer.getNotifications().stream().map(Notification::getId).collect(Collectors.toList()));
        }



        return volunteerDTO;
    }

    public ZoneDTO createZoneDTO(Zone zone) {
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setId(zone.getId());
        zoneDTO.setName(zone.getName());
        zoneDTO.setDescription(zone.getDescription());

        zoneDTO.setLatitudes(zone.getPoints().stream().map(GPSCoordinates::getLatitude).collect(Collectors.toList()));
        zoneDTO.setLongitudes(zone.getPoints().stream().map(GPSCoordinates::getLongitude).collect(Collectors.toList()));

        if (zone.getEmergencyLevel() != null) {
            zoneDTO.setEmergencyLevel(zone.getEmergencyLevel().name());
        }
        if (zone.getCatastrophes() != null) {
            zoneDTO.setCatastrophes(zone.getCatastrophes().stream().map(Catastrophe::getID).collect(Collectors.toList()));
        }
        if (zone.getStorages() != null) {
            zoneDTO.setStorages(zone.getStorages().stream().map(Storage::getId).collect(Collectors.toList()));
        }

        return zoneDTO;
    }
}
