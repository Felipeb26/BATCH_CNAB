package com.batsworks.batch.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Component
public class Converts {

    public Map<String, Object> convertToHashMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    public String alteracoesEncontradas(Map<String, Object> objAtual, Map<String, Object> objAtualizado) {
        Set<String> alteracoes = new HashSet<>(objAtual.keySet());
        alteracoes.addAll(objAtualizado.keySet());
        alteracoes.removeAll(objAtual.keySet());

        return alteracoes.toString();
    }

    public String igualdades(Map<String, Object> objAtual, Map<String, Object> objAtualizado) {
        List<String> continua = new ArrayList<>();

        for (String key : objAtual.keySet()) {
            if (objAtualizado.containsKey(key) && objAtual.get(key).equals(objAtualizado.get(key))) {
                continua.add(objAtual.get(key).toString());
            }
        }
        return continua.toString();
    }

}
