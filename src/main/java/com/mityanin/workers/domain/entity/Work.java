package com.mityanin.workers.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mityanin.workers.domain.entity.converter.UriToStringConverter;
import com.mityanin.workers.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.URI;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Work {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column
    @Convert(converter = UriToStringConverter.class)
    private URI url;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "http_code")
    private Integer httpCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Work work = (Work) o;
        return id.equals(work.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
