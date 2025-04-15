package org.pinguweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RoutePointDTO implements DTO{
    private int ID;
    private Double latitude;
    private Double longitude;
    private String routeType;
    private Integer route;
}
