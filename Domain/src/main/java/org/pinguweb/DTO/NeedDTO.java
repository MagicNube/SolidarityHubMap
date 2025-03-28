package org.pinguweb.DTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pinguweb.model.Need;
import org.yaml.snakeyaml.util.Tuple;

@NoArgsConstructor
@Data
public class NeedDTO implements DTO{
    private int id;
    private String affected;
    private String description;
    private String urgency;
    private String needType;
    private Tuple<Double, Double> location;
    private int catastrophe;
    private int task;

    public NeedDTO(Need need) {
        id = need.getId();
        affected = need.getAffected().getDNI();
        description = need.getDescription();
        urgency = need.getUrgency().name();
        needType = need.getNeedType().name();
        location = new Tuple<>(need.getLocation().getLatitude(), need.getLocation().getLongitude());
        catastrophe = need.getCatastrophe().getID();
        task = need.getTask().getId();
    }
}
