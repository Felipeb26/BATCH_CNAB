package com.batsworks.batch.config.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchParameters {

    private Map<String, Object> parameters = new HashMap<>();
}
