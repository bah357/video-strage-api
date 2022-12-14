package com.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoFile {
    private String fileName;
    private InputStream data;
}
