package com.demo.api.service;

import com.demo.api.domain.Delivery;
import com.demo.api.domain.FileDomain;
import com.demo.api.dto.FileResponseDto;
import com.demo.api.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileManagementService {

    private final FileRepository fileRepository;
    private final VideoService videoService;
    private final MongoTemplate mongoTemplate;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        var fileid = videoService.addVideo(multipartFile);
        var file = FileDomain.builder()
                .fileid(fileid)
                .name(multipartFile.getResource().getFilename())
                .size(multipartFile.getSize())
                .createdAt(Instant.now())
                .build();
        fileRepository.save(file);
        return fileid;
    }

    public List<FileResponseDto> getFileList() {
        var rawData = fileRepository.findAll();
        if (CollectionUtils.isNotEmpty(rawData)) {
            return rawData.stream().map(this::convertFileDomainToFileResponseDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public String deleteVideoFileByFileid(String fileid) {
        var list = fileRepository.findOneDistinctByName("sample.mp4");
        var fileDomain = fileRepository.findByFileid(fileid);
        if (fileDomain == null) {
            return null;
        }
        videoService.deleteVideo(fileid);
        fileRepository.deleteById(fileDomain.getId());
        return fileDomain.getFileid();
    }

    private FileResponseDto convertFileDomainToFileResponseDto(FileDomain fileDomain) {
        return FileResponseDto.builder()
                .fileid(fileDomain.getFileid())
                .name(fileDomain.getName())
                .size(fileDomain.getSize())
                .created_at(fileDomain.getCreatedAt().toString())
                .build();
    }

    public void testMongoTemplate() {

        var aggregation = Aggregation.newAggregation(Aggregation.group("delivery").max("eventTimestamp").as("eventTimestamp"));
        var results = mongoTemplate.aggregate(aggregation, "delivery",  Delivery.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(results)) {
            var first = results.get(0);
            Criteria criteria = Criteria.where("deliveryId").is(first.getDeliveryId()).and("eventTimestamp").is(first.getEventTimestamp());
            if (results.size() > 1) {
                for (int i = 1; i < results.size(); i++) {
                    var delivery = results.get(i);
                    criteria.orOperator(Criteria.where("deliveryId").is(delivery.getDeliveryId()).and("eventTimestamp").is(delivery.getEventTimestamp()));
                }
            }
            Query query = new Query(criteria);
            var deliveryList = mongoTemplate.find(query, Delivery.class);
        }
    }
}
