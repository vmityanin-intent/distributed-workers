package com.mityanin.workers.domain.entity;

import com.mityanin.workers.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkJob {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Status status;


}
