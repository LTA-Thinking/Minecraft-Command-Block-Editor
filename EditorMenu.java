/**
 * @(#)EditorMenu.java
 *
 *
 * @author
 * @version 1.00 2015/3/22
 */
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;

public class EditorMenu extends JMenuBar
implements ActionListener
{
	private JMenuItem newFile,save,saveAs,open,close,about,compile,openCompile;
	private EditorOverlord editor;

    public EditorMenu(EditorOverlord e)
    {
    	editor = e;

    	JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');

		newFile = new JMenuItem("New");
		newFile.setMnemonic('N');
		newFile.addActionListener(this);

		save = new JMenuItem("Save");
		save.setMnemonic('S');
		save.addActionListener(this);

		saveAs = new JMenuItem("Save As");
		saveAs.setMnemonic('A');
		saveAs.addActionListener(this);

		open = new JMenuItem("Open");
		open.setMnemonic('O');
		open.addActionListener(this);

		close = new JMenuItem("Close");
		close.setMnemonic('C');
		close.addActionListener(this);

		fileMenu.add(newFile);
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.add(saveAs);
		fileMenu.add(close);

		JMenu help = new JMenu("Help");
		help.setMnemonic('H');

		about = new JMenuItem("About");
		about.setMnemonic('A');
		about.addActionListener(this);

		help.add(about);

		JMenu compileMenu = new JMenu("Compile");
		compileMenu.setMnemonic('C');

		compile = new JMenuItem("Compile");
		compile.setMnemonic('C');
		compile.addActionListener(this);

		openCompile = new JMenuItem("Open Compiled File");
		openCompile.setMnemonic('O');
		openCompile.addActionListener(this);

		compileMenu.add(compile);
		compileMenu.add(openCompile);

		add(fileMenu);
		add(compileMenu);
		add(help);
    }

    public void actionPerformed(ActionEvent e)
	{
		JMenuItem src = (JMenuItem)e.getSource();

		if(src==newFile)
			editor.openNewTab();
		else if(src==save)
		{
			File file = editor.getFile();
			if(file==null)
			{
				saveAs();
			}
			else
			{
				editor.save(file);
			}

		}
		else if(src==saveAs)
		{
			saveAs();
		}
		else if(src==open)
		{
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("Endless Void Minecraft Code Files (.evmc)", "evmc");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION)
		    {
		       File file = chooser.getSelectedFile();
		       editor.openNewTab(file);
		    }
		}
		else if(src==close)
		{
			editor.close();
		}
		else if(src==compile)
		{
			File file = editor.getFile();
			if(file==null)
			{
				saveAs();
				file = editor.getFile();
			}
			else
			{
				editor.save(file);
			}
			file = MinecraftCoder_v2.compile(file,editor);

			if(file!=null)
				editor.openCompiledWindow(file);

		}
		else if(src==openCompile)
		{
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("Compiled Files (.txt)", "txt");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION)
		    {
		       File file = chooser.getSelectedFile();
		       editor.openCompiledWindow(file);
		    }
		}
		else if(src==about)
			showAbout();
	}

	public void saveAs()
	{
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Endless Void Minecraft Code Files (.evmc)", "evmc");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showSaveDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION)
	    {
			File file = chooser.getSelectedFile();
		    String path = file.getPath();
		    if(!path.substring(path.length()-5).equals(".evmc"))
		    {
		    	file = new File(path+".evmc");
		    }
		    editor.save(file);
	    }
	}

	public void showAbout()
	{
		JOptionPane.showMessageDialog(null,"This is a simple IDE for creating code for Minecraft Command Blocks\r\nCreated by Endless Void\r\nVer 0.1\t\t3/22/2015","About",JOptionPane.INFORMATION_MESSAGE);
	}

}