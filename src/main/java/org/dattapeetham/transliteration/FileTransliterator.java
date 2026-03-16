package org.dattapeetham.transliteration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class FileTransliterator {
    private static Charset charset = Charset.forName("UTF-16");
    private static CharsetDecoder decoder = charset.newDecoder();
    private static CharsetEncoder encoder = charset.newEncoder();

    public static void transliterateFile(String inputfile, String targetLanguage) {
        transliterateFile(inputfile, inputfile + ".samskrutam.txt", targetLanguage);
    }

    public static void transliterateFile(String inputFileName, String outputFileName, String targetLanguage) {
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);
        transliterateFile(targetLanguage, inputFile, outputFile);
    }

    public static void transliterateFile(String targetLanguage, File inputFile, File outputFile) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(inputFile);
            FileChannel fc = fis.getChannel();
            int sz = (int) fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0L, sz);
            CharBuffer cb = decoder.decode(bb);
            String inputText = cb.toString();
            CharBuffer cbOut = CharBuffer.wrap(ICUHelper.transliterate(inputText, targetLanguage));
            ByteBuffer bbOut = encoder.encode(cbOut);
            fos = new FileOutputStream(outputFile);
            FileChannel fcOut = fos.getChannel();
            fcOut.write(bbOut);
            fos.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeFile(fis);
        }
    }

    private static void closeFile(FileInputStream fis) {
        if (fis == null) {
            return;
        }
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        transliterateFile("sandhyavandanamtel.txt", "sandhyasamskrutam.txt", "Devanagari");
    }
}
