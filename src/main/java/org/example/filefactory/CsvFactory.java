package org.example.filefactory;

import org.example.model.FileType;

public class CsvFactory implements FileFactory {
    @Override
    public FileType chooseFileType() {
        return FileType.CSV;
    }
}