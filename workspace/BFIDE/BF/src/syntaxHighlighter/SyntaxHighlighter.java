package syntaxHighlighter;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SyntaxHighlighter implements DocumentListener {
	
	private Set<String> keyChar1;
	private Set<String> keyChar2;
	private Set<String> keyChar3;
	private Set<String> keyChar4;
	
	private Style keyChar1Style;
	private Style keyChar2Style;
	private Style keyChar3Style;
	private Style keyChar4Style;
	private Style normalStyle;
	
	private static final int CHAR_LENGTH = 1;
	
	
	public SyntaxHighlighter(JTextPane textPane) {
		// 准备着色使用的样式
		keyChar1Style = ((StyledDocument) textPane.getDocument()).addStyle("KeyMark1Style", null);
		keyChar2Style = ((StyledDocument) textPane.getDocument()).addStyle("KeyMark2Style", null);
		keyChar3Style = ((StyledDocument) textPane.getDocument()).addStyle("KeyMark3Style", null);
		keyChar4Style = ((StyledDocument) textPane.getDocument()).addStyle("KeyMark4Style", null);
		normalStyle= ((StyledDocument) textPane.getDocument()).addStyle("Keyword_Style", null);
		
		StyleConstants.setForeground(keyChar1Style, Color.RED);
		StyleConstants.setForeground(keyChar2Style, Color.BLUE);
		StyleConstants.setForeground(keyChar3Style, new Color(148,0,211));
		StyleConstants.setForeground(keyChar4Style, Color.BLACK);
		StyleConstants.setFontSize(keyChar4Style, 30);
		StyleConstants.setForeground(normalStyle, Color.LIGHT_GRAY);
		StyleConstants.setStrikeThrough(normalStyle, true);

		// 准备关键字
		keyChar1 = new HashSet<String>();
		keyChar1.add(">");
		keyChar1.add("<");
		keyChar2 = new HashSet<String>();
		keyChar2.add("+");
		keyChar2.add("-");
		keyChar3 = new HashSet<String>();
		keyChar3.add("[");
		keyChar3.add("]");
		keyChar4 = new HashSet<String>();
		keyChar4.add(".");
		keyChar4.add(",");
	}

	public void coloring(StyledDocument styledDocument) throws BadLocationException {
		int start = 0;
		int end = styledDocument.getLength();
		
		while (start < end) {
				start = colouringWord(styledDocument, start);
		}
	}

	public int colouringWord(StyledDocument styledDocument, int position) throws BadLocationException {
		String word = styledDocument.getText(position, CHAR_LENGTH);

		if (keyChar1.contains(word)) {
			SwingUtilities.invokeLater(new ColouringTask(styledDocument, position, keyChar1Style));
		} else if(keyChar2.contains(word)) {
			SwingUtilities.invokeLater(new ColouringTask(styledDocument, position, keyChar2Style));
		} else if(keyChar3.contains(word)) {
			SwingUtilities.invokeLater(new ColouringTask(styledDocument, position, keyChar3Style));
		} else if(keyChar4.contains(word)) {
			SwingUtilities.invokeLater(new ColouringTask(styledDocument, position, keyChar4Style));
		} else if(word.equals(" ")){
		
		} else {
			SwingUtilities.invokeLater(new ColouringTask(styledDocument, position, normalStyle));
		}

		return position+1;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {}

	@Override
	public void insertUpdate(DocumentEvent e) {
		try {
			coloring((StyledDocument) e.getDocument());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		try {
			coloring((StyledDocument) e.getDocument());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	private class ColouringTask implements Runnable {
		private StyledDocument styledDocument;
		private Style style;
		private int position;

		public ColouringTask(StyledDocument styledDocument, int position, Style style) {
			this.styledDocument = styledDocument;
			this.position = position;
			this.style = style;
		}

		public void run() {
			try {
				styledDocument.setCharacterAttributes(position, CHAR_LENGTH, style, true);
			} catch (Exception e) {}
		}
	}
}