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
        description = need.getDescription();

        if ( need.getAffected() != null) {
            affected = need.getAffected().getDNI();
        }
        if (need.getUrgency() != null) {
            urgency = need.getUrgency().name();
        }
        if (need.getNeedType() != null) {
            needType = need.getNeedType().name();
        }
        if (need.getLocation() != null) {
            location = new Tuple<>(need.getLocation().getLatitude(), need.getLocation().getLongitude());
        }
        if (need.getCatastrophe() != null) {
            catastrophe = need.getCatastrophe().getID();
        }
        if (need.getTask() != null) {
            task = need.getTask().getId();
        }
    }
}
