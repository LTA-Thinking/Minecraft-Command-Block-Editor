/**
 * @(#)TabCloseButton.java
 *
 *
 * @author
 * @version 1.00 2015/3/28
 */

 /*
  * Change grid layout to a border layout with a horizontal box!
  */

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JButton;

public class TabCloseButton extends JPanel
implements ActionListener
{
	private JTabbedPane pane;
	private Component content;

    public TabCloseButton(JTabbedPane p)
    {
    	pane = p;
    	int index = pane.getTabCount()-1;
    	content = pane.getComponentAt(index);

    	JLabel name = new JLabel(pane.getTitleAt(index));

    	JButton close = new JButton("X");
    	close.addActionListener(this);

    	setLayout(new GridLayout(1,2));

    	add(name);
    	add(close);
    }

    public void actionPerformed(ActionEvent e)
    {
    	pane.remove(content);
    }


}