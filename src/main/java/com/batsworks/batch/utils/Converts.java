package com.batsworks.batch.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.deepEquals;
import static java.util.Objects.nonNull;

@Slf4j
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

    public Map<String, Object> alteracoes(Map<String, Object> objAtual, Map<String, Object> objAtualizado) {
        Map<String, Object> map = new HashMap<>();

        for (Map.Entry<String, Object> entry : objAtual.entrySet()) {
            var newValue = objAtualizado.get(entry.getKey());
            if (nonNull(newValue) && nonNull(entry.getValue()) && !deepEquals(entry.getValue(), newValue)) {
                if (!entry.getValue().equals(newValue)) {
                    map.put(entry.getKey(), newValue);
                }
            }
        }
        return map;
    }

    public Map<String, Object> camposAntigos(Map<String, Object> objAtual, Map<String, Object> objAtualizado) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : objAtual.entrySet()) {
            String chave = entry.getKey();
            Object valorOriginal = entry.getValue();
            Object valorNovo = objAtualizado.get(chave);
            if (valorNovo != null && !valorNovo.equals(valorOriginal)) {
                map.put(chave, valorOriginal);
            }
        }
        return map;
    }

}
