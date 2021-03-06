package SABS;

import java.io.File;

import resources.SABS.ResetHelper;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.Pack;
import ru.sabstest.Settings;

import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;

public class Reset extends ResetHelper {

    public void testMain(Object[] args) {
	try {
	    Settings.testProj = TSSUtility.getScriptOption("projFolder");
	} catch (TSSException e) {
	    e.printStackTrace();
	    Log.msg(e);
	}

	if (Settings.testProj == null)
	    Settings.testProj = (String) args[0];

	Init.load();
	Settings.readXML(Settings.testProj + "settings\\general.xml");
	// DM+
	Log.msg("---------���������� ���� � ������������.---------");

	Pack.clearFolder(new File(Settings.path + "\\post\\archive"));
	Log.msg("����� " + Settings.path + "\\post\\archive �������.");

	Pack.clearFolder(new File(Settings.path + "\\post\\kPuI"));
	Log.msg("����� " + Settings.path + "\\post\\kPuI �������.");

	Pack.clearFolder(new File(Settings.path + "\\post\\kPuO"));
	Log.msg("����� " + Settings.path + "\\post\\kPuO �������.");

	Pack.clearFolder(new File(Settings.path + "\\post\\oPuO"));
	Log.msg("����� " + Settings.path + "\\post\\oPuO �������.");

	Pack.clearFolder(new File(Settings.path + "\\post\\oPuI"));
	Log.msg("����� " + Settings.path + "\\post\\oPuI �������.");

	// DM+ ����� ��
	// ���������
	Pack.clearFolder(new File(Settings.path + "\\post\\kUfI"));
	Log.msg("����� " + Settings.path + "\\post\\kPuI �������.");

	Pack.clearFolder(new File(Settings.path + "\\post\\kUfO"));
	Log.msg("����� " + Settings.path + "\\post\\kPuO �������.");

	// ����������������
	Pack.clearFolder(new File(Settings.path + "\\post\\oUfI"));
	Log.msg("����� " + Settings.path + "\\post\\oPuO �������.");

	Pack.clearFolder(new File(Settings.path + "\\post\\oUfO"));
	Log.msg("����� " + Settings.path + "\\post\\oPuI �������.");

	// "�������"
	Pack.clearFolder(new File(Settings.path + "\\post\\ufebs\\in"));
	Log.msg("����� " + Settings.path + "\\post\\ufebs\\in �������.");

	Pack.clearFolder(new File(Settings.path + "\\post\\ufebs\\out"));
	Log.msg("����� " + Settings.path + "\\post\\ufebs\\out �������.");

	run(Settings.path + "\\bin\\sabs_set.exe", Settings.path + "\\bin");

	SABSSetwindow().waitForExistence(15.0, 2.0);
	SABSSetwindow().maximize();
	sleep(2);
	Menutree().doubleClick(atName("���������� ��"));
	sleep(2);
	Menutree().doubleClick(
		atName("�������������� �� �� ��������� ����� ��"));

	DBpasswindow().inputKeys("1");
	Passokbutton().click();

	// DM+
	// Dumptree().click(atName("0402open.bkp"));
	// Dumptree().click(atName("0402open_eo0.bkp"));
	//
	// String dmpnm = "0402open_eo0.bkp";
	// Dumptree().click(atName(dmpnm));
	//
	Dumptree().click(atName(Settings.dumpname));

	FileOKbutton().click();
	sleep(2);
	Confirmbutton().click();

	sleep(60);
	Endwindow().waitForExistence(1200.0, 10.0);
	sleep(2);
	��button().click();

	SABSSetwindow(ANY, MAY_EXIT).click(CLOSE_BUTTON);

	// DM
	Log.msg("�� ������������� �� �� " + Settings.dumpname);

    }
}
