package org.ohana.emailclient.view;

public enum FontSize {
    SMALL,
    MEDIUM,
    BIG;

    public static String getCSSPath(FontSize fontSize){
        return switch (fontSize) {
            case SMALL -> "css/fontSmall.css";
            case MEDIUM -> "css/fontMedium.css";
            case BIG -> "css/fontBig.css";
            default -> null;
        };
    }
}
