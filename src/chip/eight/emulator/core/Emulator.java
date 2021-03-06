package chip.eight.emulator.core;

import chip.eight.emulator.gui.Screen;
import chip.eight.emulator.util.Constants;

import java.io.*;

public class Emulator {
    private enum EmulatorState { RUNNING, PAUSED, STOPPED }

    private CPU cpu;
    private Memory memory;
    private Stack stack;
    private Screen screen;
    private long cpuInterval;
    private int refreshCycle;
    private boolean isRunning;
    private EmulatorState state;

    public Emulator(Chip8Mode mode, Screen screen, KeypadListener keypadListener, int cpuFrequency) {
        this.memory = new Memory(Constants.MEMORY_SIZE);
        this.stack = new Stack(Constants.STACK_SIZE);
        this.screen = screen;
        changeCpuFrequency(cpuFrequency);
        this.cpu = new CPU(memory, stack, screen, keypadListener, mode);
        this.isRunning = false;
        this.state = EmulatorState.STOPPED;
    }

    public void loadRom(File romFile) {
        try(DataInputStream dataInputStream = new DataInputStream(new FileInputStream(romFile))) {
            int offset = 0;
            while(dataInputStream.available() > 0) {
                memory.write(cpu.getPC() + offset++, dataInputStream.readUnsignedByte());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        state = EmulatorState.PAUSED;
    }

    public void unpause() {
        state = EmulatorState.RUNNING;
    }

    public void changeCpuFrequency(int cpuFrequency) {
        this.cpuInterval = 1_000_000_000 / cpuFrequency; // in nanoseconds, each cycle corresponds to a full interval
        this.refreshCycle = cpuFrequency / 60;
    }

    public void start() {
        isRunning = true;
        state = EmulatorState.RUNNING;
        run();
    }

    public void stop() {
        isRunning = false;
        state = EmulatorState.STOPPED;
    }

    private void run() {
        long startTime, endTime;
        int cycleCounter = 0;

        while(isRunning) {
            while(state == EmulatorState.PAUSED) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            startTime = System.nanoTime();

            cpu.execute();

            if(cycleCounter  % refreshCycle == 0) {
                cycleCounter = 0;

                if(cpu.shouldRedraw()) {
                    screen.repaint();
                    cpu.resetRedrawFlag();
                }

                cpu.decrementTimers();
            }

            endTime = System.nanoTime();
            cycleCounter++;

            finishCycle(startTime, endTime);
        }

        // if the emulator stops running, clear the screen and repaint
        screen.clearScreen();
        screen.repaint();
    }

    private void finishCycle(long startTime, long endTime) {
        long remainingTime = cpuInterval - (endTime - startTime);
        long waitTillTime = System.nanoTime() + remainingTime;

        while(System.nanoTime() < waitTillTime) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
