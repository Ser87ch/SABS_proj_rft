package Modules;

import java.io.File;

import resources.Modules.CompareOutputHelper;
import ru.sabstest.DeltaDB;
import ru.sabstest.Log;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;

public class CompareOutput extends CompareOutputHelper {

    public void testMain(Object[] args) {
	// List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	// ��������� ��
	ReadEDList plOut = new ReadEDList();
	plOut.readFolder(Settings.fullfolder + "\\output\\" + num);
	ReadEDList plEt = new ReadEDList();
	plEt.readFolder(Settings.datafolder + "etalon\\" + num);

	if (plOut.getSize() != 0 && plEt.getSize() != 0) {
	    boolean cmp = plOut.equals(plEt);

	    vpManual("CompareOutput", cmp, true).performTest();

	    if (cmp) {
		Log.msgCMP("���������� ��������� � ���������.");
		logTestResult("���������� ��������� � ���������.", true);

	    } else {
		Log.msgCMP("���������� �� ��������� � ���������.");
		logTestResult("���������� �� ��������� � ���������.", false);
	    }
	} else if (plOut.getSize() == 0) {
	    Log.msg("����������� �������� ������.");
	    logTestResult("���������� �������� ������.", false);
	} else if (plEt.getSize() == 0) {
	    Log.msg("����������� ��������� ������.");
	    logInfo("���������� ��������� ������.");
	}

	// ��������� ��������� � ��
	File[] dbet = DeltaDB.getDeltaDBFiles(Settings.datafolder + "etalon\\"
		+ num);
	File[] dbout = DeltaDB.getDeltaDBFiles(Settings.fullfolder
		+ "\\output\\" + num);

	if (dbet == null)
	    dbet = new File[0];

	if (dbout == null)
	    dbout = new File[0];

	if (dbet.length != 0 && dbout.length != 0) {
	    if (DeltaDB.cmpDeltaDBfld(dbet, dbout)) {
		Log.msgCMP("��������� � �� ��������� � ���������.");
		logTestResult("��������� � �� ��������� � ���������.", true);
	    } else {
		Log.msgCMP("��������� � �� �� ��������� � ���������.");
		logTestResult("��������� � �� �� ��������� � ���������.", false);
	    }
	} else if (dbout.length == 0 && dbet.length != 0) {
	    Log.msg("����������� �������� ��������� � ��.");
	    logTestResult("���������� �������� ��������� � ��.", false);
	} else if (dbet.length == 0 && dbout.length != 0) {
	    Log.msg("����������� ��������� ��������� � ��.");
	    logInfo("���������� ��������� ��������� � ��.");
	}

    }
}
