package chip.eight.emulator.core;

import chip.eight.emulator.gui.Screen;
import chip.eight.emulator.util.Constants;

import java.util.Random;

public class CPU {
    private int[] V;
    private int I;
    private int delayTimer;
    private int soundTimer;
    private int PC;

    private Chip8Mode mode;
    private Memory memory;
    private Stack stack;
    private KeypadListener keypadListener;
    private Screen screen;
    private boolean shouldRedraw;

    private Random random;

    public CPU(Memory memory, Stack stack, Screen screen, KeypadListener keypadListener, Chip8Mode mode) {
        V = new int[Constants.REGISTER_SIZE];
        I = 0;
        delayTimer = 0;
        soundTimer = 0;
        PC = 0x200;

        this.mode = mode;
        this.memory = memory;
        this.stack = stack;
        this.keypadListener = keypadListener;
        this.screen = screen;
        this.shouldRedraw = false;

        random = new Random(System.currentTimeMillis());
    }

    public int getPC() {
        return PC;
    }

    public int getDelayTimer() {
        return delayTimer;
    }

    public int getSoundTimer() {
        return soundTimer;
    }

    public int getI() {
        return I;
    }

    public int getV(int registerIndex) {
        return V[registerIndex];
    }

    public int[] getV() {
        return V;
    }

    public int getCurrentOpcode() {
        return (memory.read(PC) << 8) | memory.read(PC + 1);
    }

    public void execute() {
        int opcode = getCurrentOpcode();

        int opcodeFamily = (opcode & 0xf000) >> 12;
        int nnn = opcode & 0x0fff;
        int kk = opcode & 0x00ff;
        int x = (opcode & 0x0f00) >> 8;
        int y = (opcode & 0x00f0) >> 4;
        int nibble = opcode & 0x000f;
        boolean incrementProgramCounter = true;
        int keyPressed;

        switch(opcodeFamily) {
            case 0x0:
                switch(nnn) {
                    case 0x0e0: // clear screen
                        screen.clearScreen();
                        setRedrawFlag();
                        break;
                    case 0x0ee: // return from subroutine
                        PC = stack.pop();
                        break;
                    default: // call routine at address
                        // this instruction is ignored by modern interpreters
                        break;
                }
                break;
            case 0x1: // jump to address
                PC = nnn;
                incrementProgramCounter = false;
                break;
            case 0x2: // call address
                stack.push(PC);
                PC = nnn;
                incrementProgramCounter = false;
                break;
            case 0x3: // skip next instruction if Vx = kk
                if(V[x] == kk) {
                    PC += 2;
                }
                break;
            case 0x4: // skip next instruction if Vx != kk
                if(V[x] != kk) {
                    PC += 2;
                }
                break;
            case 0x5: // skip next instruction if Vx = Vy
                if(V[x] == V[y]) {
                    PC += 2;
                }
                break;
            case 0x6: // set the value kk in register Vx
                V[x] = kk;
                break;
            case 0x7: // add the value kk to the register Vx
                V[x] = (V[x] + kk) & 0xff;
                break;
            case 0x8:
                switch(nibble) {
                    case 0x0: // set the value of Vy in the register Vx
                        V[x] = V[y];
                        break;
                    case 0x1: // set the result of bitwise OR operation Vx and Vy in Vx
                        V[x] = (V[x] | V[y]) & 0xff;
                        break;
                    case 0x2: // set the result of bitwise AND operation Vx and Vy in Vx
                        V[x] = (V[x] & V[y]) & 0xff;
                        break;
                    case 0x3: // set the result of bitwise XOR operation Vx and Vy in Vx
                        V[x] = (V[x] ^ V[y]) & 0xff;
                        break;
                    case 0x4: // add the value of Vy to Vx, set VF to the carry
                        int result = V[x] + V[y];
                        V[0xf] = result > 0xff ? 0x01 : 0x00;
                        V[x] = result & 0xff;
                        break;
                    case 0x5: // subtract the value of Vy from Vx, set VF to the borrow
                        V[0xf] = V[x] > V[y] ? 0x01 : 0x00;
                        V[x] = (V[x] - V[y]) & 0xff;
                        break;
                    case 0x6: // shift the value of Vx to the right 1 bit
                        V[0xf] = (V[x] & 0x01) == 0x01 ? 0x01 : 0x00;
                        V[x] = V[x] >> 1;
                        break;
                    case 0x7: // subtract the value of Vx from Vy, set VF to the borrow
                        V[0xf] = V[y] > V[x] ? 0x01 : 0x00;
                        V[x] = (V[y] - V[x]) & 0xff;
                        break;
                    case 0xe: // shift the value of Vx to the left 1 bit
                        V[0xf] = ((V[x] & 0x80) >> 7) == 0x01 ? 0x01 : 0x00;
                        V[x] = V[x] << 1;
                        break;
                    default:
                        break;
                }
                break;
            case 0x9:
                switch(nibble) {
                    case 0x0: // skip next instruction if Vx != Vy
                        if(V[x] != V[y]) {
                            PC += 2;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 0xa: // set nnn in the register I
                I = nnn;
                break;
            case 0xb: // jump tp location nnn + V0
                PC = nnn + V[0x0];
                incrementProgramCounter = false;
                break;
            case 0xc: // set the result of bitwise AND of a random number (between 0 and 255) and kk
                int randomByte = random.nextInt(256);
                V[x] = (randomByte & kk) & 0xff;
                break;
            case 0xd: // draw n-byte sprite at memory location I at (Vx, Vy)
                int xPos, yPos, width = mode.getWidth(), height = mode.getHeight(), pos, sprite, pixel, screenPixel;
                V[0xf] = 0x00;
                for(int i = 0; i < nibble; i++) { // height
                    sprite = memory.read(I + i);
                    yPos = (V[y] + i) % height;

                    for(int j = 0; j < 8; j++) { // width
                        xPos = (V[x] + j) % width;
                        pixel = (sprite & (0x80 >> j)) >> (8 - j - 1);
                        pos = (yPos * width) + xPos;
                        screenPixel = screen.readPixel(pos);

                        if(pixel != 0x0) {
                            if(screenPixel != 0x0) {
                                V[0xf] = 0x1;
                            }

                            screen.writePixel(pos, screenPixel ^ 1);
                        }
                    }
                }
                setRedrawFlag();
                break;
            case 0xe:
                keyPressed = keypadListener.getKeyPressed();

                switch(kk) {
                    case 0x9e: // skip next instruction if the key pressed is the same as the value of Vx
                        if(keyPressed == V[x]) {
                            PC += 2;
                        }
                        break;
                    case 0xa1: // skip next instruction if the key pressed is not the same as the value of Vx
                        if(keyPressed != V[x]) {
                            PC += 2;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 0xf:
                switch(kk) {
                    case 0x07: // set the delay timer value in Vx
                        V[x] = delayTimer;
                        break;
                    case 0x0a: // store the value of key pressed in Vx
                        while((keyPressed = keypadListener.getKeyPressed()) == Constants.NO_KEY) {
                            try {
                                Thread.sleep(0);
                            } catch(InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        V[x] = keyPressed;

                        break;
                    case 0x15: // set the Vx value in the delay timer
                        delayTimer = V[x];
                        break;
                    case 0x18: // set the sound timer value in Vx
                        soundTimer = V[x];
                        break;
                    case 0x1e: // add Vx to the I register
                        I += V[x];
                        break;
                    case 0x29: // set the location of sprite for digit Vx in I
                        I = V[x] * 5;
                        break;
                    case 0x33: // store bcd representation of Vx in memory locations I, I + 1 and I + 2
                        int hundreds = V[x] / 100, tens = (V[x] / 10) % 10, ones = V[x] % 10;
                        memory.write(I, hundreds);
                        memory.write(I + 1, tens);
                        memory.write(I + 2, ones);
                        break;
                    case 0x55: // store registers V0 through Vx in memory starting at location I
                        for(int i = 0; i <= x; i++) {
                            memory.write(I + i, V[i]);
                        }
                        break;
                    case 0x65: // read registers V0 through Vx from memory starting at location I
                        for(int i = 0; i <= x; i++) {
                            V[i] = memory.read(I + i);
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        if(incrementProgramCounter) {
            PC += 2; // increment the program counter 2 bytes
        }
    }

    public void decrementTimers() {
        if(delayTimer > 0) {
            delayTimer--;
        }

        if(soundTimer > 0) {
            //logInfo("START SOUND");
            soundTimer--;
        }

        if(soundTimer == 0) {
            //logInfo("STOP SOUND");
        }
    }

    public boolean shouldRedraw() {
        return shouldRedraw;
    }

    public void resetRedrawFlag() {
        this.shouldRedraw = false;
    }

    public void setRedrawFlag() {
        this.shouldRedraw = true;
    }
}
