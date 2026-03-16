package org.dattapeetham.transliteration;

public class PreReplacements {
    private static String chakrabindu = "\u0901";
    private static String kn_chakrabindu = "\u0cbc";

    public static String replace(String sourceString, String langPair) {
        if (langPair.contains("-Kannada")) {
            return sourceString.replace(chakrabindu, kn_chakrabindu);
        }
        return sourceString;
    }
}
