package com.transaction.transac.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvFactory;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.transaction.transac.dto.BaseResponse;
import com.transaction.transac.dto.DecryptCsvDTO;
import com.transaction.transac.utils.AESGCMEncryptor;
import com.transaction.transac.utils.CreateMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("comm/v1")
public class CsvUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    @PostMapping(value = "/decrypt/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse decryptCsv(DecryptCsvDTO decryptCsvDTO) throws Exception {
        try {
            List<Map<String, String>> processedCSV = processCSV(decryptCsvDTO);
            String filePath = decryptCsvDTO.getPath() + decryptCsvDTO.getFileName();
//            Files.write(Paths.get(filePath), csvBytes, StandardOpenOption.CREATE);
            writeCSV(filePath, processedCSV);
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setData(null);
            baseResponse.setMetaDTO(CreateMetaData.createSuccessMetaData());
            return baseResponse;
        } catch (IOException e) {
            LOGGER.error("Exception while processing the csv", e, true);
            throw e;
        }
    }

    private List<Map<String, String>> processCSV(DecryptCsvDTO decryptCsvDTO) throws Exception {
        LOGGER.info("Reading the Csv");
        List<Map<String, String>> csvList = new LinkedList<>();
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        MappingIterator<Map<String, String>> iterator = mapper.reader(Map.class)
                .with(schema)
                .readValues(decryptCsvDTO.getFile().getInputStream());
        while (iterator.hasNext()) {
            csvList.add(iterator.next());
        }
        String secretKey = decryptCsvDTO.getSecretKey();

        for (Map<String, String> inputMap : csvList) {
            for (String column : decryptCsvDTO.getColumns()) {
                String encryptedValue = inputMap.get(column);
                String decryptValue = "";
                if (StringUtils.hasText(encryptedValue)) {
                    decryptValue = getDecryptedValue(encryptedValue, secretKey);
                }
                if(!StringUtils.hasText(decryptValue)){
                    LOGGER.error("Could not decrypt identifier of userId: {}", inputMap.get("user_id"));
                }
                inputMap.put(column, decryptValue);
            }
        }
        return csvList;
    }

//    private byte[] writeCSV(List<Map<String, String>> processedCSV) throws JsonProcessingException {
//        CsvMapper mapper = new CsvMapper();
//        CsvSchema schema = CsvSchema.builder()
//                .addColumns(processedCSV.get(0).keySet(), CsvSchema.ColumnType.STRING)
//                .build()
//                .withHeader();
//        String csvString = mapper.writer(schema).writeValueAsString(processedCSV);
//        return csvString.getBytes();
//    }

    public static void writeCSV(String filePath, List<Map<String, String>> data) throws IOException {
        CsvFactory csvFactory = new CsvFactory();
        try (CsvGenerator csvGenerator = csvFactory.createGenerator(new FileWriter(filePath))) {
            // Write headers
            if (!data.isEmpty()) {
                writeHeaders(csvGenerator, data.get(0).keySet());
            }

            // Write data
            for (Map<String, String> record : data) {
                writeRecord(csvGenerator, record);
            }
        }
    }

    private static void writeHeaders(JsonGenerator jsonGenerator, Iterable<String> headers) throws IOException {
        jsonGenerator.writeStartArray();
        for (String header : headers) {
            jsonGenerator.writeString(header);
        }
        jsonGenerator.writeEndArray();
//        jsonGenerator.writeRaw('\n');
    }

    private static void writeRecord(JsonGenerator jsonGenerator, Map<String, String> record) throws IOException {
        jsonGenerator.writeStartArray();
        for (String value : record.values()) {
            jsonGenerator.writeString(value);
        }
        jsonGenerator.writeEndArray();
//        jsonGenerator.writeRaw('\n');
    }

    private String getDecryptedValue(String encryptedKey, String secretKey) {
        try {
            return AESGCMEncryptor.decrypt(encryptedKey, secretKey);
        } catch (Exception e) {
            LOGGER.error("Error while decrypting the key {}", e);
            return "";
        }
    }
}
