package chip.eight.emulator.gui.dialogs;

import javax.swing.*;
import java.awt.*;

public class CpuFrequencyDialog extends SettingDialog {
    private static final int PADDING = 5;

    private static final int MINIMUM_FREQUENCY = 300;
    private static final int MAXIMUM_FREQUENCY = 1_500;
    private static final int MAJOR_TICK_INTERVAL = 300;
    private static final int MINOR_TICK_INTERVAL = 50;

    private int cpuFrequency;
    private JSlider cpuFrequencySlider;

    public CpuFrequencyDialog(JFrame parent, int cpuFrequency) {
        this.cpuFrequency = cpuFrequency;

        initDialog(parent, "CPU Frequency");
    }

    public int getCpuFrequency() {
        return cpuFrequency;
    }

    @Override
    protected JPanel buildMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(getMainPanelWidth(), getMainPanelHeight()));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        JLabel cpuFrequencyLabel = new JLabel("CPU Frequency (MHz)");
        cpuFrequencyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cpuFrequencySlider = new JSlider(JSlider.HORIZONTAL, MINIMUM_FREQUENCY, MAXIMUM_FREQUENCY, getCpuFrequency());
        cpuFrequencySlider.setMajorTickSpacing(MAJOR_TICK_INTERVAL);
        cpuFrequencySlider.setMinorTickSpacing(MINOR_TICK_INTERVAL);
        cpuFrequencySlider.setPaintTicks(true);
        cpuFrequencySlider.setPaintLabels(true);

        mainPanel.add(cpuFrequencyLabel);
        mainPanel.add(cpuFrequencySlider);

        return mainPanel;
    }

    @Override
    protected int getMainPanelWidth() {
        return 300 + (2 * PADDING);
    }

    @Override
    protected int getMainPanelHeight() {
        return 60 + (2 * PADDING);
    }

    @Override
    protected void applyOkChanges() {
        cpuFrequency = cpuFrequencySlider.getValue();
    }

    @Override
    protected boolean isRequiredEmpty() {
        return false;
    }
}
