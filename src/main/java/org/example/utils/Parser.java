package org.example.utils;

import org.example.model.BankOperation;

import java.io.IOException;
import java.util.List;

public interface Parser {

    void toFile(List<BankOperation> operation) throws IOException;

    List<BankOperation> toObjectList() throws IOException;

}