package org.pinguweb.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RouteDTO implements DTO{
    private int ID;
    private String name;
    private String routeType;
    private Integer catastrophe;
    private List<Integer> points;
}
