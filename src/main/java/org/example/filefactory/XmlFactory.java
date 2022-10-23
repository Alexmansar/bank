package org.example.filefactory;

import org.example.model.FileType;

public class XmlFactory implements FileFactory {
    @Override
    public FileType chooseFileType() {
        return FileType.XML;
    }
}