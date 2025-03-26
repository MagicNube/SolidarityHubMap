package org.pinguweb.frontend.DTO;

import lombok.Data;

import java.util.List;
@Data
public class ZoneDTO {
    private int id;
    private String name;
    private String description;
    private String emergencyLevel;
    private List<Integer> catastrophes;
    private List<Integer> storages;
    private List<Integer> points;
}
