package com.batsworks.batch.config.cnab;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CnabMultiResource extends MultiResourcePartitioner {
    @Value("${configuration.max_size}")
    private int maxSize;


    @Override
    public void setResources(Resource[] resources) {
        super.setResources(resources);
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        int min = 1;
        int targetSize = (maxSize - min) / gridSize + 1;
        log.info("target size: {}", targetSize);
        Map<String, ExecutionContext> result = new HashMap<>();

        int number = 0;
        int start = min;
        int end = start + targetSize - 1;

        while (start <= maxSize) {
            ExecutionContext context = new ExecutionContext();
            result.put("partition" + number, context);

            if (end >= maxSize) {
                end = maxSize;
            }

            context.putInt("minValue", start);
            context.putInt("maxValue", end);
            start += targetSize;
            end += targetSize;
            number++;
        }
        log.info("particion result: {}", result.toString());
        return result;
    }
}
