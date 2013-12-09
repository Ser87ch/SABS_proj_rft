package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import ru.sabstest.Packet;
import ru.sabstest.ReadED;
import ru.sabstest.ReadEDList;

public class MainWindow {
    private JFrame frm = new JFrame();
    private JComboBox testNoCb = new JComboBox(), etalonNoCb = new JComboBox(),
	    tcNoCb = new JComboBox();
    private JButton cmpBtn = new JButton("Сравнить"), openBtn = new JButton(
	    "Открыть файл");
    private DefaultListModel lItems = new DefaultListModel();
    private JList lst = new JList(lItems);
    private String projFolder = "C:\\sabstest\\", testFolder = projFolder
	    + "tests\\", dataFolder = projFolder + "data\\", outputFolder,
	    etalonFolder;
    private ReadEDList rOut, rEt;
    private ActionListener cmpButtonListener = new ActionListener() {

	@Override
	public void actionPerformed(ActionEvent e) {
	    lItems.removeAllElements();
	    openBtn.setEnabled(false);

	    int sizeOut = new File(outputFolder).list() == null ? 0 : new File(
		    outputFolder).list().length;
	    int sizeEt = new File(etalonFolder).list() == null ? 0 : new File(
		    etalonFolder).list().length;

	    if (sizeOut == 0) {
		JOptionPane.showMessageDialog(null,
			"Отсуствуют выходные данные.", "Сообщение",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    if (sizeEt == 0) {
		JOptionPane.showMessageDialog(null,
			"Отсуствуют эталонные данные.", "Сообщение",
			JOptionPane.WARNING_MESSAGE);
		return;
	    }

	    if (sizeEt != sizeOut) {
		JOptionPane.showMessageDialog(null,
			"Количество файлов не совпадает.", "Сообщение",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }

	    rOut = new ReadEDList();
	    rEt = new ReadEDList();

	    rOut.readFolder(outputFolder);
	    rEt.readFolder(etalonFolder);

	    if (rOut.equals(rEt)) {
		JOptionPane.showMessageDialog(null, "Данные совпадают.",
			"Сообщение", JOptionPane.INFORMATION_MESSAGE);
		openBtn.setEnabled(true);

		for (ReadED r : rOut.pList) {
		    Packet pOut = (Packet) r;
		    lItems.addElement(pOut.filename);
		}
		lst.setSelectedIndex(0);

	    } else {
		JOptionPane.showMessageDialog(null, "Данные не совпадают.",
			"Сообщение", JOptionPane.ERROR_MESSAGE);
		openBtn.setEnabled(true);
		int i = 0;
		boolean[] isRed = new boolean[rOut.getSize()];
		for (ReadED r : rOut.pList) {
		    Packet pOut = (Packet) r;
		    Packet pEt = (Packet) rEt.pList.get(i);
		    lItems.addElement(pOut.filename);
		    if (!pOut.equals(pEt))
			isRed[i] = true;
		    i++;
		}
		lst.setCellRenderer(new MyRenderer(isRed));
		lst.setSelectedIndex(0);
		// lst.revalidate();
	    }
	}
    };

    @SuppressWarnings("serial")
    private class MyRenderer extends DefaultListCellRenderer {
	boolean[] isRed;

	MyRenderer(boolean[] isRed) {
	    this.isRed = isRed;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
		int index, boolean isSelected, boolean cellHasFocus) {
	    JLabel lbl = (JLabel) super.getListCellRendererComponent(list,
		    value, index, isSelected, cellHasFocus);

	    if (isRed[index])
		lbl.setForeground(Color.RED);

	    return lbl;
	}
    }

    MainWindow() {

	frm.setTitle("Сравнение выходных данных с эталоном");

	loadComboBox(testNoCb, new File(testFolder).list());
	loadComboBox(etalonNoCb, new File(dataFolder).list());
	cmpBtn.addActionListener(cmpButtonListener);

	testNoCb.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		String testDataFolder = testFolder
			+ (String) testNoCb.getSelectedItem() + "\\output\\";
		loadComboBox(tcNoCb, new File(testDataFolder).list());
	    }
	});

	tcNoCb.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		outputFolder = testFolder + (String) testNoCb.getSelectedItem()
			+ "\\output\\" + (String) tcNoCb.getSelectedItem()
			+ "\\";
		etalonFolder = dataFolder
			+ (String) etalonNoCb.getSelectedItem() + "\\etalon\\"
			+ (String) tcNoCb.getSelectedItem() + "\\";
		;
	    }
	});

	openBtn.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		if (rOut != null && rEt != null) {
		    int index = lst.getSelectedIndex();
		    new ComparePacketWindow((Packet) rOut.get(index),
			    (Packet) rEt.get(index));
		}
	    }
	});

	openBtn.setEnabled(false);
	etalonNoCb.setSelectedIndex(etalonNoCb.getItemCount() - 1);
	testNoCb.setSelectedIndex(testNoCb.getItemCount() - 1);
	lst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	JPanel p = new JPanel(), p1 = new JPanel(), p2 = new JPanel(), p3 = new JPanel();
	p.setLayout(new GridLayout(4, 1));

	p1.setLayout(new BorderLayout());
	p1.add(BorderLayout.WEST, new JLabel("Номер теста          "));
	p1.add(BorderLayout.CENTER, testNoCb);

	p2.setLayout(new BorderLayout());
	p2.add(BorderLayout.WEST, new JLabel("Номер эталона      "));
	p2.add(BorderLayout.CENTER, etalonNoCb);

	p3.setLayout(new BorderLayout());
	p3.add(BorderLayout.WEST, new JLabel("Номер тест-кейса "));
	p3.add(BorderLayout.CENTER, tcNoCb);

	p.add(p1);
	p.add(p2);
	p.add(p3);
	p.add(cmpBtn);
	frm.add(BorderLayout.NORTH, p);
	frm.add(BorderLayout.CENTER, new JScrollPane(lst));
	frm.add(BorderLayout.SOUTH, openBtn);
    }

    private void loadComboBox(JComboBox cb, String[] strs) {
	cb.removeAllItems();
	for (String str : strs)
	    cb.addItem(str);
    }

    public JFrame getJFrame() {
	return frm;
    }

    public static void main(String[] args) {
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	SwingConsole.run((new MainWindow()).getJFrame(), 300, 400);
    }
}
