package com.demo.api.controller;

import com.demo.api.service.FileManagementService;
import com.demo.api.service.VideoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequestMapping("/files")
@RestController
@RequiredArgsConstructor
public class FilesController {

    private final FileManagementService fileManagementService;
    private final VideoService videoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart(name = "videoFile") MultipartFile multipartFile) throws IOException {
        var fileName = multipartFile.getName();
        if (StringUtils.endsWithAny(fileName, ".mp4", ".mpg", ".mpg4", ".mpeg")) {
            return new ResponseEntity<>("Error: Only files with extension mp4/mpg is accepted", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        var fileid = fileManagementService.saveFile(multipartFile.getName(), multipartFile);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.LOCATION, fileid);
        return new ResponseEntity<>("File uploaded successfully", headers, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getAllFile() throws IOException {
        var responseBody = fileManagementService.getFileList();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/{fileid}")
    public void getFileById(@PathVariable String fileid, HttpServletResponse response) throws IOException {
        var videoFile = videoService.getVideo(fileid);
        if (videoFile == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print("File not found");
            out.flush();
            return;
        }
        FileCopyUtils.copy(videoFile.getData(), response.getOutputStream());
    }

    @DeleteMapping("/{fileid}")
    public ResponseEntity<String> deleteFileById(@PathVariable String fileid) throws IOException {
        var deleteFileid = fileManagementService.deleteVideoFileByFileid(fileid);
        if (StringUtils.isBlank(deleteFileid)) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("File was successfully removed", HttpStatus.OK);
    }
}
