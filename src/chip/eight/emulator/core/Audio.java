package chip.eight.emulator.core;

public class Audio {
    private Audio() {}

    // todo maybe see another audio to use, if there is something betters
    public static void play() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    public static void stop() {
    }
}
