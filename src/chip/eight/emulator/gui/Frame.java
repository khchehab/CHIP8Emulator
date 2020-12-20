package chip.eight.emulator.gui;

import chip.eight.emulator.core.Chip8Mode;
import chip.eight.emulator.core.Emulator;
import chip.eight.emulator.core.KeypadListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class Frame {
    private static final int PADDING = 5;
    private static final JPanel TOP_PADDING, BOTTOM_PADDING, LEFT_PADDING, RIGHT_PADDING;
    private static final JFileChooser ROM_FILE_CHOOSER;

    static {
        Dimension verticalDimension = new Dimension(0, PADDING),
                horizontalDimension = new Dimension(PADDING, 0);

        TOP_PADDING = new JPanel();
        TOP_PADDING.setPreferredSize(verticalDimension);
        TOP_PADDING.setBackground(Color.BLACK);

        BOTTOM_PADDING = new JPanel();
        BOTTOM_PADDING.setPreferredSize(verticalDimension);
        BOTTOM_PADDING.setBackground(Color.BLACK);

        LEFT_PADDING = new JPanel();
        LEFT_PADDING.setPreferredSize(horizontalDimension);
        LEFT_PADDING.setBackground(Color.BLACK);

        RIGHT_PADDING = new JPanel();
        RIGHT_PADDING.setPreferredSize(horizontalDimension);
        RIGHT_PADDING.setBackground(Color.BLACK);

        ROM_FILE_CHOOSER = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        ROM_FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ROM_FILE_CHOOSER.setMultiSelectionEnabled(false);
        ROM_FILE_CHOOSER.addChoosableFileFilter(new FileNameExtensionFilter("Chip8 Roms (*.ch8 or *.c8)", "ch8", "c8"));
    }

    private static Frame instance = null;

    private Chip8Mode mode;
    private Emulator emulator;
    private JFrame frame;
    private Screen screen;
    private KeypadListener keypadListener;
    private int scale;
    private File currentRomFile;

    private Frame() {
        mode = Chip8Mode.STANDARD;
        emulator = null;
        currentRomFile = null;
        scale = 10;
        keypadListener = new KeypadListener();
        buildFrame();
    }

    public static Frame getInstance() {
        if(instance == null) {
            instance = new Frame();
        }

        return instance;
    }

    public void show() {
        frame.pack();
        frame.setVisible(true);
    }

    private void buildFrame() {
        Dimension frameDimension = new Dimension(getFrameWidth(), getFrameHeight());

        frame = new JFrame("Chip8 Emulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(frameDimension);
        frame.setLayout(new BorderLayout());

        // add paddings around the screen
        frame.add(TOP_PADDING, BorderLayout.PAGE_START);
        frame.add(BOTTOM_PADDING, BorderLayout.PAGE_END);
        frame.add(LEFT_PADDING, BorderLayout.LINE_START);
        frame.add(RIGHT_PADDING, BorderLayout.LINE_END);

        buildMenu();
        buildScreen();
    }

    private void buildScreen() {
        // add one to the width and height since when drawing the pixels the bottom-right edge will not be displayed if
        // if the dimensions are exact
        Dimension screenDimension = new Dimension(getScreenWidth() + 1, getScreenHeight() + 1);

        screen = new Screen(mode.getWidth(), mode.getHeight(), scale);
        screen.setPreferredSize(screenDimension);
        screen.setLayout(null);
        screen.addKeyListener(keypadListener);
        screen.setFocusable(true);
        screen.requestFocus();

        frame.add(screen, BorderLayout.CENTER);
    }

    private void buildMenu() {
        // Building the file menu
        JMenuItem loadRomMenuItem = new JMenuItem("Load");
        loadRomMenuItem.addActionListener(event -> showRomFileChooser());

        JMenuItem resetMenuItem = new JMenuItem("Reset");
        resetMenuItem.addActionListener(event -> startEmulator());

        JMenuItem stopMenuItem = new JMenuItem("Stop");
        stopMenuItem.addActionListener(event -> {
            stopEmulator();
            currentRomFile = null;
        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(event -> System.exit(0));

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(loadRomMenuItem);
        fileMenu.add(resetMenuItem);
        fileMenu.add(stopMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        // Building the options menu
        JMenuItem settingsMenuItem = new JMenuItem("Settings");
        settingsMenuItem.addActionListener(event -> {
            // todo action listener for settings
            // options to have:
            // 1. color of set and unset pixels
            // 2. change cpu frequency
            // 3. change the scale
            // 4. keypad mapping
        });

        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.add(settingsMenuItem);

        // Building the menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);

        frame.setJMenuBar(menuBar);
    }

    private void startEmulator() {
        if(currentRomFile == null) {
            return;
        }

        stopEmulator();

        new Thread(() -> {
            emulator = new Emulator(mode, screen, keypadListener);
            emulator.loadRom(currentRomFile);
            emulator.start();
        }).start();
    }

    private void stopEmulator() {
        if(emulator != null) {
            emulator.stop();
            emulator = null;
        }
    }

    private void showRomFileChooser() {
        int ret = ROM_FILE_CHOOSER.showOpenDialog(null);

        if(ret == JFileChooser.APPROVE_OPTION) {
            currentRomFile = ROM_FILE_CHOOSER.getSelectedFile();
            System.out.println("Selected file: " + currentRomFile.getAbsolutePath());
            startEmulator();
        }
    }

    private int getFrameWidth() {
        return getScreenWidth() + (2 * PADDING);
    }

    private int getFrameHeight() {
        return getScreenHeight() + (2 * PADDING);
    }

    private int getScreenWidth() {
        return mode.getWidth() * scale;
    }

    private int getScreenHeight() {
        return mode.getHeight() * scale;
    }
}
