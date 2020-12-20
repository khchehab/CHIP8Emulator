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
    }

    private static Frame instance = null;

    private Chip8Mode mode;
    private Emulator emulator;
    private JFrame frame;
    private Screen screen;
    private KeypadListener keypadListener;
    private int scale;

    private Frame() {
        mode = Chip8Mode.STANDARD;
        emulator = null;
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
        JMenuItem loadRomMenuItem = new JMenuItem("Load");
        loadRomMenuItem.addActionListener(event -> {
            // todo maybe move this file chooser to a static global variable to avoid initializing each time this action is clicked
            JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Chip8 Roms", "ch8"));
            int ret = fileChooser.showOpenDialog(null);

            if(ret == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                startEmulator(selectedFile); // todo this is not working, see what should be done
            }
        });

        JMenuItem resetMenuItem = new JMenuItem("Reset");
        resetMenuItem.addActionListener(event -> {
            // todo action listener for resetting emulator
        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(event -> System.exit(0));

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(loadRomMenuItem);
        fileMenu.add(resetMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);
    }

    private void startEmulator(File romFile) {
        emulator = new Emulator(mode, screen, keypadListener);
        emulator.loadRom(romFile);
        emulator.start();
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
