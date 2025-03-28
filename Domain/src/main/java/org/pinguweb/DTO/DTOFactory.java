package org.pinguweb.DTO;

import java.lang.reflect.Constructor;

public class DTOFactory {
    public static <T extends DTO> T createDTO(Class<T> dtoClass) {
        try {
            Constructor<T> constructor = dtoClass.getConstructor();
            return constructor.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends DTO> T createDTO(Class<T> dtoClass, Object dtoParent){
        try{
            Constructor<T> constructor = dtoClass.getConstructor(dtoParent.getClass());
            return constructor.newInstance(dtoParent);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
