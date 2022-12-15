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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

@Slf4j
@RequestMapping("/files")
@RestController
@RequiredArgsConstructor
public class FilesController {

    private final FileManagementService fileManagementService;
    private final VideoService videoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart(name = "data") MultipartFile multipartFile) throws IOException {
        var filename = multipartFile.getResource().getFilename();

        if (!StringUtils.endsWithAny(filename, ".mp4", ".mpg", ".mpg4", ".mpeg")) {
            return new ResponseEntity<>("Error: Only files with extension mp4/mpg is accepted", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        var fileid = fileManagementService.saveFile(multipartFile);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        final URI location = ServletUriComponentsBuilder
                .fromCurrentServletMapping().path("/files/{fileid}").build()
                .expand(fileid).toUri();
        headers.add(HttpHeaders.LOCATION, location.toString());
        return new ResponseEntity<>("File uploaded successfully", headers, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getAllFile() {
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
            out.print("File not found");
            out.flush();
            return;
        }

        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, videoFile.getContentDisposition());
        response.setContentType(videoFile.getContentType());
        FileCopyUtils.copy(videoFile.getData(), response.getOutputStream());
    }

    @DeleteMapping("/{fileid}")
    public ResponseEntity<String> deleteFileById(@PathVariable String fileid) {
        var deleteFileid = fileManagementService.deleteVideoFileByFileid(fileid);
        if (StringUtils.isBlank(deleteFileid)) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("File was successfully removed", HttpStatus.NO_CONTENT);
    }
}
