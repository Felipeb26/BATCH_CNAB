package com.batsworks.batch.config.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchParameters {

    private Map<String, Object> parameters;

}
