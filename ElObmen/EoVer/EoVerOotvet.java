package ElObmen.EoVer;
import resources.ElObmen.EoVer.EoVerOotvetHelper;
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
public class EoVerOotvet extends EoVerOotvetHelper
{
	/**
	 * Script Name   : <b>EoVerOotvet</b>
	 * Generated     : <b>09.04.2013 17:18:16</b>
	 * Description   : Functional Test Script
	 * Original Host : WinNT Version 5.1  Build 2600 (S)
	 * 
	 * @since  2013/04/09
	 * @author Admin
	 */
	public void testMain(Object[] args) 
	{

		//boolean isDebugOotvet = true  ;
		boolean isDebugOotvet = false ;
		
		if(isDebugOotvet == false)
		{
			int qq = 1;
			qq = qq + 2;
		
			// Window: purs_loader.exe: 044582002 �� ����� ������ ��������
			mainMemuTree().click(atName("��������� ��"));
		
			sleep(8);
		
			// ��������� ��������
			IconPanelObr().click(atPoint(67,12));
		
			sleep(4);
		

			mainMemuTree().click(atName("������ � ������������� �����������"));

			sleep(4);
		
			// ����� ��������� � �������!!!???
			tcxTreeListwindow().click(atPoint(152,119));
		
			// �������
			IconPanelErr().click(atPoint(390,8));
		
			sleep(2);
			
			// Window: purs_loader.exe: �������� ���������
			����������������button().click(atPoint(80,13));
		
			sleep(2);
			

			// Window: purs_loader.exe: 044582002 �� ����� ������ ��������
			mainMemuTree().click(atName("��������� ��"));

			sleep(8);
		
			IconPanelObr().click(atPoint(42,13));
		
		
			// Window: purs_loader.exe: 044582002 �� ����� ������ ��������
			mainMemuTree().click(atName("����������� �������"));
			
		}
		else
		{
			
			// Window: purs_loader.exe: 044582002 �� ����� ������ ��������
			mainMemuTree().click(atName("��������� ��"));
		
			sleep(8);
		
			// ���������� ��������
			IconPanelObr().click(atPoint(64,11));
		
			sleep(4);
			
			
			//
			mainMemuTree().click(atName("������ � ������������� �����������"));

			sleep(4);
		
			// ����� ������� ������� !!! ???
			tcxTreeListwindow().click(atPoint(129,115));
		
			// �������
			IconPanelErr().click(atPoint(387,12));
		
			sleep(2);
		
			// Window: purs_loader.exe: �������� ���������
			����������������button().click(atPoint(75,13));
		
			sleep(2);
			

			// Window: purs_loader.exe: 044582002 �� ����� ������ ��������
			mainMemuTree().click(atName("��������� ��"));
		
			sleep(8);
		
			// ������������ ���������
			IconPanelObr().click(atPoint(40,8));
		
			sleep(4);
			
			mainMemuTree().click(atName("����������� �������"));

		}

		Log.msg("�������� ��������� �������� � ������������ ��������� �� ���-�� � �� �� ��� ����������������(���� ��).");	


	}
}

