package org.pinguweb.frontend.services;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.*;
import org.pingu.web.BackendObservableService.BackendObservableService;
import org.pingu.web.BackendObservableService.Singleton;
import org.pingu.web.BackendObservableService.observableList.DTOObservableList;
import org.springframework.core.ParameterizedTypeReference;

@Getter
@Slf4j
public class BackendDTOService extends Singleton implements BackendObservableService {

    // Lazy loading + gestor de carga?

    private static final BackendDTOService s_pInstancia = new BackendDTOService();
    public static final String BACKEND = "http://localhost:8081";
    public static BackendDTOService GetInstancia() {
        return s_pInstancia;
    }

    protected BackendDTOService() {
        log.info("Iniciando los observadores del backend");
        NeedList = new DTOObservableList<>(BACKEND + "/api/needs", 3, new ParameterizedTypeReference<>() {});
        AffectedList = new DTOObservableList<>(BACKEND + "/api/affecteds", 3,  new ParameterizedTypeReference<>() {});
        CatastropheList = new DTOObservableList<>(BACKEND + "/api/catastrophes", 5,  new ParameterizedTypeReference<>() {});
        RouteList = new DTOObservableList<>(BACKEND + "/api/routes", 3,  new ParameterizedTypeReference<>() {});
        RoutePointList = new DTOObservableList<>(BACKEND + "/api/routepoints", 3,  new ParameterizedTypeReference<>() {});
        StorageList = new DTOObservableList<>(BACKEND + "/api/storages", 5,  new ParameterizedTypeReference<>() {});
        ResourceList = new DTOObservableList<>(BACKEND + "/api/resources", 3,  new ParameterizedTypeReference<>() {});
        TaskList = new DTOObservableList<>(BACKEND + "/api/tasks", 3, new ParameterizedTypeReference<>() {});
        VolunteerList = new DTOObservableList<>(BACKEND + "/api/volunteers", 3,  new ParameterizedTypeReference<>() {});
        ZoneList = new DTOObservableList<>(BACKEND + "/api/zones", 3,  new ParameterizedTypeReference<>() {});
    }

    private final DTOObservableList<NeedDTO> NeedList;
    private final DTOObservableList<ResourceDTO> ResourceList;
    private final DTOObservableList<AffectedDTO> AffectedList;
    private final DTOObservableList<CatastropheDTO> CatastropheList;
    private final DTOObservableList<RouteDTO> RouteList;
    private final DTOObservableList<RoutePointDTO> RoutePointList;
    private final DTOObservableList<StorageDTO> StorageList;
    private final DTOObservableList<TaskDTO> TaskList;
    private final DTOObservableList<VolunteerDTO> VolunteerList;
    private final DTOObservableList<ZoneDTO> ZoneList;

    @Override
    public void shutdown(){
        NeedList.shutdown();
        AffectedList.shutdown();
        CatastropheList.shutdown();
        RouteList.shutdown();
        RoutePointList.shutdown();
        StorageList.shutdown();
        TaskList.shutdown();;
        VolunteerList.shutdown();
        ResourceList.shutdown();
        ZoneList.shutdown();
    }

    @Override
    public void update(){
        NeedList.update();
        AffectedList.update();
        CatastropheList.update();
        RouteList.update();
        RoutePointList.update();
        StorageList.update();
        TaskList.update();
        VolunteerList.update();
        ZoneList.update();
    }
}
