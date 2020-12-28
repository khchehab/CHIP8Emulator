package chip.eight.emulator.gui.dialogs;

import chip.eight.emulator.gui.filters.IntegerFilter;
import chip.eight.emulator.util.Constants;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class PixelColorDialog extends SettingDialog {
    private static final int SET_PIXEL_PANEL_HEIGHT = 50;
    private static final int UNSET_PIXEL_PANEL_HEIGHT = 50;

    private Color setColor, unsetColor;
    private JTextField setPixelRedField, setPixelGreenField, setPixelBlueField,
            unsetPixelRedField, unsetPixelGreenField, unsetPixelBlueField;

    public PixelColorDialog(JFrame parent, Color setColor, Color unsetColor) {
        this.setColor = setColor;
        this.unsetColor = unsetColor;

        initDialog(parent, "Pixel Color Chooser", "Invalid Pixel Colors", "All the color fields are required.");
    }

    public Color getSetColor() {
        return setColor;
    }

    public Color getUnsetColor() {
        return unsetColor;
    }

    @Override
    protected JPanel buildMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.setPreferredSize(new Dimension(getMainPanelWidth(), getMainPanelHeight()));

        mainPanel.add(buildSetPixelPanel());
        mainPanel.add(buildUnsetPixelPanel());

        return mainPanel;
    }

    @Override
    protected int getMainPanelWidth() {
        return 300;
    }

    @Override
    protected int getMainPanelHeight() {
        return SET_PIXEL_PANEL_HEIGHT + UNSET_PIXEL_PANEL_HEIGHT;
    }

    @Override
    protected void applyOkChanges() {
        setColor = constructColor(setPixelRedField, setPixelGreenField, setPixelBlueField);
        unsetColor = constructColor(unsetPixelRedField, unsetPixelGreenField, unsetPixelBlueField);
    }

    @Override
    protected boolean isRequiredEmpty() {
        return Constants.isStringEmpty(setPixelRedField.getText()) ||
                Constants.isStringEmpty(setPixelGreenField.getText()) ||
                Constants.isStringEmpty(setPixelBlueField.getText()) ||
                Constants.isStringEmpty(unsetPixelRedField.getText()) ||
                Constants.isStringEmpty(unsetPixelGreenField.getText()) ||
                Constants.isStringEmpty(unsetPixelBlueField.getText());
    }

    private JPanel buildSetPixelPanel() {
        JPanel setPixelPanel = new JPanel();
        setPixelPanel.setLayout(new GridLayout(1, 3));
        setPixelPanel.setPreferredSize(new Dimension(getMainPanelWidth(), SET_PIXEL_PANEL_HEIGHT));
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

        JPanel redPanel = new JPanel();
        redPanel.setLayout(new BoxLayout(redPanel, BoxLayout.X_AXIS));
        redPanel.add(new JLabel("Red"));
        redPanel.add(setPixelRedField);

        JPanel greenPanel = new JPanel();
        greenPanel.setLayout(new BoxLayout(greenPanel, BoxLayout.X_AXIS));
        greenPanel.add(new JLabel("Green"));
        greenPanel.add(setPixelGreenField);

        JPanel bluePanel = new JPanel();
        bluePanel.setLayout(new BoxLayout(bluePanel, BoxLayout.X_AXIS));
        bluePanel.add(new JLabel("Blue"));
        bluePanel.add(setPixelBlueField);

        setPixelPanel.add(redPanel);
        setPixelPanel.add(greenPanel);
        setPixelPanel.add(bluePanel);

        return setPixelPanel;
    }

    private JPanel buildUnsetPixelPanel() {
        JPanel unsetPixelPanel = new JPanel();
        unsetPixelPanel.setLayout(new GridLayout(1, 3));
        unsetPixelPanel.setPreferredSize(new Dimension(getMainPanelWidth(), UNSET_PIXEL_PANEL_HEIGHT));
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

        JPanel redPanel = new JPanel();
        redPanel.setLayout(new BoxLayout(redPanel, BoxLayout.X_AXIS));
        redPanel.add(new JLabel("Red"));
        redPanel.add(unsetPixelRedField);

        JPanel greenPanel = new JPanel();
        greenPanel.setLayout(new BoxLayout(greenPanel, BoxLayout.X_AXIS));
        greenPanel.add(new JLabel("Green"));
        greenPanel.add(unsetPixelGreenField);

        JPanel bluePanel = new JPanel();
        bluePanel.setLayout(new BoxLayout(bluePanel, BoxLayout.X_AXIS));
        bluePanel.add(new JLabel("Blue"));
        bluePanel.add(unsetPixelBlueField);

        unsetPixelPanel.add(redPanel);
        unsetPixelPanel.add(greenPanel);
        unsetPixelPanel.add(bluePanel);

        return unsetPixelPanel;
    }

    private Color constructColor(JTextField redField, JTextField greenField, JTextField blueField) {
        int red = Integer.parseInt(redField.getText()),
                green = Integer.parseInt(greenField.getText()),
                blue = Integer.parseInt(blueField.getText());
        return new Color(red, green, blue);
    }
}
