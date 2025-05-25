package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.StorageDTO;
import org.pingu.web.BackendObject;
import org.pingu.web.BackendService;
import org.pinguweb.frontend.services.BackendDTOService;
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

import java.util.Arrays;

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

        LMarkerOptions options = new LMarkerOptions().withDraggable(false).withIcon(icon);

        this.markerObj = new LMarker(reg, new LLatLng(reg, latitude, longitude), options);
    }

    public StorageDTO toDto(){
        StorageDTO storageDTO = new StorageDTO();
        storageDTO.setID(getID());
        storageDTO.setLatitude(this.getLatitude());
        storageDTO.setLongitude(this.getLongitude());
        storageDTO.setName(this.getName());
        storageDTO.setZone(this.getZoneID());
        storageDTO.setFull(this.isFull());
        return storageDTO;
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
        StorageDTO storageDTO = toDto();

        String finurl = "/api/storages" + storageDTO.getID();
        try{
            BackendObject<StorageDTO> status = BackendService.postToBackend(BackendDTOService.BACKEND + finurl, storageDTO, StorageDTO.class);
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
        String finurl = "/api/storages/" + getID();
        try{
            HttpStatusCode status = BackendService.deleteFromBackend(BackendDTOService.BACKEND + finurl);
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
        StorageDTO storageDTO = toDto();

        String finurl = "/api/storages/" + this.getID();
        try{
            HttpStatus status = (HttpStatus) BackendService.putToBackend(BackendDTOService.BACKEND + finurl, storageDTO);
            if (status == HttpStatus.OK){
                log.info("Editado exitosamente");
            }
            else{
                log.info("Error editando");
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }
}
