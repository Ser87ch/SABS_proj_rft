package ElObmen.EoIn;
import resources.ElObmen.EoIn.EoInContrHelper;
import ru.sabstest.Log;

import com.rational.test.ft.*;
import com.rational.test.ft.object.interfaces.*;
import com.rational.test.ft.object.interfaces.SAP.*;
import com.rational.test.ft.object.interfaces.WPF.*;
import com.rational.test.ft.object.interfaces.dojo.*;
import com.rational.test.ft.object.interfaces.siebel.*;
import com.rational.test.ft.object.interfaces.flex.*;
import com.rational.test.ft.object.interfaces.generichtmlsubdomain.*;
import com.rational.test.ft.script.*;
import com.rational.test.ft.value.*;
import com.rational.test.ft.vp.*;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;
/**
 * Description   : Functional Test Script
 * @author Admin
 */
public class EoInContr extends EoInContrHelper
{
	/**
	 * Script Name   : <b>EoInContr</b>
	 * Generated     : <b>01.04.2013 12:17:32</b>
	 * Description   : Functional Test Script
	 * Original Host : WinNT Version 5.1  Build 2600 (S)
	 * 
	 * @since  2013/04/01
	 * @author Admin
	 */
	public void testMain(Object[] args) 
	{

		// Window: purs_loader.exe: 044582002 �� ����� ������ ��������
		// ����� ������ ���� 
		mainMenuTree().click(atName("�������� �� (�����)"));
		
		sleep(2);
		
		// ����� ������ "������ � ��������"
		IconPanel().click(atPoint(191,12));
		
		// Window: purs_loader.exe:
		// ����� ������ "���������� � �������" � ���� ������ � ��������
		WorkWithFddMenu().click(atPoint(38,11));
		
		sleep(2);
		
		// Window: purs_loader.exe: ��������
		// ����������� ��������� 
		��button().click(atPoint(30,12));
		
		// Window: purs_loader.exe: 044582002 �� ����� ������ ��������
		// "���������� ��������"
		IconPanel().click(atPoint(63,16));

		sleep(2);
		
		// Window: purs_loader.exe: 044582002 �� ����� ������ ��������
		mainMenuTree().click(atName("����������� �������"));
		
		Log.msg("��������� ��������� �������� ������� �� ���-�� �� ��� ����������(���� ��).");
	}
}

