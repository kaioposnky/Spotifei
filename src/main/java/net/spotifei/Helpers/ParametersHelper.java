package net.spotifei.Helpers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ParametersHelper {

    public static Map<String, Object> getParametersFromObject(Object object) {
        Map<String, Object> params = new HashMap<>();
        Class<?> clazz = object.getClass();

        while (clazz != null){
            Field[] fields = clazz.getFields();
            for(Field field : fields){
                field.setAccessible(true);
                try {
                    params.put(field.getName().toLowerCase(), field.get(object));
                } catch (IllegalAccessException e) {
                    // teoricamente esse catch nunca vai ser executado porque o field sempre é acessível
                    // se for executado da throw em um runtimeexception para evitar erros que podem quebrar o codigo
                    throw new RuntimeException(e);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return params;
    }
}
