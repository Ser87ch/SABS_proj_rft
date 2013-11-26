package gui;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import ru.sabstest.Packet;

public class ComparePacketWindow {
    private Packet pOut, pEt;
    private JFrame frm = new JFrame("Сравнение пакетов");
    private TableModel tM = new TableModel();
    private JTable jTbl = new JTable(tM);
    private List<NameValues> lstShow;

    public ComparePacketWindow(Packet pOut, Packet pEt) {
	this.pOut = pOut;
	this.pEt = pEt;
	loadFields();

	jTbl.getColumnModel().getColumn(0).setCellRenderer(getRend());
	jTbl.getColumnModel().getColumn(1).setCellRenderer(getRend());
	jTbl.getColumnModel().getColumn(2).setCellRenderer(getRend());
	jTbl.getColumnModel().getColumn(3).setCellRenderer(getRend());

	jTbl.getColumnModel().getColumn(0).setHeaderValue("Тип");
	jTbl.getColumnModel().getColumn(1).setHeaderValue("Атрибут");
	jTbl.getColumnModel().getColumn(2).setHeaderValue("Тест");
	jTbl.getColumnModel().getColumn(3).setHeaderValue("Эталон");
	frm.add(new JScrollPane(jTbl));
	SwingConsole.run(frm, 500, 500, true);
    }

    @SuppressWarnings("serial")
    private TableCellRenderer getRend() {
	return new DefaultTableCellRenderer() {
	    @Override
	    public Component getTableCellRendererComponent(JTable table,
		    Object value, boolean isSelected, boolean hasFocus,
		    int row, int col) {

		JLabel l = (JLabel) super.getTableCellRendererComponent(table,
			value, isSelected, hasFocus, row, col);

		if (!lstShow.get(row).isEquals)
		    l.setBackground(Color.RED);
		else
		    l.setBackground(Color.WHITE);

		if (lstShow.get(row).isLast)
		    l.setForeground(Color.BLUE);
		else
		    l.setForeground(Color.BLACK);
		return l;
	    }
	};
    }

    private void loadFields() {
	lstShow = new ArrayList<NameValues>();
	loadSimpleFields(pOut, pEt);
    }

    void loadSimpleFields(Object oOut, Object oEt) {
	Class<?> pClass = oOut.getClass();
	Field[] fields = pClass.getFields();

	for (Field field : fields)
	    if (field.getType() == int.class || field.getType() == String.class
		    || field.getType() == Date.class
		    || field.getType() == boolean.class) {
		String name = "", vOut = "", vEt = "";

		name = field.getName();
		try {
		    Object o = field.get(oOut);
		    vOut = o != null ? o.toString() : "null";

		    o = field.get(oEt);
		    vEt = o != null ? o.toString() : "null";
		} catch (Exception e) {

		    e.printStackTrace();
		}

		lstShow.add(new NameValues(pClass.getSimpleName(), name, vOut,
			vEt));
	    }
	lstShow.get(lstShow.size() - 1).isLast = true;
	for (Field field : fields)
	    if (field.getType() == List.class) {
		List<?> lstO = null, lstE = null;
		try {
		    lstO = (List<?>) field.get(oOut);
		    lstE = (List<?>) field.get(oEt);
		} catch (Exception e) {

		    e.printStackTrace();
		}

		for (int i = 0; i < (lstO.size() >= lstE.size() ? lstO.size()
			: lstE.size()); i++) {
		    Object oO = lstO.get(i);
		    Object oE = lstE.get(i);
		    loadSimpleFields(oO, oE);
		}
	    } else if (field.getType() != int.class
		    && field.getType() != String.class
		    && field.getType() != Date.class
		    && field.getType() != boolean.class
		    && field.getType() != List.class)
		try {
		    loadSimpleFields(field.get(oOut), field.get(oEt));
		} catch (Exception e) {
		    e.printStackTrace();
		}

    }

    @SuppressWarnings("serial")
    private class TableModel extends AbstractTableModel {

	@Override
	public int getColumnCount() {
	    return 4;
	}

	@Override
	public int getRowCount() {
	    return lstShow.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
	    return lstShow.get(arg0).get(arg1);
	}

    }

    private class NameValues {
	String type, name, valueOut, valueEt;
	boolean isEquals, isLast;

	public NameValues(String type, String name, String valueOut,
		String valueEt) {
	    this.type = type;
	    this.name = name;
	    this.valueOut = valueOut;
	    this.valueEt = valueEt;
	    isEquals = valueOut.equals(valueEt);
	}

	public String get(int index) {
	    switch (index) {
	    case 0:
		return type;
	    case 1:
		return name;
	    case 2:
		return valueOut;
	    case 3:
		return valueEt;
	    }
	    return "error";
	}

    }
}
