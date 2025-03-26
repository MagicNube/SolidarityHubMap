package org.pinguweb.frontend.DTO;

import lombok.Data;

@Data
public class NeedDTO {
    private int id;
    private String affected;
    private String description;
    private String urgency;
    private String needType;
    private int location;
    private int catastrophe;
    private int task;
}
