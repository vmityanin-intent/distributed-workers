package com.mityanin.workers.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class LoggingItemWriter<T> implements ItemWriter<T> {

    @Override
    public void write(List<? extends T> items) throws Exception {
        log.trace("LoggingItemWriter starts");
        items.forEach(item -> log.info(item.toString()));
        log.trace("LoggingItemWriter stops");

    }
}
