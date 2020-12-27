package chip.eight.emulator.gui.dialogs;

import javax.swing.*;
import java.awt.*;

public class ScaleDialog extends SettingDialog {
    private int scale;
    private JSpinner scaleSpinner;

    public ScaleDialog(JFrame parent, int scale) {
        this.scale = scale;
        initDialog(parent, "Scale");
    }

    public int getScale() {
        return scale;
    }

    @Override
    protected JPanel buildMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setPreferredSize(new Dimension(getMainPanelWidth(), getMainPanelHeight()));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        scaleSpinner = new JSpinner(new SpinnerNumberModel(scale, 10, 20, 1));
        ((JSpinner.DefaultEditor) scaleSpinner.getEditor()).getTextField().setEditable(false);

        mainPanel.add(new JLabel("Scale"));
        mainPanel.add(scaleSpinner);

        return mainPanel;
    }

    @Override
    protected int getMainPanelWidth() {
        return 200 + (2 * PADDING);
    }

    @Override
    protected int getMainPanelHeight() {
        return 30 + (2 * PADDING);
    }

    @Override
    protected void applyOkChanges() {
        scale = (int) scaleSpinner.getValue();
    }

    @Override
    protected boolean isRequiredEmpty() {
        return false;
    }
}
