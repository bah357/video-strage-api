package com.demo.api.service;

import com.demo.api.domain.FileDomain;
import com.demo.api.dto.FileResponseDto;
import com.demo.api.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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

    public String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        var fileid = videoService.addVideo(fileName, multipartFile);
        var file = FileDomain.builder()
                .fileid(fileid)
                .name(fileName)
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
                .createdAt(fileDomain.getCreatedAt().toString())
                .build();
    }
}
