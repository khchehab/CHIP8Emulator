package chip.eight.emulator.gui.dialogs;

import chip.eight.emulator.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class KeypadMappingDialog extends SettingDialog {
    private Map<Integer, Integer> keypadMapping;
    private JTextField[] keypadMappingFieldList;

    public KeypadMappingDialog(JFrame parent, Map<Integer, Integer> keypadMapping) {
        this.keypadMapping = keypadMapping;

        initDialog(parent, "Keypad Mapping", "Invalid Keypad Mapping", "All the key mappings should be filled");
    }

    public Map<Integer, Integer> getKeypadMapping() {
        return keypadMapping;
    }

    @Override
    protected JPanel buildMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 4, 1, 1));
        mainPanel.setPreferredSize(new Dimension(getMainPanelWidth(), getMainPanelHeight()));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        return mainPanel;
    }

    @Override
    protected int getMainPanelWidth() {
        return 300 + (2 * PADDING);
    }

    @Override
    protected int getMainPanelHeight() {
        return 300 + (2 * PADDING);
    }

    @Override
    protected void applyOkChanges() {

    }

    @Override
    protected boolean isRequiredEmpty() {
        return false;
    }
}
