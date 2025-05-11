package org.pinguweb.frontend.singleton;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.NeedDTO;
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
            needServerList = new BackendDTOObservableList<>("/api/needs", 2);
            Thread.sleep(100); // Espera entre servicios para no explotar el back
        }
        catch (InterruptedException e){
            log.error("Error en una pausa del servicio {} {}", e.getMessage(), e.getStackTrace());
            throw new RuntimeException();
        }
    }

    private final BackendDTOObservableList<NeedDTO> needServerList;

    public void shutdown(){
        needServerList.shutdown();
    }
}
