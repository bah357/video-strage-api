package com.demo.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("file")
@Data
public class FileDomain {

    @Id
    private String id;
    private String fileid;
    private String name;
    private Long size;
    private Instant createdAt;

}
