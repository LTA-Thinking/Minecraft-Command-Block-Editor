/**
 * @(#)EditorWindowListener.java
 *
 *
 * @author
 * @version 1.00 2015/3/28
 */
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class EditorWindowListener
implements WindowListener
{
	private EditorOverlord parent;
	private JFrame window;

    public EditorWindowListener(EditorOverlord p,JFrame w)
    {
    	parent = p;
    	window = w;
    }

    public void windowActivated(WindowEvent e)
    {
    }

    public void windowClosed(WindowEvent e)
    {
    }

    public void windowClosing(WindowEvent e)
    {
    	if(parent.checkAllSave())
    	{
    		window.dispose();
    		System.exit(0);
    	}
    }

    public void windowDeactivated(WindowEvent e)
    {
    }

	public void windowDeiconified(WindowEvent e)
	{
	}

    public void windowIconified(WindowEvent e)
    {
    }

    public void windowOpened(WindowEvent e)
    {
    }
}