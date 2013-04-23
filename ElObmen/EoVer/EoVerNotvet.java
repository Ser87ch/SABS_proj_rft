package ElObmen.EoVer;
import resources.ElObmen.EoVer.EoVerNotvetHelper;
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
public class EoVerNotvet extends EoVerNotvetHelper
{
	/**
	 * Script Name   : <b>EoVerNform</b>
	 * Generated     : <b>09.04.2013 17:16:20</b>
	 * Description   : Functional Test Script
	 * Original Host : WinNT Version 5.1  Build 2600 (S)
	 * 
	 * @since  2013/04/09
	 * @author Admin
	 */
	public void testMain(Object[] args) 
	{
		/*
		Menutree().click(atName("��������� ��"));	
		ESpanel().click(atPoint(45,15));		
		
		while(Errorwindow().exists())
		{
			OKerrorbutton().click();			
		}
		
		Menutree().click(atName("����������� �������"));
		Log.msg("�� �������.");
		*/
		
		
		// Window: purs_loader.exe
		mainMenuTree().click(atName("��������� ��"));
		
		sleep(8);
		
		//����������� ������ 
		FormEsWinIcon().click(atPoint(39,9));

		
		// Window: purs_loader.exe: ������
		while(ErrorWindow().exists())
		{
			OKbutton().click();			
		}

		
		// Window: purs_loader.exe: 044582002 �� ����� ������ ��������
		mainMenuTree().click(atName("����������� �������"));
		
		sleep(2);
		
		Log.msg("��������� ������������ ��������� �� ���-�� �� ��� ���������������� ���-��(���� ��).");
	}
}

