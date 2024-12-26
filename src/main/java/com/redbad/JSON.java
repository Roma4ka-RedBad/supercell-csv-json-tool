package com.redbad;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class JSON {
    public static List<String[]> toList(LinkedHashMap<String, LinkedHashMap<String, ArrayList<Object>>> hashMap) throws IOException {
        if (!hashMap.containsKey("__metadata"))
            throw new IOException("The file metadata was not found!");

        List<String[]> list = new ArrayList<>();
        list.add(hashMap.get("__metadata").keySet().toArray(new String[0]));

        List<String> types = new ArrayList<>();
        for (ArrayList<Object> value : hashMap.get("__metadata").values())
            types.add(value.getFirst().toString());
        list.add(types.toArray(new String[0]));

        for (String name : hashMap.keySet()) {
            if (name.equals("__metadata")) continue;

            LinkedHashMap<String, ArrayList<Object>> fields = hashMap.get(name);

            List<String[]> lines = new ArrayList<>();
            lines.add(new String[fields.size() + 1]);
            lines.getFirst()[0] = name;

            int fieldIndex = 1;
            for (ArrayList<Object> field : fields.values()) {
                for (int index = 0; index < field.size(); index++) {
                    while (lines.size() <= index) {
                        lines.add(new String[fields.size() + 1]);
                        lines.getLast()[0] = "";
                    }

                    if (Objects.isNull(field.get(index))) {
                        lines.get(index)[fieldIndex] = "";
                    } else {
                        lines.get(index)[fieldIndex] = types.get(fieldIndex).toLowerCase().contains("int") ?
                                String.valueOf(Math.round((double) field.get(index))) : field.get(index).toString();
                    }
                }
                fieldIndex++;
            }
            list.addAll(lines);
        }
        return list;
    }

    public static void toCSV(FileReader inputFile, FileWriter outputFile) throws IOException {
        Gson gson = new Gson();
        Type inputType = new TypeToken<LinkedHashMap<String, LinkedHashMap<String, Object>>>() {}.getType();
        LinkedHashMap<String, LinkedHashMap<String, Object>> hashMap = gson.fromJson(inputFile, inputType);

        LinkedHashMap<String, LinkedHashMap<String, ArrayList<Object>>> convertedMap = new LinkedHashMap<>();
        for (Map.Entry<String, LinkedHashMap<String, Object>> outerEntry : hashMap.entrySet()) {
            LinkedHashMap<String, ArrayList<Object>> convertedInnerMap = new LinkedHashMap<>();
            for (Map.Entry<String, Object> innerEntry : outerEntry.getValue().entrySet()) {
                Object value = innerEntry.getValue();

                ArrayList<Object> list = new ArrayList<>();
                if (value instanceof ArrayList) {
                    list.addAll((ArrayList<?>) value);
                } else {
                    list.add(value);
                }
                convertedInnerMap.put(innerEntry.getKey(), list);
            }
            convertedMap.put(outerEntry.getKey(), convertedInnerMap);
        }

        CSVWriter writer = new CSVWriter(outputFile);
        writer.writeAll(toList(convertedMap));
        writer.close();
    }

    public static void toCSV(String inputPath, String outputPath) throws IOException {
        toCSV(new FileReader(inputPath), new FileWriter(outputPath));
    }
}
