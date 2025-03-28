package org.pinguweb.DTO;

import java.lang.reflect.Constructor;

public class DTOFactory {
    public static <T extends DTO> T createDTO(Class<T> dtoClass) throws ReflectiveOperationException {
        Constructor<T> constructor = dtoClass.getConstructor();
        return constructor.newInstance();
    }

    public static <T extends DTO> T createDTO(Class<T> dtoClass, Object dtoParent) throws ReflectiveOperationException {
        Constructor<T> constructor = dtoClass.getConstructor(dtoParent.getClass());
        return constructor.newInstance(dtoParent);
    }
}
