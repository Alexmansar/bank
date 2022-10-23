package org.example.parser;

import org.example.model.BankOperation;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Parser {

    void toFile(List<BankOperation> operation, File file) throws IOException;

    List<BankOperation> toObjectList(File file) throws IOException;

}