package org.pinguweb.frontend.objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Donation {
    
    
    private int id;

    @Setter
    private Volunteer volunteer;

    
    private List<Resource> resources;

    public Donation(Volunteer volunteer, Resource resource) {
        this.volunteer = volunteer;
        this.resources = List.of(resource);
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
    }
}
