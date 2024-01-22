package com.example.festisounds.Core.Utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GenrePositionMapGenerator {


    public HashMap<String, short[]> makeGenrePositionMap(String fileName) {
        HashMap<String, short[]> genrePositionMap = new HashMap<>();

            MappingIterator<String[]> iterator = getMappingIterator(fileName);
            iterator.next();
            while (iterator.hasNext()) {
                String[] row = iterator.next();
                short[] genreColour = convertHexColourToRGB(row[3]);
                short[] position = getPosition(row, genreColour);
                genrePositionMap.put(row[0], position);
            }

        return genrePositionMap;
    }


    private static MappingIterator<String[]> getMappingIterator(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            File csvFile = resource.getFile();
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
            return mapper.readerFor(String[].class).readValues(csvFile);
        } catch (IOException e) {
            System.out.println("ffs");
        }
        return null;

    }


    short[] getPosition(String[] row, short[] genreColour) {
        short xPosition = Short.parseShort(row[1]);
        short yPosition = Short.parseShort(row[2]);
        return new short[] {xPosition, yPosition, genreColour[0], genreColour[1], genreColour[2]};
    }

    short[] convertHexColourToRGB(String hexColour) {

        short amountRed = Short.valueOf(hexColour.substring(1, 3), 16);
        short amountGreen = Short.valueOf(hexColour.substring(3, 5), 16);
        short amountBlue = Short.valueOf(hexColour.substring(5, 7), 16);

        return new short[]{amountRed, amountGreen, amountBlue};
    }
}
