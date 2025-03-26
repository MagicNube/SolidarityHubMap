package org.pinguweb.frontend.objects;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Resource {
    
    
    private int id;

    @Setter
        private String type;

    private double quantity;

    @Setter
    private Donation donation;

    @Setter
    private Storage storage;

    public Resource(String type,double quantity) {
        this.type = type;
        this.quantity = quantity;
    }

}
