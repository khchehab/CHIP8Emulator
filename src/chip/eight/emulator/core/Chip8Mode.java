package chip.eight.emulator.core;

public enum Chip8Mode {
    STANDARD(64, 32, 500);

    private int width;
    private int height;
    private int cpuFrequency;

    Chip8Mode(int width, int height, int cpuFrequency) {
        this.width = width;
        this.height = height;
        this.cpuFrequency = cpuFrequency;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCpuFrequency() {
        return cpuFrequency;
    }
}
