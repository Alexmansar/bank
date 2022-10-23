package org.example.filefactory;

import org.example.model.FileType;

public class YamlFactory implements FileFactory {
    @Override
    public FileType chooseFileType() {
        return FileType.YAML;
    }
}