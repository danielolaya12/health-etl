package org.healthetl.filters;

import org.healthetl.data.S3SchemaWriter;
import org.healthetl.utils.DataTypeInfererUtil;
import org.json.simple.JSONObject;

import java.io.IOException;


public class SchemaDefinitionFilter extends Filter{
    private final DataTypeInfererUtil dataTypeInferrer;
    private final S3SchemaWriter s3DataWriter;
    private final String readerName;

    public SchemaDefinitionFilter(DataTypeInfererUtil dataTypeInferrer, S3SchemaWriter s3DataWriter, String readerName) {
        this.dataTypeInferrer = dataTypeInferrer;
        this.s3DataWriter = s3DataWriter;
        this.readerName = readerName;
    }
    @Override
    public void run (){
        try {
            schemaLog();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    // Method to infer data types and write the result to an S3 bucket
    public void schemaLog() throws IOException, InterruptedException {
        JSONObject json;
        if ((json = input.read()) != null) {
            // create schema definition
            JSONObject schemaDefinition = inferSchemaDefinition(json);
            // output to s3
            writeSchemaToS3(schemaDefinition);
        }
    }

    private JSONObject inferSchemaDefinition(JSONObject jsonInput) {
        return dataTypeInferrer.inferDataTypes(jsonInput);
    }
    private void writeSchemaToS3(JSONObject schemaDefinition) {
        s3DataWriter.writeJsonToS3(schemaDefinition, readerName);
    }
}