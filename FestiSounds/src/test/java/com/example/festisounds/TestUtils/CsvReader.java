package com.example.festisounds.TestUtils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CsvReader {
    public static HashMap<String, Double> readMapCsv(String fileName) {
        HashMap<String, Double> builtMap = new HashMap<>();
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            File csvFile = resource.getFile();
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
            MappingIterator<String[]> iterator = mapper.readerFor(String[].class).readValues(csvFile);
            while (iterator.hasNext()) {
                String[] row = iterator.next();
                builtMap.put(row[0], Double.parseDouble(row[1]));
            }
        } catch (IOException e) {
            System.out.println("ffs");
        }
        return builtMap;
    }

    public static ArrayList<Set<String>> readSetCsv(String fileName) {
        ArrayList<Set<String>> builtSetList = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            File csvFile = resource.getFile();
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
            MappingIterator<String[]> iterator = mapper.readerFor(String[].class).readValues(csvFile);
            while (iterator.hasNext()) {
                String[] row = iterator.next();
                Set<String> nextArtistSet = new HashSet<String>(Arrays.asList(row));
                builtSetList.add(nextArtistSet);
            }
        } catch (IOException e) {
            System.out.println("ffs");
        }
        return builtSetList;
    }
}
