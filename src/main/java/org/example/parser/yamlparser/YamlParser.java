package org.example.parser.yamlparser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.model.BankOperation;
import org.example.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class YamlParser implements Parser {
    ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    @Override
    public void toFile(List<BankOperation> operations, File file) throws IOException {
        MAPPER = YAMLMapper.builder().addModule(new JavaTimeModule()).build();
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        MAPPER.writeValue(file, operations);
    }

    @Override
    public List<BankOperation> toObjectList(File file) throws IOException {
        MAPPER = YAMLMapper.builder().addModule(new JavaTimeModule()).build();
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        return MAPPER.readValue(file, new TypeReference<>() {
        });
    }
}