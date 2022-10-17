package org.example.utils.jsonparser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.model.BankOperation;
import org.example.utils.Parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JsonParser implements Parser {
    public static final String JSON_FILE = "src/main/resources/bankOperation.json";
    File FILE = new File(JSON_FILE);
    ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void toFile(List<BankOperation> operations) throws IOException {
        MAPPER = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        MAPPER.writeValue(FILE, operations);
    }

    public List<BankOperation> toObjectList() throws IOException {
        MAPPER = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        return MAPPER.readValue(FILE, new TypeReference<>() {
        });
    }
}