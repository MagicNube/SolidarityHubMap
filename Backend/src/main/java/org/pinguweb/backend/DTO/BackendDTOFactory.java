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
        affectedDTO.setGpsCoordinates(new Tuple<>(afectado.getGpsCoordinates().getLatitude(), afectado.getGpsCoordinates().getLongitude()));
        affectedDTO.setDisability(afectado.isDisability());
        affectedDTO.setNeeds(afectado.getNeeds().stream().map(Need::getId).collect(Collectors.toList()));
        return affectedDTO;
    }

    public CatastropheDTO createCatastropheDTO(Catastrophe catastrophe) {
        CatastropheDTO catastropheDTO = new CatastropheDTO();
        catastropheDTO.setId(catastrophe.getID());
        catastropheDTO.setName(catastrophe.getName());
        catastropheDTO.setDescription(catastrophe.getDescription());
        catastropheDTO.setLocation(new Tuple<>(catastrophe.getLocation().getLatitude(), catastrophe.getLocation().getLongitude()));
        catastropheDTO.setStartDate(catastrophe.getStartDate());
        catastropheDTO.setEmergencyLevel(catastrophe.getEmergencyLevel().name());
        catastropheDTO.setNeeds(catastrophe.getNeeds().stream().map(Need::getId).collect(Collectors.toList()));
        catastropheDTO.setZones(catastrophe.getZones().stream().map(Zone::getId).collect(Collectors.toList()));
        return catastropheDTO;
    }

    public NeedDTO createNeedDTO(Need need) {
        NeedDTO needDTO = new NeedDTO();
        needDTO.setId(need.getId());
        needDTO.setAffected(need.getAffected().getDNI());
        needDTO.setDescription(need.getDescription());
        needDTO.setUrgency(need.getUrgency().name());
        needDTO.setNeedType(need.getNeedType().name());
        needDTO.setLocation(new Tuple<>(need.getLocation().getLatitude(), need.getLocation().getLongitude()));
        needDTO.setCatastrophe(need.getCatastrophe().getID());
        needDTO.setTask(need.getTask().getId());
        return needDTO;
    }

    public SkillDTO createSkillDTO(Skill skill) {
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setName(skill.getName());
        return skillDTO;
    }

    public TaskDTO createTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setNeed(task.getNeed().stream().map(Need::getId).collect(Collectors.toList()));
        taskDTO.setTaskName(task.getTaskName());
        taskDTO.setTaskDescription(task.getTaskDescription());
        taskDTO.setStartTimeDate(task.getStartTimeDate());
        taskDTO.setEstimatedEndTimeDate(task.getEstimatedEndTimeDate());
        taskDTO.setPriority(task.getPriority().name());
        taskDTO.setStatus(task.getStatus().name());
        taskDTO.setType(task.getType().name());
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
        volunteerDTO.setSkills(volunteer.getSkills().stream().map(Skill::getName).collect(Collectors.toList()));
        volunteerDTO.setScheduleAvailabilities(volunteer.getScheduleAvailabilities().stream().map(ScheduleAvailability::getId).collect(Collectors.toList()));
        volunteerDTO.setPreferences(volunteer.getPreferences().stream().map(Preference::getName).collect(Collectors.toList()));
        volunteerDTO.setTasks(volunteer.getTasks().stream().map(Task::getId).collect(Collectors.toList()));
        volunteerDTO.setDonations(volunteer.getDonations().stream().map(Donation::getId).collect(Collectors.toList()));
        volunteerDTO.setCertificates(volunteer.getCertificates().stream().map(Certificate::getId).collect(Collectors.toList()));
        volunteerDTO.setNotifications(volunteer.getNotifications().stream().map(Notification::getId).collect(Collectors.toList()));
        return volunteerDTO;
    }

    public ZoneDTO createZoneDTO(Zone zone) {
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setId(zone.getId());
        zoneDTO.setName(zone.getName());
        zoneDTO.setDescription(zone.getDescription());
        zoneDTO.setEmergencyLevel(zone.getEmergencyLevel().name());
        zoneDTO.setCatastrophes(zone.getCatastrophes().stream().map(Catastrophe::getID).collect(Collectors.toList()));
        zoneDTO.setStorages(zone.getStorages().stream().map(Storage::getId).collect(Collectors.toList()));
        zoneDTO.setPoints(zone.getPoints().stream().map(x -> new Tuple<>(x.getLatitude(), x.getLongitude())).collect(Collectors.toList()));
        return zoneDTO;
    }
}
