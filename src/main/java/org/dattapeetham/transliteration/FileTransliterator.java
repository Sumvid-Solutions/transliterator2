package org.dattapeetham.transliteration;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileTransliterator {

    public static void transliterateFile(String inputfile, String targetLanguage) {
        transliterateFile(inputfile, inputfile + ".samskrutam.txt", targetLanguage);
    }

    public static void transliterateFile(String inputFileName, String outputFileName, String targetLanguage) {
        var inputFile = new File(inputFileName);
        var outputFile = new File(outputFileName);
        transliterateFile(targetLanguage, inputFile, outputFile);
    }

    public static void transliterateFile(String targetLanguage, File inputFile, File outputFile) {
        try {
            var inputText = Files.readString(inputFile.toPath(), StandardCharsets.UTF_16);
            var outputText = ICUHelper.transliterate(inputText, targetLanguage);
            Files.writeString(outputFile.toPath(), outputText, StandardCharsets.UTF_16);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void main(String[] args) {
        transliterateFile("sandhyavandanamtel.txt", "sandhyasamskrutam.txt", "Devanagari");
    }
}
