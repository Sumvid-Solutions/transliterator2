package org.dattapeetham.transliteration;

import com.ibm.icu.text.Transliterator;
import java.util.ArrayList;
import java.util.Enumeration;

public class ICUHelper {
    public static final String BENGALI = "Bengali";
    public static final String MALAYALAM = "Malayalam";
    public static final String GUJARATI = "Gujarati";
    public static final String TAMIL = "Tamil";
    public static final String KANNADA = "Kannada";
    public static final String DEVANAGARI = "Devanagari";
    public static final String TELUGU = "Telugu";

    public static void main(String[] args) {
        try {
            String sourceString = "\u0c35\u0c3e\u0c02\u0c1b\u0c38\u0c3f \u0c2f\u0c26\u0c3f \u0c24\u0c24\u0c4d\u0c2a\u0c26 \u0c2e\u0c35\u0c3f\u0c28\u0c3e\u0c36\u0c02";
            System.out.println("Source in Telugu:" + sourceString);
            Enumeration<String> e = Transliterator.getAvailableTargets(TELUGU);
            while (e.hasMoreElements()) {
                transliterate(sourceString, e.nextElement());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String transliterate(String sourceString, String destLanguage) {
        String langPair = "Any-" + destLanguage;
        Transliterator trans = Transliterator.getInstance(langPair);
        String string = trans.transliterate(PreReplacements.replace(sourceString, langPair));
        System.out.println("Converted to " + destLanguage + ":" + string);
        return string;
    }

    public static ArrayList<String> getAvailableTargets(String sourceLanguage) {
        ArrayList<String> availableLanguages = new ArrayList<>();
        Enumeration<String> e = Transliterator.getAvailableTargets(sourceLanguage);
        while (e.hasMoreElements()) {
            availableLanguages.add(e.nextElement());
        }
        return availableLanguages;
    }

    public static ArrayList<String> getAvailableSources() {
        ArrayList<String> availableLanguages = new ArrayList<>();
        Enumeration<String> e = Transliterator.getAvailableSources();
        while (e.hasMoreElements()) {
            availableLanguages.add(e.nextElement());
        }
        return availableLanguages;
    }
}
