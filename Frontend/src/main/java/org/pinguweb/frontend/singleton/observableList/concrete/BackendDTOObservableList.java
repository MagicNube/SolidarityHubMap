package org.pinguweb.frontend.singleton.observableList.concrete;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BackendDTOObservableList<T> extends ObservableList<T>{
    private final ScheduledExecutorService scheduler;
    private final String url;

    public BackendDTOObservableList(String url, int minutes) {
        log.info("Creando Polling service en {}", url);
        this.url = url;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("Polling-" + delegate.hashCode());
            return t;
        });
        iniciarPolling(minutes);
    }

    private void iniciarPolling(int minutes) {
        scheduler.scheduleWithFixedDelay(
                this::hacerPolling, 0, minutes, TimeUnit.MINUTES
        );
    }

    private void hacerPolling() {
        try {
            log.info("Empezando polling de {}", this.url);
            List<T> datos = fetchFromBackend();
            synchronized (this.delegate) {
                if (datos.equals(this.delegate)) {
                    this.delegate.clear();
                    this.delegate.addAll(datos);
                }
            }
        } catch (Exception e) {
            log.error("Error en el auto polling, {}, {}", e.getMessage(), e.getStackTrace());
        }
    }

    // Implementa la llamada REST o cliente HTTP
    private List<T> fetchFromBackend() {
        BackendObject<List<T>> objects = BackendService.getListFromBackend(BackendService.BACKEND + url,
                new ParameterizedTypeReference<>() {
                });

        if (objects.getStatusCode() == HttpStatus.OK) {
            return objects.getData();
        } else if (objects.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.error("FALLO: No se ha encontrado contenido en la petición: {}", BackendService.BACKEND + url);
            return new ArrayList<>();
        } else if (objects.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            log.error("FALLO: Petición {} devolvió servicio no disponible. ¿El backend funciona?", BackendService.BACKEND + url);
            return new ArrayList<>();
        } else {
            throw new RuntimeException("Backend object return unexpected status code");
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException ex) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}