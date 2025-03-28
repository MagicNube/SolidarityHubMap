package org.pinguweb.DTO;

import java.lang.reflect.Constructor;

public interface DTOFactory<T extends DTO, J> {
    T createDTO(Class<T> dtoClass);
    T createDTO(Class<T> dtoClass, J object);
}
