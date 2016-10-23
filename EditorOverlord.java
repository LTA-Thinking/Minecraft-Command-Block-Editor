/**
 * @(#)EditorOverlord.java
 *
 *
 * @author
 * @version 1.00 2015/3/22
 */

 /* Things to add
  *		- Seperate tabs for compiled files
  *			- pop out window with button to copy text in it
  *		- Easier way to close tabs (make it pretty)
  *		- Toolbars
  *		- Line Numbers
  */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.JTabbedPane;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;

public class EditorOverlord
{
	private EditorMenu menu;
	private JTabbedPane mainWindow;
	private JTextArea output;

    public EditorOverlord()
    {
    	menu = new EditorMenu(this);
    	mainWindow = new JTabbedPane(JTabbedPane.TOP);

    	output = new JTextArea();
    	output.setEditable(false);
    	output.setText("Output:\r\n");

    	JScrollPane qScroller = new JScrollPane(output);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    	JFrame frame = new JFrame("Endless Void's Minecraft Command Block Code Editor");

    	EditorWindowListener listener = new EditorWindowListener(this,frame);

    	frame.addWindowListener(listener);

    	Container c = frame.getContentPane();
    	c.add(BorderLayout.CENTER, mainWindow);
    	c.add(BorderLayout.SOUTH, qScroller);

    	frame.setJMenuBar(menu);
    	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(600,400);
		frame.setVisible(true);

    }

    public void openNewTab()
    {
    	int count = 1;
    	while(mainWindow.indexOfTab("Untitled" + count)!=-1)
    	{
    		count++;
    	}
    	mainWindow.add("Untitled"+count,new MinecraftEditor(null));
    	mainWindow.setTabComponentAt(mainWindow.getTabCount()-1,new TabCloseButton(mainWindow));
    }

    public void openNewTab(File file)
    {
    	try
    	{
	    	MinecraftEditor newTab = new MinecraftEditor(file);
	    	String name = file.getName();

	    	if(mainWindow.indexOfTab(name)!=-1)
	    	{
	    		int count=1;
	    		while(mainWindow.indexOfTab(name+"_"+count)!=-1)
	    		{
	    			count++;
	    		}
	    		name = name+"_"+count;
	    	}

	    	mainWindow.add(file.getName(),newTab);
	    	mainWindow.setTabComponentAt(mainWindow.getTabCount()-1,new TabCloseButton(mainWindow));

	    	String line;
	    	BufferedReader reader = new BufferedReader(new FileReader(file));

			while((line=reader.readLine())!=null)
			{
				newTab.addLine(line);
			}

			reader.close();

			newTab.save();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }

    public void save(File file)
    {
    	try
    	{
	    	int index = mainWindow.getSelectedIndex();
	    	MinecraftEditor selected = (MinecraftEditor)mainWindow.getSelectedComponent();

	    	BufferedWriter writer = new BufferedWriter(new FileWriter(file));

	    	writer.write(selected.getText());
	    	writer.flush();
	    	writer.close();

	    	mainWindow.setTitleAt(index,file.getName());
	    	selected.setFile(file);
	    	selected.save();

	    	addToOutput("Saved file " + file.getName());
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}

    }

    public void close()
    {
    	if(checkSave(mainWindow.getSelectedIndex()))
    	{
    		mainWindow.remove(mainWindow.getSelectedIndex());
    	}
    }

    public File getFile()
    {
    	return ((MinecraftEditor)mainWindow.getSelectedComponent()).getFile();
    }

    public boolean checkAllSave()
    {
    	int numTabs = mainWindow.getTabCount();

    	for(int i=0;i<numTabs;i++)
    	{
    		if(!checkSave(i))
    			return false;
    	}
    	return true;
    }

    public boolean checkSave(int tab)
    {
    	MinecraftEditor e = (MinecraftEditor)mainWindow.getComponentAt(tab);

    	if(!e.isSaved())
    	{
    		int option = JOptionPane.showConfirmDialog(null,"Do you wish to save?","Save",JOptionPane.YES_NO_CANCEL_OPTION);

    		if(option==JOptionPane.YES_OPTION)
    		{
    			File file = e.getFile();
    			mainWindow.setSelectedIndex(tab);
				if(file==null)
				{
					menu.saveAs();
				}
				else
				{
					save(file);
				}
    		}
    		if(option==JOptionPane.CANCEL_OPTION)
    		{
    			return false;
    		}
    	}

    	return true;
    }

    public boolean checkSave()
    {
    	return checkSave(mainWindow.getSelectedIndex());
    }

    public void addToOutput(String s)
    {
    	output.append(s+"\r\n\r\n");
    }

    public void openCompiledWindow(File file)
    {
    	try
    	{
	    	JFrame compiledWindow = new JFrame(file.getName());
			JTextArea display = new JTextArea();
	    	String line;
	    	BufferedReader reader = new BufferedReader(new FileReader(file));

			while((line=reader.readLine())!=null)
			{
				display.append(line+"\r\n");
			}

			reader.close();

			display.setEditable(false);

			JScrollPane qScroller = new JScrollPane(display);
			qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

			JButton copy = new JButton("Copy Text");
			copy.addActionListener(new CopyButtonListener(display));

			Container c = compiledWindow.getContentPane();
			c.add(qScroller, BorderLayout.CENTER);
			c.add(copy, BorderLayout.SOUTH);

	    	compiledWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			compiledWindow.setSize(600,400);
			compiledWindow.setVisible(true);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }

    public static void main(String[] args)
    {
    	new EditorOverlord();
    }
}