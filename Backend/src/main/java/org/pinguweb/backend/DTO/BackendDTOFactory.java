package org.pinguweb.backend.DTO;

import lombok.NoArgsConstructor;
import org.pinguweb.DTO.DTO;

@NoArgsConstructor
public class BackendDTOFactory<T extends DTO>{

    private Class<T> dtoClass;

    public BackendDTOFactory(Class<T> dtoClass) {
        this.dtoClass = dtoClass;
    }
}
