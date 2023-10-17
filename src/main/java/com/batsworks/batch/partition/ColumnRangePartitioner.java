package com.batsworks.batch.partition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ColumnRangePartitioner implements Partitioner {
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        int min = 1;
        int max = 3000;
        int targetSize = (max - min) / gridSize + 1;
        log.info("target size: {}", targetSize);
        Map<String, ExecutionContext> result = new HashMap<>();

        int number = 0;
        int start = min;
        int end = start + targetSize - 1;

        while (start <= max) {
            ExecutionContext context = new ExecutionContext();
            result.put("partition" + number, context);

            if (end >= max) {
                end = max;
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
