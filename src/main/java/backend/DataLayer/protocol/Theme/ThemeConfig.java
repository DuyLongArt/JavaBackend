//package backend.DataLayer.protocol.Theme;
//
//import jakarta.persistence.*;
//import java.util.Objects;
//import java.util.regex.Pattern;
//
//@Entity
//@Table(name = "theme_configs")
//public class ThemeConfig implements ThemeStructure {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "theme_id")
//    private Long id;
//
//    @Column(name = "theme_name", nullable = false, unique = true, length = 100)
//    private String themeName;
//
//    @Column(name = "primary_color", nullable = false, length = 7)
//    private String primaryColor;
//
//    @Column(name = "secondary_color", nullable = false, length = 7)
//    private String secondaryColor;
//
//    @Column(name = "background_color", nullable = false, length = 7)
//    private String backgroundColor;
//
//    @Column(name = "accent_color", length = 7)
//    private String accentColor;
//
//    @Column(name = "text_color", length = 7)
//    private String textColor;
//
//    @Column(name = "border_color", length = 7)
//    private String borderColor;
//
//    @Column(name = "font_family", length = 100)
//    private String fontFamily;
//
//    @Column(name = "font_size")
//    private Integer fontSize;
//
//    @Column(name = "is_dark_theme")
//    private Boolean isDarkTheme;
//
//    @Column(name = "is_active")
//    private Boolean isActive;
//
//    // Regex pattern for validating hex colors
//    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#([0-9A-Fa-f]{6}|[0-9A-Fa-f]{3})$");
//
//    // Default constructor for JPA
//    public ThemeConfig() {
//        this.isActive = false;
//        this.isDarkTheme = false;
//    }
//
//    // Constructor with required fields
//    public ThemeConfig(String themeName, String primaryColor, String secondaryColor, String backgroundColor) {
//        this();
//        setThemeName(themeName);
//        setPrimaryColor(primaryColor);
//        setSecondaryColor(secondaryColor);
//        setBackgroundColor(backgroundColor);
//    }
//
//    // Full constructor
//    public ThemeConfig(String themeName, String primaryColor, String secondaryColor,
//                       String backgroundColor, String accentColor, String textColor,
//                       String borderColor, String fontFamily, Integer fontSize,
//                       Boolean isDarkTheme, Boolean isActive) {
//        this(themeName, primaryColor, secondaryColor, backgroundColor);
//        setAccentColor(accentColor);
//        setTextColor(textColor);
//        setBorderColor(borderColor);
//        setFontFamily(fontFamily);
//        setFontSize(fontSize);
//        setDarkTheme(isDarkTheme);
//        setActive(isActive);
//    }
//
//    // Getters
//    public Long getId() {
//        return id;
//    }
//
//    @Override
//    public String getThemeName() {
//        return themeName;
//    }
//
//    @Override
//    public String getPrimaryColor() {
//        return primaryColor;
//    }
//
//    @Override
//    public String getSecondaryColor() {
//        return secondaryColor;
//    }
//
//    @Override
//    public String getBackgroundColor() {
//        return backgroundColor;
//    }
//
//    public String getAccentColor() {
//        return accentColor;
//    }
//
//    public String getTextColor() {
//        return textColor;
//    }
//
//    public String getBorderColor() {
//        return borderColor;
//    }
//
//    public String getFontFamily() {
//        return fontFamily;
//    }
//
//    public Integer getFontSize() {
//        return fontSize;
//    }
//
//    public Boolean isDarkTheme() {
//        return isDarkTheme != null ? isDarkTheme : false;
//    }
//
//    public Boolean isActive() {
//        return isActive != null ? isActive : false;
//    }
//
//    // Setters with validation
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setThemeName(String themeName) {
//        if (themeName == null || themeName.trim().isEmpty()) {
//            throw new IllegalArgumentException("Theme name cannot be null or empty");
//        }
//        if (themeName.length() > 100) {
//            throw new IllegalArgumentException("Theme name cannot exceed 100 characters");
//        }
//        this.themeName = themeName.trim();
//    }
//
//    public void setPrimaryColor(String primaryColor) {
//        validateColor(primaryColor, "Primary color");
//        this.primaryColor = normalizeColor(primaryColor);
//    }
//
//    public void setSecondaryColor(String secondaryColor) {
//        validateColor(secondaryColor, "Secondary color");
//        this.secondaryColor = normalizeColor(secondaryColor);
//    }
//
//    public void setBackgroundColor(String backgroundColor) {
//        validateColor(backgroundColor, "Background color");
//        this.backgroundColor = normalizeColor(backgroundColor);
//    }
//
//    public void setAccentColor(String accentColor) {
//        if (accentColor != null) {
//            validateColor(accentColor, "Accent color");
//            this.accentColor = normalizeColor(accentColor);
//        } else {
//            this.accentColor = null;
//        }
//    }
//
//    public void setTextColor(String textColor) {
//        if (textColor != null) {
//            validateColor(textColor, "Text color");
//            this.textColor = normalizeColor(textColor);
//        } else {
//            this.textColor = null;
//        }
//    }
//
//    public void setBorderColor(String borderColor) {
//        if (borderColor != null) {
//            validateColor(borderColor, "Border color");
//            this.borderColor = normalizeColor(borderColor);
//        } else {
//            this.borderColor = null;
//        }
//    }
//
//    public void setFontFamily(String fontFamily) {
//        if (fontFamily != null && fontFamily.length() > 100) {
//            throw new IllegalArgumentException("Font family cannot exceed 100 characters");
//        }
//        this.fontFamily = fontFamily != null ? fontFamily.trim() : null;
//    }
//
//    public void setFontSize(Integer fontSize) {
//        if (fontSize != null && (fontSize < 8 || fontSize > 72)) {
//            throw new IllegalArgumentException("Font size must be between 8 and 72 pixels");
//        }
//        this.fontSize = fontSize;
//    }
//
//    public void setDarkTheme(Boolean darkTheme) {
//        this.isDarkTheme = darkTheme;
//    }
//
//    public void setActive(Boolean active) {
//        this.isActive = active;
//    }
//
//    // Utility methods
//    private void validateColor(String color, String colorName) {
//        if (color == null || color.trim().isEmpty()) {
//            throw new IllegalArgumentException(colorName + " cannot be null or empty");
//        }
//
//        String normalizedColor = normalizeColor(color);
//        if (!HEX_COLOR_PATTERN.matcher(normalizedColor).matches()) {
//            throw new IllegalArgumentException(colorName + " must be a valid hex color (e.g., #FF5733 or #F53)");
//        }
//    }
//
//    private String normalizeColor(String color) {
//        if (color == null) return null;
//
//        String trimmed = color.trim().toUpperCase();
//        if (!trimmed.startsWith("#")) {
//            trimmed = "#" + trimmed;
//        }
//
//        // Convert 3-digit hex to 6-digit
//        if (trimmed.length() == 4) {
//            StringBuilder expanded = new StringBuilder("#");
//            for (int i = 1; i < trimmed.length(); i++) {
//                char c = trimmed.charAt(i);
//                expanded.append(c).append(c);
//            }
//            trimmed = expanded.toString();
//        }
//
//        return trimmed;
//    }
//
//    /**
//     * Check if theme has all required colors
//     */
//    public boolean isComplete() {
//        return themeName != null && !themeName.trim().isEmpty() &&
//                primaryColor != null && secondaryColor != null && backgroundColor != null;
//    }
//
//    /**
//     * Apply default colors based on theme type
//     */
//    public void applyDefaults() {
//        if (isDarkTheme()) {
//            if (textColor == null) textColor = "#FFFFFF";
//            if (borderColor == null) borderColor = "#444444";
//        } else {
//            if (textColor == null) textColor = "#000000";
//            if (borderColor == null) borderColor = "#CCCCCC";
//        }
//
//        if (fontFamily == null) fontFamily = "Arial, sans-serif";
//        if (fontSize == null) fontSize = 14;
//    }
//
//    /**
//     * Create a copy of the theme with a new name
//     */
//    public ThemeConfig copy(String newName) {
//        return new ThemeConfig(newName, primaryColor, secondaryColor, backgroundColor,
//                accentColor, textColor, borderColor, fontFamily, fontSize,
//                isDarkTheme, false); // New copy is inactive by default
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//
//        ThemeConfig that = (ThemeConfig) obj;
//        return Objects.equals(id, that.id) && Objects.equals(themeName, that.themeName);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, themeName);
//    }
//
//    @Override
//    public String toString() {
//        return "ThemeConfig{" +
//                "id=" + id +
//                ", themeName='" + themeName + '\'' +
//                ", primaryColor='" + primaryColor + '\'' +
//                ", secondaryColor='" + secondaryColor + '\'' +
//                ", backgroundColor='" + backgroundColor + '\'' +
//                ", isDarkTheme=" + isDarkTheme +
//                ", isActive=" + isActive +
//                '}';
//    }
//}