package com.stedi.lsportfolio.other;

import java.util.Locale;

public class StaticUtils {
    public static String getSupportedLanguage() {
        return Locale.getDefault().getLanguage().equals("pl") ? "pl" : "en";
    }
}
