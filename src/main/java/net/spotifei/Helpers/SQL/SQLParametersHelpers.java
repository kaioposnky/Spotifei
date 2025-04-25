package net.spotifei.Helpers.SQL;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SQLParametersHelpers {

    public Map<String, Object> getParametersFromObject(Object object){
        Map<String, Object> params = new HashMap<>();
        Class<?> clazz = object.getClass();

        while (clazz != null){
            Field[] fields = clazz.getFields();
            for(Field field : fields){
                try {
                    params.put(field.getName().toLowerCase(), field.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return params;
    }
}
