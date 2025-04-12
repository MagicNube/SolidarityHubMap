package org.pinguweb.backend.DTO;

import org.pinguweb.DTO.NeedDTO;
import org.pinguweb.DTO.ZoneDTO;
import org.pinguweb.backend.model.GPSCoordinates;
import org.pinguweb.backend.model.Need;
import org.pinguweb.backend.model.Zone;
import org.pinguweb.backend.model.enums.EmergencyLevel;
import org.pinguweb.backend.model.enums.Status;
import org.pinguweb.backend.model.enums.TaskType;
import org.pinguweb.backend.model.enums.UrgencyLevel;
import org.pinguweb.backend.repository.AffectedRepository;
import org.pinguweb.backend.repository.CatastropheRepository;
import org.pinguweb.backend.repository.StorageRepository;
import org.pinguweb.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelDTOFactory {
    @Autowired
    CatastropheRepository catastropheRepository;

    @Autowired
    StorageRepository storageRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AffectedRepository affectedRepository;

    public Zone createFromDTO(ZoneDTO dto){
        Zone zona = new Zone();
        zona.setName(dto.getName());
        zona.setDescription(dto.getDescription());
        zona.setEmergencyLevel(EmergencyLevel.valueOf(dto.getEmergencyLevel()));

        zona.setCatastrophe(catastropheRepository.getReferenceById(dto.getCatastrophe()));

        for (int id : dto.getStorages()){
            zona.getStorages().add(storageRepository.getReferenceById(id));
        }

        for (int i = 0; i < dto.getLatitudes().size(); i++){
            GPSCoordinates coord = new GPSCoordinates(dto.getLatitudes().get(i), dto.getLongitudes().get(i));
            zona.getPoints().add(coord);
        }

        return zona;
    }

    public Need createFromDTO(NeedDTO dto){
        Need need = new Need();

        need.setDescription(dto.getDescription());
        need.setUrgency(UrgencyLevel.valueOf(dto.getUrgency()));
        need.setTaskType(TaskType.valueOf(dto.getNeedType()));

        GPSCoordinates coord = new GPSCoordinates(dto.getLatitude(), dto.getLongitude());
        need.setLocation(coord);

        need.setCatastrophe(catastropheRepository.getReferenceById(dto.getCatastrophe()));
        need.setTask(taskRepository.getReferenceById(dto.getTask()));
        need.setAffected(affectedRepository.getReferenceById(dto.getAffected()));
        need.setStatus(Status.valueOf(dto.getStatus()));

        return need;
    }
}
