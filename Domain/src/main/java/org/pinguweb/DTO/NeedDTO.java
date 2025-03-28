package org.pinguweb.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.util.Tuple;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class NeedDTO implements DTO{
    private int id;
    private String affected;
    private String description;
    private String urgency;
    private String needType;
    private Tuple<Double, Double> location;
    private int catastrophe;
    private int task;
}
