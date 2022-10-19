package org.example.utils.jsonparser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.model.BankOperation;
import org.example.utils.Parser;

import java.io.File;
import java.util.List;

@Slf4j
public class JsonParser implements Parser {

    ObjectMapper MAPPER = new ObjectMapper();

    @Override
    @SneakyThrows
    public void toFile(List<BankOperation> operations, File file) {
        MAPPER = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        MAPPER.writeValue(file, operations);
    }

    @SneakyThrows
    public List<BankOperation> toObjectList(File file) {
        MAPPER = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        return MAPPER.readValue(file, new TypeReference<>() {
        });
    }
}