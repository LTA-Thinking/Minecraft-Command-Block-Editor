/**
 * @(#)CopyButtonListener.java
 *
 *
 * @author
 * @version 1.00 2015/4/5
 *
 * Thanks to LouwHopley at StackOverflow.com for the clipboard copying code
 * http://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
 *
 */

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.Toolkit;

public class CopyButtonListener
implements ActionListener{

	private JTextArea display;

	public CopyButtonListener(JTextArea ta)
	{
		display = ta;
	}

    public void actionPerformed(ActionEvent e)
    {
    	String displayText = display.getText();
		StringSelection stringSelection = new StringSelection(displayText);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection,null);
    }


}