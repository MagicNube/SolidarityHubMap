package org.pinguweb.frontend.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class CatastropheDTO {
    private int id;
    private String name;
    private String description;
    private int location;
    private LocalDate startDate;
    private String emergencyLevel;
    private List<Integer> needs;
    private List<Integer> zones;
}
