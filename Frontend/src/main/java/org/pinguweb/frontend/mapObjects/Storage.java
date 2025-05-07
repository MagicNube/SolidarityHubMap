package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.StorageDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import software.xdev.vaadin.maps.leaflet.basictypes.LIcon;
import software.xdev.vaadin.maps.leaflet.basictypes.LIconOptions;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.basictypes.LPoint;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarkerOptions;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@Slf4j
public class Storage extends MapObject{

    LMarker markerObj;
    private String name;
    private int zoneID;
    private boolean full;

    public Storage(LComponentManagementRegistry reg, Double latitude, Double longitude){

        LIcon icon = new LIcon(reg, new LIconOptions()
                .withIconUrl("https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png")
                .withShadowUrl("https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png")
                .withIconSize(new LPoint(reg, 25, 41))
                .withIconAnchor(new LPoint(reg, 12, 41))
                .withPopupAnchor(new LPoint(reg, 1, -34))
                .withShadowSize(new LPoint(reg, 41, 41))
        );

        LMarkerOptions options = new LMarkerOptions().withDraggable(true).withIcon(icon);

        this.markerObj = new LMarker(reg, new LLatLng(reg, latitude, longitude), options);
    }

    @Override
    public void addToMap(LMap map){
        this.getMarkerObj().addTo(map);
    }

    @Override
    public void removeFromMap(LMap map){
        this.getMarkerObj().removeFrom(map);
    }

    @Override
    public int pushToServer(){
        StorageDTO storageDTO = new StorageDTO();
        storageDTO.setLatitude(this.getLatitude());
        storageDTO.setLongitude(this.getLongitude());
        storageDTO.setName(this.getName());
        storageDTO.setZone(this.getZoneID());
        storageDTO.setFull(this.isFull());

        // TODO: AGREGAR LO QUE QUIERAS GUARDAR

        String finurl = "/api/storages";
        try{
            BackendObject<StorageDTO> status = BackendService.postToBackend(BackendService.BACKEND+finurl, storageDTO, StorageDTO.class);
            if (status.getStatusCode() == HttpStatus.OK){
                return status.getData().getID();
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }

    @Override
    public int deleteFromServer() {
        String finurl = "/api/storages/" + this.getID();
        try{
            HttpStatusCode status = BackendService.deleteFromBackend(BackendService.BACKEND+finurl);
            if (status == HttpStatus.OK){
                //TODO: Eliminar del mapa
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }

    @Override
    public int updateToServer() {
        StorageDTO storageDTO = new StorageDTO();
        storageDTO.setLatitude(this.getLatitude());
        storageDTO.setLongitude(this.getLongitude());
        storageDTO.setName(this.getName());
        storageDTO.setZone(this.getZoneID());
        storageDTO.setFull(this.isFull());

        String finurl = "/api/storages/" + this.getID();
        try{
            HttpStatus status = (HttpStatus) BackendService.putToBackend(BackendService.BACKEND+finurl, storageDTO);
            if (status == HttpStatus.OK){

            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }

    public static List<StorageDTO> getAllFromServer() {
        BackendObject<List<StorageDTO>> storages = BackendService.getListFromBackend(BackendService.BACKEND + "/api/storages",
                new ParameterizedTypeReference<>() {
                });

        if (storages.getStatusCode() == HttpStatus.OK) {
            return storages.getData();
        } else if (storages.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.error("FALLO: No se ha encontrado contenido en la petición: /api/storages");
            return new ArrayList<>();
        } else if (storages.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            log.error("FALLO: Petición /api/storages devolvió servicio no disponible. ¿El backend funciona?");
            return new ArrayList<>();
        } else {
            throw new RuntimeException("Backend object return unexpected status code");
        }
    }
}
