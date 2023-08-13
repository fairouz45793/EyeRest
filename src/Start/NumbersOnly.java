
package Start;

import javax.swing.text.*;



public class NumbersOnly extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        Document doc = fb.getDocument();
        if (doc.getLength() > 1) return;

        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (test(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }


    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        if (doc.getLength() > 1) return;

        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        if (text.length() > 1) {
            text = text.substring(0, 2 - doc.getLength());
        }
        sb.replace(offset, offset + length, text);

        if (test(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }


    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);
        if (sb.isEmpty()) sb.append(0);

        if (test(sb.toString())) {
            super.remove(fb, offset, length);
        }
    }


    private boolean test(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
