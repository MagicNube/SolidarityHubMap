package org.pinguweb.DTO;

public interface DTOFactory<T extends DTO, J> {
    T createDTO(Class<T> dtoClass);
    T createDTO(Class<T> dtoClass, J object);
}
