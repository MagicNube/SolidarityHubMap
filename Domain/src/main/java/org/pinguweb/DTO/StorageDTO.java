package org.pinguweb.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StorageDTO implements DTO {
    private Integer ID;
    private String name;
    private Double latitude;
    private Double longitude;
    private boolean isFull;
    private List<Integer> resources;
    private Integer zone;
}
