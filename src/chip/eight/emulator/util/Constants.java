package chip.eight.emulator.util;

public class Constants {
    public static final int MEMORY_SIZE = 0x1000;
    public static final int STACK_SIZE = 16;
    public static final int REGISTER_SIZE = 16;
    public static final int KEY_SIZE = 16;

    public static final int BYTE_MASK = 0xff;

    public static final int NO_KEY = -1;

    public static final int OK_CLICKED = 1;
    public static final int CANCEL_CLICKED = 0;

    // Prevent initialization
    private Constants() {}

    public static boolean isStringEmpty(String text) {
        return text == null || text.isEmpty();
    }
}
