package org.pinguweb.frontend.singleton;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.*;
import org.pinguweb.frontend.singleton.observableList.concrete.BackendDTOObservableList;

@Getter
@Slf4j
public class BackendDTOObservableService extends Singleton {

    private static final BackendDTOObservableService s_pInstancia = new BackendDTOObservableService();

    public static BackendDTOObservableService GetInstancia() {
        return s_pInstancia;
    }

    protected BackendDTOObservableService() {
        try {
            log.info("Iniciando los observadores del backend");
            needList = new BackendDTOObservableList<>("/api/needs", 2);
            Thread.sleep(200); // Espera entre servicios para no explotar el back

            AffectedList = new BackendDTOObservableList<>("/api/affecteds", 2);
            Thread.sleep(200);

            CatastropheList = new BackendDTOObservableList<>("/api/catastrophes", 5);
            Thread.sleep(200);

            RouteList = new BackendDTOObservableList<>("/api/routes", 2);
            Thread.sleep(200);

            RoutePointList = new BackendDTOObservableList<>("/api/routepoints", 2);
            Thread.sleep(200);

            StorageList = new BackendDTOObservableList<>("/api/storages", 5);
            Thread.sleep(200);

            TaskList = new BackendDTOObservableList<>("/api/tasks", 2);
            Thread.sleep(200);

            VolunteerList = new BackendDTOObservableList<>("/api/volunteers", 2);
            Thread.sleep(200);

            ZoneList = new BackendDTOObservableList<>("/api/zones", 2);
        }
        catch (InterruptedException e){
            log.error("Error en una pausa del servicio {} {}", e.getMessage(), e.getStackTrace());
            throw new RuntimeException();
        }
    }

    private final BackendDTOObservableList<NeedDTO> needList;
    private final BackendDTOObservableList<AffectedDTO> AffectedList;
    private final BackendDTOObservableList<CatastropheDTO> CatastropheList;
    private final BackendDTOObservableList<RouteDTO> RouteList;
    private final BackendDTOObservableList<RoutePointDTO> RoutePointList;
    private final BackendDTOObservableList<StorageDTO> StorageList;
    private final BackendDTOObservableList<TaskDTO> TaskList;
    private final BackendDTOObservableList<VolunteerDTO> VolunteerList;
    private final BackendDTOObservableList<ZoneDTO> ZoneList;

    public void shutdown(){
        needList.shutdown();
    }
}
