package net.spotifei.Helpers;

//imports
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ParametersHelper {

    /**
     * Extrai todos os campos de um objeto, incluindo os herdados de suas superclasses,
     * e os retorna como um mapa de pares chave-valor.
     *
     * @param object O objeto do qual extrair os parâmetros.
     * @return Um mapa onde as chaves são os nomes dos campos (em minúsculas) e os valores são os valores dos campos.
     * @throws RuntimeException Se ocorrer um IllegalAccessException, embora seja improvável
     * quando setAccessible(true) é usado.
     */
    public static Map<String, Object> getParametersFromObject(Object object) {
        Map<String, Object> params = new HashMap<>();
        Class<?> clazz = object.getClass();

        while (clazz != null){
            Field[] fields = clazz.getDeclaredFields();
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
