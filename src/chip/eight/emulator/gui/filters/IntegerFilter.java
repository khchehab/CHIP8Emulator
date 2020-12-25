package chip.eight.emulator.gui.filters;

import chip.eight.emulator.util.Constants;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class IntegerFilter extends DocumentFilter {
    private int minimumValue, maximumValue;

    public IntegerFilter(int minimumValue, int maximumValue) {
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        Document doc = fb.getDocument();

        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if(testInteger(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();

        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if(testInteger(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        Document doc = fb.getDocument();

        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);
        String text = sb.toString();

        if(Constants.isStringEmpty(text) || testInteger(text)) {
            super.remove(fb, offset, length);
        }
    }

    private boolean testInteger(String text) {
        try {
            int i = Integer.parseInt(text);
            return i >= minimumValue && i <= maximumValue;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
