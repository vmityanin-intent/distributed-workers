package com.mityanin.workers.repository;

import com.mityanin.workers.domain.entity.Work;
import com.mityanin.workers.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;

public interface WorkRepository extends PagingAndSortingRepository<Work, Long> {

    Work findFirstByStatus(Status status);

    Page<Work> findAllByIdBetweenAndStatus(Long from, Long to,Status status, Pageable pr);

    Work findFirstByOrderByIdAsc();

    Work findFirstByOrderByIdDesc();
}
