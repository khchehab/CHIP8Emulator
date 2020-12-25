package chip.eight.emulator.gui.dialogs;

import chip.eight.emulator.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class SettingDialog {
    private static final int BUTTON_PANEL_HEIGHT = 30;

    private JDialog dialog;
    private JPanel contentPane;

    private JFrame parent;
    private String title;
    private String requiredErrorTitle;
    private String requiredErrorMessage;
    private int state;

    public void show() {
        dialog.setVisible(true);
    }

    public boolean isOkClicked() {
        return state == Constants.OK_CLICKED;
    }

    protected abstract JPanel buildMainPanel();
    protected abstract int getMainPanelWidth();
    protected abstract int getMainPanelHeight();
    protected abstract void applyOkChanges();
    protected abstract boolean isRequiredEmpty();

    protected void initDialog(JFrame parent, String title, String requiredErrorTitle, String requiredErrorMessage) {
        this.parent = parent;
        this.title = title;
        this.requiredErrorTitle = requiredErrorTitle;
        this.requiredErrorMessage = requiredErrorMessage;
        this.state = -1;

        buildDialog();
    }

    protected void initDialog(JFrame parent, String title) {
        initDialog(parent, title, null, null);
    }

    private void buildDialog() {
        dialog = new JDialog(parent, title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(parent);

        int dialogWidth = getMainPanelWidth(),
                dialogHeight = getMainPanelHeight() + BUTTON_PANEL_HEIGHT;

        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setPreferredSize(new Dimension(dialogWidth, dialogHeight));
        contentPane.registerKeyboardAction(event -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        contentPane.registerKeyboardAction(event -> onOk(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        contentPane.add(buildMainPanel());
        contentPane.add(buildButtonPanel());

        dialog.setContentPane(contentPane);
        dialog.pack();
    }

    private JPanel buildButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        buttonPanel.setPreferredSize(new Dimension(getMainPanelWidth(), BUTTON_PANEL_HEIGHT));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(event -> onOk());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> onCancel());

        buttonPanel.add(new JPanel());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }

    private void onCancel() {
        state = Constants.CANCEL_CLICKED;
        dialog.dispose();
    }

    private void onOk() {
        if(isRequiredEmpty()) {
            JOptionPane.showMessageDialog(dialog, requiredErrorMessage, requiredErrorTitle, JOptionPane.ERROR_MESSAGE);
            return;
        }

        state = Constants.OK_CLICKED;
        applyOkChanges();
        dialog.dispose();
    }


}
