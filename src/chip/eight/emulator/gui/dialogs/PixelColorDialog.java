package chip.eight.emulator.gui.dialogs;

import chip.eight.emulator.gui.filters.IntegerFilter;
import chip.eight.emulator.util.Constants;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PixelColorDialog {
    private static final int MAIN_PANEL_WIDTH = 300;
    private static final int SET_PIXEL_PANEL_HEIGHT = 50;
    private static final int UNSET_PIXEL_PANEL_HEIGHT = 50;
    private static final int BUTTON_PANEL_HEIGHT = 30;

    private JDialog dialog;
    private JPanel contentPane;
    private JPanel mainPanel;
    private JTextField setPixelRedField, setPixelGreenField, setPixelBlueField,
                        unsetPixelRedField, unsetPixelGreenField, unsetPixelBlueField;

    private final JFrame parent;
    private Color setColor;
    private Color unsetColor;
    private int state;

    public PixelColorDialog(JFrame parent, Color setColor, Color unsetColor) {
        this.parent = parent;
        this.setColor = setColor;
        this.unsetColor = unsetColor;
        this.state = -1;

        buildDialog();
    }

    public void show() {
        dialog.setVisible(true);
    }

    public Color getSetColor() {
        return setColor;
    }

    public Color getUnsetColor() {
        return unsetColor;
    }

    public boolean isOkClicked() {
        return state == Constants.OK_CLICKED;
    }

    private void buildDialog() {
        dialog = new JDialog(parent, "Pixel Color Chooser", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(parent);

        int dialogHeight = SET_PIXEL_PANEL_HEIGHT + UNSET_PIXEL_PANEL_HEIGHT + BUTTON_PANEL_HEIGHT;

        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH, dialogHeight));
        contentPane.registerKeyboardAction(event -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPane.registerKeyboardAction(event -> onOk(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        buildMainPanel();
        buildButtonPanel();

        dialog.setContentPane(contentPane);
        dialog.pack();
    }

    private void buildMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH, SET_PIXEL_PANEL_HEIGHT + UNSET_PIXEL_PANEL_HEIGHT));

        buildSetPixelPanel();
        buildUnsetPixelPanel();

        contentPane.add(mainPanel);
    }

    private void buildSetPixelPanel() {
        JPanel setPixelPanel = new JPanel();
        setPixelPanel.setLayout(new GridLayout(1, 6));
        setPixelPanel.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH, SET_PIXEL_PANEL_HEIGHT));
        setPixelPanel.setBorder(BorderFactory.createTitledBorder("Set Pixel"));

        setPixelRedField = new JTextField();
        ((PlainDocument) setPixelRedField.getDocument()).setDocumentFilter(new IntegerFilter(0, 255));
        setPixelRedField.setText(String.valueOf(setColor.getRed()));
        setPixelGreenField = new JTextField();
        ((PlainDocument) setPixelGreenField.getDocument()).setDocumentFilter(new IntegerFilter(0, 255));
        setPixelGreenField.setText(String.valueOf(setColor.getGreen()));
        setPixelBlueField = new JTextField();
        ((PlainDocument) setPixelBlueField.getDocument()).setDocumentFilter(new IntegerFilter(0, 255));
        setPixelBlueField.setText(String.valueOf(setColor.getBlue()));

        setPixelPanel.add(new JLabel("Red"));
        setPixelPanel.add(setPixelRedField);
        setPixelPanel.add(new JLabel("Green"));
        setPixelPanel.add(setPixelGreenField);
        setPixelPanel.add(new JLabel("Blue"));
        setPixelPanel.add(setPixelBlueField);

        mainPanel.add(setPixelPanel);
    }

    private void buildUnsetPixelPanel() {
        JPanel unsetPixelPanel = new JPanel();
        unsetPixelPanel.setLayout(new GridLayout(1, 6));
        unsetPixelPanel.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH, UNSET_PIXEL_PANEL_HEIGHT));
        unsetPixelPanel.setBorder(BorderFactory.createTitledBorder("Unset Pixel"));

        unsetPixelRedField = new JTextField();
        ((PlainDocument) unsetPixelRedField.getDocument()).setDocumentFilter(new IntegerFilter(0, 255));
        unsetPixelRedField.setText(String.valueOf(unsetColor.getRed()));
        unsetPixelGreenField = new JTextField();
        ((PlainDocument) unsetPixelGreenField.getDocument()).setDocumentFilter(new IntegerFilter(0, 255));
        unsetPixelGreenField.setText(String.valueOf(unsetColor.getGreen()));
        unsetPixelBlueField = new JTextField();
        ((PlainDocument) unsetPixelBlueField.getDocument()).setDocumentFilter(new IntegerFilter(0, 255));
        unsetPixelBlueField.setText(String.valueOf(unsetColor.getBlue()));

        unsetPixelPanel.add(new JLabel("Red"));
        unsetPixelPanel.add(unsetPixelRedField);
        unsetPixelPanel.add(new JLabel("Green"));
        unsetPixelPanel.add(unsetPixelGreenField);
        unsetPixelPanel.add(new JLabel("Blue"));
        unsetPixelPanel.add(unsetPixelBlueField);

        mainPanel.add(unsetPixelPanel);
    }

    private void buildButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        buttonPanel.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH, BUTTON_PANEL_HEIGHT));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(event -> onOk());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> onCancel());

        buttonPanel.add(new JPanel());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        contentPane.add(buttonPanel);
    }

    private void onCancel() {
        this.state = Constants.CANCEL_CLICKED;
        dialog.dispose();
    }

    private void onOk() {
        if(isAnyFieldEmpty()) {
            JOptionPane.showMessageDialog(dialog, "All the color fields are required", "Invalid Pixel Colors", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.state = Constants.OK_CLICKED;
        setColor = constructColor(setPixelRedField, setPixelGreenField, setPixelBlueField);
        unsetColor = constructColor(unsetPixelRedField, unsetPixelGreenField, unsetPixelBlueField);
        dialog.dispose();
    }

    private Color constructColor(JTextField redField, JTextField greenField, JTextField blueField) {
        int red = Integer.parseInt(redField.getText()),
                green = Integer.parseInt(greenField.getText()),
                blue = Integer.parseInt(blueField.getText());
        return new Color(red, green, blue);
    }

    private boolean isAnyFieldEmpty() {
        return Constants.isStringEmpty(setPixelRedField.getText()) ||
                Constants.isStringEmpty(setPixelGreenField.getText()) ||
                Constants.isStringEmpty(setPixelBlueField.getText()) ||
                Constants.isStringEmpty(unsetPixelRedField.getText()) ||
                Constants.isStringEmpty(unsetPixelGreenField.getText()) ||
                Constants.isStringEmpty(unsetPixelBlueField.getText());
    }
}
