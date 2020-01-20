package com.mityanin.workers.controller;

import com.mityanin.workers.domain.entity.Work;
import com.mityanin.workers.domain.entity.WorkJob;
import com.mityanin.workers.repository.WorkJobRepository;
import com.mityanin.workers.repository.WorkRepository;
import com.mityanin.workers.service.BatchService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class WorkController {

    private final WorkJobRepository jobRepository;

    private final BatchService batchService;

    private final WorkRepository workRepository;

    @PostMapping("/submit")
    public void submit() throws Exception {
        batchService.run();
    }

    @GetMapping("/status")
    public Iterable<WorkJob> status() throws Exception {
        return jobRepository.findAll();
    }

    @GetMapping("/content")
    public Page<Work> content(Pageable pageable) throws Exception {
        return workRepository.findAll(pageable);
    }
}
