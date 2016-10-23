/**
 * @(#)MinecraftEditor.java
 *
 *
 * @author
 * @version 1.00 2015/3/10
 */
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JPanel;
//import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.io.File;


public class MinecraftEditor extends JPanel
{
	private JTextArea editor;
	private File file;
	private String lastSave = "";

    public MinecraftEditor(File f)
    {
    	file = f;

    	editor = new JTextArea();
    	editor.setEditable(true);

    	JScrollPane qScroller = new JScrollPane(editor);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		setLayout(new BorderLayout());
    	add(qScroller,BorderLayout.CENTER);
    }

    public void addLine(String line)
    {
    	editor.append(line + "\r\n");
    }

    public String getText()
    {
    	return editor.getText();
    }

    public void setFile(File f)
    {
    	file = f;
    }

    public File getFile()
    {
    	return file;
    }

    public boolean isSaved()
    {
    	return (lastSave.equals(editor.getText()));
    }

    public void save()
    {
    	lastSave = editor.getText();
    }

    /*public static void main(String[] args)
    {
    	JFrame frame = new JFrame();
    	frame.getContentPane().add(BorderLayout.CENTER, new MinecraftEditor());
		frame.setSize(600,400);
		frame.setVisible(true);
    }*/


}