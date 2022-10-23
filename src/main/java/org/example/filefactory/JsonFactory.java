package org.example.filefactory;

import org.example.model.FileType;

public class JsonFactory implements FileFactory {
    @Override
    public FileType chooseFileType() {
        return FileType.JSON;
    }
}