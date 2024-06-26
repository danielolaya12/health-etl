package org.healthetl.filters;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@Log4j2
public class CsvReader extends Filter{
    public void run (){
        readCsv();
    }
    private void readCsv() {
        try (Reader reader = new FileReader("Independent_Medical_Reviews.csv");
             CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {
            for (CSVRecord csvRecord : csvParser) {
                JSONObject jsonObject = new JSONObject();
                for (String header : csvParser.getHeaderNames()) {
                    jsonObject.put(header, csvRecord.get(header));
                }
                output.write(jsonObject);
            }
            output.notifyThreads();
        } catch (IOException e) {
            // log.error(e.getMessage());
        }
    }
}
