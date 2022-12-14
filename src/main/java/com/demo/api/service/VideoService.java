package com.demo.api.service;

import com.demo.api.dto.VideoFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final GridFsTemplate gridFsTemplate;

    private final GridFsOperations gridFsOperations;

    public String addVideo(String fileName, MultipartFile multipartFile) throws IOException {
        DBObject metadata = new BasicDBObject();
        metadata.put("type", "video");
        metadata.put("fileName", fileName);
        ObjectId fileId = gridFsTemplate.store(multipartFile.getInputStream(), fileName, multipartFile.getContentType(), metadata);
        return fileId.toString();
    }

    public VideoFile getVideo(String fileId) throws IllegalStateException, IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        if (file == null) {
            return null;
        }
        return VideoFile.builder()
                .fileName(file.getFilename())
                .data(gridFsOperations.getResource(file).getInputStream())
                .build();
    }

    public void deleteVideo(String fileId) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
    }
}
