package com.demo.api.service;

import com.demo.api.dto.VideoFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final GridFsTemplate gridFsTemplate;

    private final GridFsOperations gridFsOperations;

    public String addVideo(MultipartFile multipartFile) throws IOException {
        DBObject metadata = new BasicDBObject();
        var fileName = multipartFile.getResource().getFilename();
        metadata.put("fileName", fileName);
        metadata.put(HttpHeaders.CONTENT_TYPE, multipartFile.getContentType());
        ObjectId fileId = gridFsTemplate.store(multipartFile.getInputStream(), fileName, multipartFile.getContentType(), metadata);
        return fileId.toString();
    }

    public VideoFile getVideo(String fileId) throws IllegalStateException, IOException {
        var file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        if (file == null) {
            return null;
        }

        var videoFile = new VideoFile();
        videoFile.setFileName(file.getFilename());
        videoFile.setData(gridFsOperations.getResource(file).getInputStream());
        var metadata = file.getMetadata();
        if (metadata != null) {
            videoFile.setContentDisposition("attachment; filename=\"" + file.getFilename() + "\"");
            videoFile.setContentType(metadata.getString(HttpHeaders.CONTENT_TYPE));
        }
        return videoFile;
    }

    public void deleteVideo(String fileId) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
    }
}
