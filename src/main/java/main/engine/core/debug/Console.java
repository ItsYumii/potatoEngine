package main.java.main.engine.core.debug;

public final class Console {

    private Console() {}

    public static final String
            RESET = "\u001B[0m",

    BLACK          = "\u001B[30m",
            RED            = "\u001B[31m",
            GREEN          = "\u001B[32m",
            YELLOW         = "\u001B[33m",
            BLUE           = "\u001B[34m",
            MAGENTA        = "\u001B[35m",
            CYAN           = "\u001B[36m",
            WHITE          = "\u001B[37m",
            BRIGHT_BLACK   = "\u001B[90m",
            BRIGHT_RED     = "\u001B[91m",
            BRIGHT_GREEN   = "\u001B[92m",
            BRIGHT_YELLOW  = "\u001B[93m",
            BRIGHT_BLUE    = "\u001B[94m",
            BRIGHT_MAGENTA = "\u001B[95m",
            BRIGHT_CYAN    = "\u001B[96m",
            BRIGHT_WHITE   = "\u001B[97m",

    BG_BLACK          = "\u001B[40m",
            BG_RED            = "\u001B[41m",
            BG_GREEN          = "\u001B[42m",
            BG_YELLOW         = "\u001B[43m",
            BG_BLUE           = "\u001B[44m",
            BG_MAGENTA        = "\u001B[45m",
            BG_CYAN           = "\u001B[46m",
            BG_WHITE          = "\u001B[47m",
            BG_BRIGHT_BLACK   = "\u001B[100m",
            BG_BRIGHT_RED     = "\u001B[101m",
            BG_BRIGHT_GREEN   = "\u001B[102m",
            BG_BRIGHT_YELLOW  = "\u001B[103m",
            BG_BRIGHT_BLUE    = "\u001B[104m",
            BG_BRIGHT_MAGENTA = "\u001B[105m",
            BG_BRIGHT_CYAN    = "\u001B[106m",
            BG_BRIGHT_WHITE   = "\u001B[107m",

    BOLD       = "\u001B[1m",
            DIM        = "\u001B[2m",
            ITALIC     = "\u001B[3m",
            UNDERLINE  = "\u001B[4m",
            BLINK      = "\u001B[5m",
            REVERSE    = "\u001B[7m",
            HIDDEN     = "\u001B[8m";

    public static void info(String msg) {
        System.out.println(BRIGHT_GREEN + "[INFO] \u001B[38;2;180;255;135m" + msg + RESET);
    }

    public static void info(String msg, Object... args) {
        System.out.printf(BRIGHT_GREEN + "[INFO] \u001B[38;2;180;255;135m" + msg + RESET + "\n", args);
    }

    public static void warn(String msg) {
        System.out.println(BRIGHT_YELLOW + "[WARN] \u001B[38;2;255;252;163m" + msg + RESET);
    }

    public static void warn(String msg, Object... args) {
        System.out.printf(BRIGHT_YELLOW + "[WARN] \u001B[38;2;255;252;163m" + msg + RESET + "\n", args);
    }

    public static void err(String msg) {
        System.out.println(RED + "[ERROR] \u001B[38;2;254;176;175m" + msg + RESET);
    }

    public static void err(String msg, Object... args) {
        System.out.printf(RED + "[ERROR] \u001B[38;2;254;176;175m" + msg + RESET + "\n", args);
    }

    public static void print(String msg, String color) {
        System.out.println(color + msg + RESET);
    }

    public static void print(String msg, String color, Object... args) {
        System.out.printf(color + msg + RESET + "\n", args);
    }
}
