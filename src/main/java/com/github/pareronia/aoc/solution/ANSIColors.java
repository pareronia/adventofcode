package com.github.pareronia.aoc.solution;

public class ANSIColors {

    public static final String RESET = "\u001B[0m";
    
    // Text attributes
    public static final String BOLD = "\u001B[1m";

    // Regular text colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Bright text colors
    public static final String BRIGHT_BLACK = "\u001B[30;1m";
    public static final String BRIGHT_RED = "\u001B[31;1m";
    public static final String BRIGHT_GREEN = "\u001B[32;1m";
    public static final String BRIGHT_YELLOW = "\u001B[33;1m";
    public static final String BRIGHT_BLUE = "\u001B[34;1m";
    public static final String BRIGHT_MAGENTA = "\u001B[35;1m";
    public static final String BRIGHT_CYAN = "\u001B[36;1m";
    public static final String BRIGHT_WHITE = "\u001B[37;1m";

    // Background colors
    public static final String BACKGROUND_BLACK = "\u001B[40m";
    public static final String BACKGROUND_RED = "\u001B[41m";
    public static final String BACKGROUND_GREEN = "\u001B[42m";
    public static final String BACKGROUND_YELLOW = "\u001B[43m";
    public static final String BACKGROUND_BLUE = "\u001B[44m";
    public static final String BACKGROUND_MAGENTA = "\u001B[45m";
    public static final String BACKGROUND_CYAN = "\u001B[46m";
    public static final String BACKGROUND_WHITE = "\u001B[47m";

    // Bright background colors
    public static final String BRIGHT_BACKGROUND_BLACK = "\u001B[40;1m";
    public static final String BRIGHT_BACKGROUND_RED = "\u001B[41;1m";
    public static final String BRIGHT_BACKGROUND_GREEN = "\u001B[42;1m";
    public static final String BRIGHT_BACKGROUND_YELLOW = "\u001B[43;1m";
    public static final String BRIGHT_BACKGROUND_BLUE = "\u001B[44;1m";
    public static final String BRIGHT_BACKGROUND_MAGENTA = "\u001B[45;1m";
    public static final String BRIGHT_BACKGROUND_CYAN = "\u001B[46;1m";
    public static final String BRIGHT_BACKGROUND_WHITE = "\u001B[47;1m";
    
    public static String bold(final String text) {
        return BOLD + text + RESET;
    }
    
    public static String yellow(final String text) {
        return YELLOW + text + RESET;
    }
    
    public static String red(final String text) {
        return RED + text + RESET;
    }
}
