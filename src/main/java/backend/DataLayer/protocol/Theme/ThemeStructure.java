package backend.DataLayer.protocol.Theme;

/**
 * Core theme structure interface defining essential visual properties.
 * Implementations should ensure color values are valid hex codes.
 */
public interface ThemeStructure {

    /**
     * Gets the unique name identifier for this theme.
     * @return the theme name, never null
     */
    String getThemeName();

    /**
     * Gets the primary color used for main UI elements.
     * @return hex color code (e.g., "#FF5733")
     */
    String getPrimaryColor();

    /**
     * Gets the secondary color used for accent elements.
     * @return hex color code (e.g., "#33FF57")
     */
    String getSecondaryColor();

    /**
     * Gets the background color for the main interface.
     * @return hex color code (e.g., "#FFFFFF")
     */
    String getBackgroundColor();

    /**
     * Sets the theme name.
     * @param themeName unique identifier for the theme
     * @throws IllegalArgumentException if name is null or invalid
     */
    void setThemeName(String themeName);

    /**
     * Sets the primary color.
     * @param primaryColor hex color code
     * @throws IllegalArgumentException if color format is invalid
     */
    void setPrimaryColor(String primaryColor);

    /**
     * Sets the secondary color.
     * @param secondaryColor hex color code
     * @throws IllegalArgumentException if color format is invalid
     */
    void setSecondaryColor(String secondaryColor);

    /**
     * Sets the background color.
     * @param backgroundColor hex color code
     * @throws IllegalArgumentException if color format is invalid
     */
    void setBackgroundColor(String backgroundColor);

    /**
     * Validates if all required theme properties are properly set.
     * @return true if theme is complete and valid
     */
    default boolean isValid() {
        return getThemeName() != null && !getThemeName().trim().isEmpty() &&
                isValidHexColor(getPrimaryColor()) &&
                isValidHexColor(getSecondaryColor()) &&
                isValidHexColor(getBackgroundColor());
    }

    /**
     * Checks if a string is a valid hex color.
     * @param color the color string to validate
     * @return true if valid hex color format
     */
    default boolean isValidHexColor(String color) {
        if (color == null) return false;
        return color.matches("^#([0-9A-Fa-f]{6}|[0-9A-Fa-f]{3})$");
    }

    /**
     * Determines if this is a dark theme based on background color brightness.
     * @return true if background color is dark
     */
    default boolean isDarkTheme() {
        String bgColor = getBackgroundColor();
        if (!isValidHexColor(bgColor)) return false;

        // Convert hex to RGB and calculate brightness
        String hex = bgColor.substring(1);
        if (hex.length() == 3) {
            hex = "" + hex.charAt(0) + hex.charAt(0) +
                    hex.charAt(1) + hex.charAt(1) +
                    hex.charAt(2) + hex.charAt(2);
        }

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        // Calculate perceived brightness using standard formula
        double brightness = (r * 0.299 + g * 0.587 + b * 0.114) / 255;
        return brightness < 0.5;
    }

    /**
     * Creates a CSS color variable string for this theme.
     * @return CSS custom properties string
     */
    default String toCssVariables() {
        return String.format(
                ":root {\n" +
                        "  --primary-color: %s;\n" +
                        "  --secondary-color: %s;\n" +
                        "  --background-color: %s;\n" +
                        "  --theme-name: '%s';\n" +
                        "}",
                getPrimaryColor(),
                getSecondaryColor(),
                getBackgroundColor(),
                getThemeName()
        );
    }
}