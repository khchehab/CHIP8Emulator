package chip.eight.emulator.core;

import chip.eight.emulator.util.Constants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeypadListener extends KeyAdapter {
    private static final Map<Integer, Integer> DEFAULT_MAPPING = new HashMap<Integer, Integer>() {{
        put(0x0, KeyEvent.VK_X);
        put(0x1, KeyEvent.VK_1);
        put(0x2, KeyEvent.VK_2);
        put(0x3, KeyEvent.VK_3);
        put(0x4, KeyEvent.VK_Q);
        put(0x5, KeyEvent.VK_W);
        put(0x6, KeyEvent.VK_E);
        put(0x7, KeyEvent.VK_A);
        put(0x8, KeyEvent.VK_S);
        put(0x9, KeyEvent.VK_D);
        put(0xA, KeyEvent.VK_Z);
        put(0xB, KeyEvent.VK_C);
        put(0xC, KeyEvent.VK_4);
        put(0xD, KeyEvent.VK_R);
        put(0xE, KeyEvent.VK_F);
        put(0xF, KeyEvent.VK_V);
    }};

    private Map<Integer, Integer> keypadMapping;
    private int pressed;

    public KeypadListener() {
        keypadMapping = DEFAULT_MAPPING;
        pressed = Constants.NO_KEY;
    }

    public int getKeyPressed() {
        return pressed;
    }

    public void setKeypadMapping(Map<Integer, Integer> keypadMapping) {
        this.keypadMapping = keypadMapping;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressed = getKey(e.getKeyCode());

        if(this.pressed != Constants.NO_KEY) {
            System.out.println("Key Pressed: " + e.getKeyChar() + " which corresponds to the Chip8 Key " + Integer.toHexString(pressed));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed = Constants.NO_KEY;
    }

    private int getKey(int keyCode) {
        for(Map.Entry<Integer, Integer> keyMap : keypadMapping.entrySet()) {
            if(keyMap.getValue() == keyCode) {
                return keyMap.getKey();
            }
        }

        return Constants.NO_KEY;
    }
}
