package org.pinguweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CatastropheDTO implements DTO{
    private int id;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private LocalDate startDate;
    private String emergencyLevel;
    private List<Integer> needs;
    private List<Integer> zones;
}
