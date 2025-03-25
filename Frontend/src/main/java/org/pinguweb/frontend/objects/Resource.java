package org.pinguweb.frontend.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
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
