package org.ohana.emailclient.view;

public enum ColorThemes {
    LIGHT,
    DEFAULT,
    DARK;

    public static String getCSSPath(ColorThemes colorThemes){
        return switch (colorThemes) {
            case LIGHT -> "css/themeLight.css";
            case DARK -> "css/themeDark.css";
            case DEFAULT -> "css/themeDefault.css";
        };
    }
}
