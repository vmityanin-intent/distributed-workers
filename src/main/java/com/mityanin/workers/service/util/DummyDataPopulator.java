package com.mityanin.workers.service.util;

import com.mityanin.workers.domain.entity.Work;
import com.mityanin.workers.domain.enums.Status;
import com.mityanin.workers.repository.WorkRepository;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

public class DummyDataPopulator {
    public static void populate(ConfigurableApplicationContext run) {
        final WorkRepository bean = run.getBean(WorkRepository.class);


        final List<Work> works = new LinkedList<>();
        for (int i = 0; i < 10_000; i++) {
            works.add(Work.builder()
                    .url(URI.create("url_" + i))
                    .status(Status.NEW)
                    .build());
        }
        bean.saveAll(works);
        bean.save(Work.builder()
                .url(URI.create("http://google.com"))
                .status(Status.NEW)
                .build());
        bean.save(Work.builder()
                .url(URI.create("http://www.reddit.com"))
                .status(Status.NEW)
                .build());
    }
}
