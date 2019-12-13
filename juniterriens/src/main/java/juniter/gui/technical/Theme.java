package juniter.gui.technical;

public enum Theme {
    JMetroBase("/gui/css/JMetroBase.css"),
    BLANK_THEME("/gui/css/JMetroBase.css"),
    DARK_THEME("/gui/css/JMetroDarkTheme.css"),
    LIGHT_THEME("/gui/css/JMetroLightTheme.css");


    private final String THETHEME;

    Theme(String ept) {
        this.THETHEME = ept;
    }

    public String getTheme() {
        return this.THETHEME;
    }
}
