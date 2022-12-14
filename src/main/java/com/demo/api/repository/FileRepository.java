package com.demo.api.repository;

import com.demo.api.domain.FileDomain;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface FileRepository extends MongoRepository<FileDomain, String>{

    FileDomain findByFileid(String fileid);
}
