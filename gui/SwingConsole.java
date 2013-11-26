package gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SwingConsole {
    public static void run(final JFrame f, final int width, final int height) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		// f.setTitle(f.getClass().getSimpleName());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(width, height);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	    }
	});
    }

    public static void run(final JFrame f, final int width, final int height,
	    final boolean isChild) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		// f.setTitle(f.getClass().getSimpleName());
		if (isChild)
		    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		else
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(width, height);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	    }
	});
    }
}