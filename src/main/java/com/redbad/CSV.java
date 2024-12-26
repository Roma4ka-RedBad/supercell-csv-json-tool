package com.redbad;

import com.google.gson.FormattingStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CSV {
    private static Object parseValue(String value, String type) {
        return switch (type.toLowerCase()) {
            case "int", "intarray" -> value.isEmpty() ? null : Integer.parseInt(value);
            case "boolean", "booleanarray" -> value.isEmpty() ? null : Boolean.parseBoolean(value);
            default -> value;
        };
    }

    @SuppressWarnings("unchecked")
    private static void clearArrays(LinkedHashMap<String, Object> currentRow) {
        if (currentRow != null) {
            for (String key : currentRow.keySet()) {
                if (currentRow.get(key) instanceof ArrayList) {
                    ArrayList<Object> array = (ArrayList<Object>) currentRow.get(key);
                    if (array.size() == 1) {
                        currentRow.replace(key, array.getFirst());
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static LinkedHashMap<String, LinkedHashMap<String, Object>> toHashMap(FileReader file) throws CsvValidationException, IOException {
        CSVReader reader = new CSVReader(file);

        LinkedHashMap<String, LinkedHashMap<String, Object>> hashMap = new LinkedHashMap<>();
        String[] headers = reader.readNext();
        String[] types = reader.readNext();

        String[] line;
        LinkedHashMap<String, Object> currentRow = null;
        while ((line = reader.readNext()) != null) {
            if (!line[0].isEmpty()) {
                clearArrays(currentRow);

                currentRow = new LinkedHashMap<>();
                for (int index = 1; index < headers.length; index++) {
                    String value = index < line.length ? line[index] : "";
                    ArrayList<Object> array = new ArrayList<>();
                    array.add(parseValue(value, types[index]));
                    currentRow.put(headers[index], array);
                }
                hashMap.put(line[0], currentRow);
            } else if (currentRow != null) {
                for (int index = 1; index < headers.length; index++) {
                    String value = index < line.length ? line[index] : "";
                    ArrayList<Object> array = (ArrayList<Object>) currentRow.get(headers[index]);
                    array.add(parseValue(value, types[index]));
                }
            }
        }
        clearArrays(currentRow);

        LinkedHashMap<String, Object> metadata = new LinkedHashMap<>();
        for (int index = 0; index < headers.length; index++) {
            metadata.put(headers[index], types[index]);
        }
        hashMap.put("__metadata", metadata);

        return hashMap;
    }

    public static String toJSON(LinkedHashMap<String, LinkedHashMap<String, Object>> hashMap, FormattingStyle formattingStyle) {
        Gson gson = new GsonBuilder().serializeNulls().setFormattingStyle(formattingStyle).create();
        return gson.toJson(hashMap);
    }

    public static String toJSON(FileReader file, FormattingStyle formattingStyle) throws CsvValidationException, IOException {
        return toJSON(toHashMap(file), formattingStyle);
    }

    public static String toJSON(String filePath, FormattingStyle formattingStyle) throws CsvValidationException, IOException {
        return toJSON(new FileReader(filePath), formattingStyle);
    }

    public static void toJSON(String inputPath, String outputPath, FormattingStyle formattingStyle) throws CsvValidationException, IOException {
        FileWriter fileWriter = new FileWriter(outputPath);
        fileWriter.write(toJSON(inputPath, formattingStyle));
        fileWriter.close();
    }

    public static void toJSON(FileReader inputFile, FileWriter outputFile, FormattingStyle formattingStyle) throws CsvValidationException, IOException {
        outputFile.write(toJSON(inputFile, formattingStyle));
    }
}
