package chip.eight.emulator.core;

public class Stack {
    private int[] data;
    private int size;
    private int counter;

    public Stack(int size) {
        this.data = new int[size];
        this.size = size;
        this.counter = 0;
    }

    public int pop() {
        if(counter == 0) {
            return 0;
        }

        return data[--counter];
    }

    public boolean push(int address) {
        if(counter == size) {
            return false;
        }

        data[counter++] = address;
        return true;
    }
}
