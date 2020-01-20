package com.mityanin.workers.component;

import com.mityanin.workers.repository.WorkRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.Math.toIntExact;
import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
public class ColumnsEvenPartitioner implements Partitioner {

    public static final String START = "start";
    public static final String STOP = "stop";
    private WorkRepository workRepository;

    private int maxNumberPerFetch;

    @Override
    public Map<String, ExecutionContext> partition(int threadsNumber) {

        final Long max = workRepository.findFirstByOrderByIdDesc().getId();
        final Long min = workRepository.findFirstByOrderByIdAsc().getId();
        final int entitiesNumber = toIntExact((max == null) ? 0 : (max - min + 1));
        int numberOfPages =(int) Math.ceil((double) entitiesNumber / maxNumberPerFetch);


        final Map<String, ExecutionContext> ctx = IntStream.iterate(toIntExact(min), i -> i + maxNumberPerFetch).limit(numberOfPages)
                .boxed()
                .collect(toMap(k -> k, k -> new ExecutionContext()))
                .entrySet()
                .stream()
                .peek(e -> {
                    e.getValue().put(START, e.getKey());
                    e.getValue().put(STOP, e.getKey() + maxNumberPerFetch - 1);
                })
                .collect(toMap(e -> String.valueOf(e.getKey()), Map.Entry::getValue));
        return ctx;

    }
}
