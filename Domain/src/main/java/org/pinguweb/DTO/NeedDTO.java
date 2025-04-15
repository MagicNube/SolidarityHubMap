package org.pinguweb.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class NeedDTO implements DTO{
    private int ID;
    private String affected;
    private String description;
    private String urgency;
    private String needType;
    private Double latitude;
    private Double longitude;
    private String status;
    private int catastrophe;
    private int task;
}
