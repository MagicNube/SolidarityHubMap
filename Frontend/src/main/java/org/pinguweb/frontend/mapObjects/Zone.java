package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.ZoneDTO;
import org.pingu.web.BackendObject;
import org.pingu.web.BackendService;
import org.pinguweb.frontend.services.BackendDTOService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolygon;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolylineOptions;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Setter
@Getter
public class Zone extends MapObject{

    private String name;
    private String description;
    private String emergencyLevel;
    private int catastrophe;
    private List<Integer> storages;
    private List<Double> latitudes;
    private List<Double> longitudes;

    List<LLatLng> points;
    LPolygon polygon;

    public Zone(){
        this.points = new ArrayList<>();
    }

    public void addPoint(LComponentManagementRegistry reg, Tuple<Double, Double> coord){
        this.points.add(new LLatLng(reg, coord._1(), coord._2()));
    }

    public void generatePolygon(LComponentManagementRegistry reg, String lineColor, String fillColor){
        this.polygon = new LPolygon(reg, points, new LPolylineOptions().withColor(lineColor).withFillColor(fillColor));
    }

    public ZoneDTO toDto(){
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setName(this.name);
        zoneDTO.setDescription(this.description);
        zoneDTO.setEmergencyLevel(this.emergencyLevel);
        zoneDTO.setCatastrophe(this.catastrophe);
        zoneDTO.setStorages(this.storages);
        zoneDTO.setLatitudes(this.latitudes);
        zoneDTO.setLongitudes(this.longitudes);
        return zoneDTO;
    }

    @Override
    public void addToMap(LMap map){
        this.getPolygon().addTo(map);
    }

    @Override
    public void removeFromMap(LMap map){
        this.getPolygon().removeFrom(map);
    }

    @Override
    public int pushToServer(){
        ZoneDTO zoneDTO = toDto();

        //System.out.println("ZoneDTO: " + zoneDTO.getCatastrophe() + " " + zoneDTO.getStorages());
        String finurl = "/api/zones";
        try{
            BackendObject<ZoneDTO> status = BackendService.postToBackend(BackendDTOService.BACKEND + finurl, zoneDTO, ZoneDTO.class);
            if (status.getStatusCode() == HttpStatus.OK){
                log.debug("Zona creada satisfactoriamente");
                return status.getData().getID();
            }
        }
        catch (Exception e) {
            log.error("Error creando zona");
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());

        }
        return 0;
    }

    @Override
    public int deleteFromServer() {
        String finurl = "/api/zones/" + this.getID();
        try{
            HttpStatusCode status = BackendService.deleteFromBackend(BackendDTOService.BACKEND + finurl);
            if (status == HttpStatus.OK){
                log.debug("Zona eliminada satisfactoriamente");
                return status.value();
            }
            else if (status == HttpStatus.NOT_FOUND){
                log.error("Zona no encontrada en el servidor");
            }
            else if (status == HttpStatus.INTERNAL_SERVER_ERROR){
                log.error("Error interno del servidor al eliminar la zona");
            }
            else{
                log.error("Error inesperado al eliminar la zona");
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }

    @Override
    public int updateToServer() {
        ZoneDTO zoneDTO = toDto();

        String finurl = "/api/zones";
        try{
            HttpStatusCode status = BackendService.putToBackend(BackendDTOService.BACKEND + finurl, zoneDTO);
            if (status == HttpStatus.OK){
                log.debug("Zona actualizada satisfactoriamente");
                return status.value();
            }
            else if (status == HttpStatus.NOT_FOUND){
                log.error("Zona no encontrada en el servidor");
            }
            else if (status == HttpStatus.INTERNAL_SERVER_ERROR){
                log.error("Error interno del servidor al actualizar la zona");
            }
            else{
                log.error("Error inesperado al actualizar la zona");
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }
}
