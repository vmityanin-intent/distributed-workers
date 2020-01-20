package com.mityanin.workers.repository;

import com.mityanin.workers.domain.entity.WorkJob;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WorkJobRepository extends PagingAndSortingRepository<WorkJob, Long> {

}
