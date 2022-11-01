package org.example.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum FileType {
    JSON("json"), YAML("yaml"), XML("xml"), CSV("csv");
    public final String fileFormat;

    FileType(String fileFormat) {
        this.fileFormat = fileFormat;
    }
}