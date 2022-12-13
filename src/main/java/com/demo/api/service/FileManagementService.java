package com.demo.api.service;

import com.demo.api.domain.FileDomain;
import com.demo.api.dto.FileResponseDto;
import com.demo.api.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DeflaterOutputStream;

@Service
@RequiredArgsConstructor
public class FileManagementService {

    private final FileRepository fileRepository;

    public String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        //TODO: instead of saving data directly through mongoDB repository, use GridFS
        // https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/gridfs/
        var file = FileDomain.builder()
                .name(fileName)
                .data(Arrays.toString(multipartFile.getBytes()))
                .size(multipartFile.getSize())
                .createdAt(Instant.now())
                .build();
        return fileRepository.save(file).getFileid();
    }

    public List<FileResponseDto> getFileList() {
        var rawData = fileRepository.findAll();
        if (CollectionUtils.isNotEmpty(rawData)) {
            return rawData.stream().map(this::convertFileDomainToFileResponseDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private FileResponseDto convertFileDomainToFileResponseDto(FileDomain fileDomain) {
        var fileResponseDto = new FileResponseDto();
        BeanUtils.copyProperties(fileDomain, fileResponseDto);
        return FileResponseDto.builder()
                .fileid(fileDomain.getFileid())
                .name(fileDomain.getName())
                .size(fileDomain.getSize())
                .createdAt(fileDomain.getCreatedAt().toString())
                .build();
    }

    /**
     * This one is not really useful.
     * @param bArray
     * @return
     * @throws IOException
     */
    public static byte[] compress(byte[] bArray) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (DeflaterOutputStream dos = new DeflaterOutputStream(os)) {
            dos.write(bArray);
        }
        return os.toByteArray();
    }
}
