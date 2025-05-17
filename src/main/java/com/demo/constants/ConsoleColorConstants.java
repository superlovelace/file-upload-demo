package com.demo.constants;

/**
 * 控制台颜色常量
 * @author peter
 */
public class ConsoleColorConstants {

    private ConsoleColorConstants() {
        throw new IllegalStateException("Constants class");
    }

    // 颜色重置
    public static final String RESET = "\033[0m";

    // 字体颜色
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";

    // 字体背景色
    public static final String BLACK_BACKGROUND = "\033[0;40m";
    public static final String RED_BACKGROUND = "\033[0;41m";
    public static final String GREEN_BACKGROUND = "\033[0;42m";
    public static final String YELLOW_BACKGROUND = "\033[0;43m";
    public static final String BLUE_BACKGROUND = "\033[0;44m";
    public static final String PURPLE_BACKGROUND = "\033[0;45m";
    public static final String CYAN_BACKGROUND = "\033[0;46m";
    public static final String WHITE_BACKGROUND = "\033[0;47m";

    // 粗体

    // 字体颜色+粗体
    public static final String BLACK_BOLD = "\033[1;30m";
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String PURPLE_BOLD = "\033[1;35m";
    public static final String CYAN_BOLD = "\033[1;36m";
    public static final String WHITE_BOLD = "\033[1;37m";

    // 背景色+粗体
    public static final String BLACK_BACKGROUND_BOLD = "\033[1;40m";
    public static final String RED_BACKGROUND_BOLD = "\033[1;41m";
    public static final String GREEN_BACKGROUND_BOLD = "\033[1;42m";
    public static final String YELLOW_BACKGROUND_BOLD = "\033[1;43m";
    public static final String BLUE_BACKGROUND_BOLD = "\033[1;44m";
    public static final String PURPLE_BACKGROUND_BOLD = "\033[1;45m";
    public static final String CYAN_BACKGROUND_BOLD = "\033[1;46m";
    public static final String WHITE_BACKGROUND_BOLD = "\033[1;47m";

    // etc...
}