package com.example.mealplanner.utils;

public class FlagUtils {

    public static String getFlagCode(String area) {
        if (area == null) return "unknown";

        switch (area.trim()) {
            case "Algerian": return "dz";
            case "American": return "us";
            case "Argentinian": return "ar";
            case "Australian": return "au";
            case "British": return "gb";
            case "Canadian": return "ca";
            case "Chinese": return "cn";
            case "Croatian": return "hr";
            case "Dutch": return "nl";
            case "Egyptian": return "eg";
            case "Filipino": return "ph";
            case "French": return "fr";
            case "Greek": return "gr";
            case "Indian": return "in";
            case "Irish": return "ie";
            case "Italian": return "it";
            case "Jamaican": return "jm";
            case "Japanese": return "jp";
            case "Kenyan": return "ke";
            case "Malaysian": return "my";
            case "Mexican": return "mx";
            case "Moroccan": return "ma";
            case "Norwegian": return "no";
            case "Polish": return "pl";
            case "Portuguese": return "pt";
            case "Russian": return "ru";
            case "Saudi Arabian": return "sa";
            case "Slovakian": return "sk";
            case "Spanish": return "es";
            case "Syrian": return "sy";
            case "Thai": return "th";
            case "Tunisian": return "tn";
            case "Turkish": return "tr";
            case "Ukrainian": return "ua";
            case "Uruguayan": return "uy";
            case "Venezulan": return "ve";
            case "Vietnamese": return "vn";
            default: return "unknown";
        }
    }
    public static String getFlagUrl(String area) {
        return "https://www.themealdb.com/images/icons/flags/big/64/" + getFlagCode(area) + ".png";
    }
}