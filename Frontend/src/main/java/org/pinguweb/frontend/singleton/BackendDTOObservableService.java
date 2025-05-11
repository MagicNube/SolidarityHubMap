package org.pinguweb.frontend.singleton;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.TaskDTO;
import org.pinguweb.frontend.singleton.observableList.concrete.BackendDTOObservableList;

@Slf4j
@Getter
public class BackendDTOObservableService extends Singleton {

    private static final BackendDTOObservableService s_pInstancia = new BackendDTOObservableService();

    public static BackendDTOObservableService GetInstancia() {
        return s_pInstancia;
    }

    protected BackendDTOObservableService() {
        try {
            log.info("Iniciando los observadores del backend");
            taskServerList = new BackendDTOObservableList<>("/api/tasks", 2);
            Thread.sleep(100); // Espera entre servicios para no explotar el back
        }
        catch (InterruptedException e){
            log.error("Error en una pausa del servicio {} {}", e.getMessage(), e.getStackTrace());
            throw new RuntimeException();
        }
    }

    private final BackendDTOObservableList<TaskDTO> taskServerList;

    public void shutdown(){
        taskServerList.shutdown();
    }
}
