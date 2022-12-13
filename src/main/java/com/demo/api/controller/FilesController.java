package com.demo.api.controller;

import com.demo.api.service.FileManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequestMapping("/v1/files")
@RestController
@RequiredArgsConstructor
public class FilesController {

    private final FileManagementService fileManagementService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestPart(name = "videoFile") MultipartFile multipartFile) throws IOException {
        var fileName = multipartFile.getOriginalFilename();
        if (fileName == null) {
            return new ResponseEntity<>("Error: fileName cannot be null", HttpStatus.BAD_REQUEST);
        } else if (!fileName.endsWith(".mp4") && !fileName.endsWith(".mpg")) {
            return new ResponseEntity<>("Error: Only files with extension mp4/mpg is accepted", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        multipartFile.getContentType();

        var filePath = fileManagementService.saveFile(StringUtils.cleanPath(fileName), multipartFile);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.LOCATION, filePath);

        return new ResponseEntity<>("File uploaded successfully", headers, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getAllFile() throws IOException {
        var responseBody = fileManagementService.getFileList();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
