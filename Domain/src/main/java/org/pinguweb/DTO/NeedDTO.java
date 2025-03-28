package org.pinguweb.DTO;
import lombok.Data;
import org.pinguweb.model.Need;
import org.yaml.snakeyaml.util.Tuple;

@Data
public class NeedDTO {
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
