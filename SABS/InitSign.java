package SABS;

import java.io.File;

import resources.SABS.InitSignHelper;
import ru.sabstest.Log;
import ru.sabstest.Settings;

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
import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;




/**
 * Description   : Functional Test Script
 * @author Admin
 */
public class InitSign extends InitSignHelper
{
	
	public void testMain(Object[] args) 
	{
	
		// ��� ������� � ������ ������� 
		String signProfile = "";
		String signProfImg = "";
				
		if(args.length>0)
			signProfile = (String) args[0];
		else
			signProfile = "contrkep";
		
		signProfImg = signProfile+".fdd";
		signProfile = signProfile.toUpperCase();
		
		
		
		callScript("SABS.VFD",new String[]{signProfImg});
		sleep(1);
		
		run( "C:\\Program Files\\MDPREI\\spki\\scs.exe",  "C:\\Program Files\\MDPREI\\spki"   );

		// Window: scs.exe: �������� �������
		comboBoxcomboBox().click(ARROW);
		comboBoxcomboBox().click(atText("CONTRKEP"));
		okbutton().click(atPoint(57,11));
		
		// Window: scs.exe: ������� ���� ��� ������������� ���
		// ????
		// ��� �������� �������
		int ew = 0;

		TestObject[] win = find(atProperty(".name","������� ���� ��� ������������� ���"));
		ew = win.length;
		
		if (ew>0)
		{
			//try {

				// Window: scs.exe: ������������� ������� ��������� �����
			
				double nWait = 0.01 ;

				// ???
				list1table().click(atCell(atRow(atText("9632PFYIBN01")), 
		                            atColumn(atText("����� ������"))));

				okbutton2().click(atPoint(33,9));
				
				// Window: scs.exe: ������������� ���
				//����������������(ANY,MAY_EXIT).inputChars("1212121212121212121212121212121212121212");
				for(int i = 1;  i<50;  i++)
				{
					TestObject[] wbd1 = find(atProperty(".name","������������� ���"));
					ew = wbd1.length;
					if (ew>0)
					{
						����������������window().inputChars("1");
						sleep(nWait);
					}	
					else
						break ;

					TestObject[] wbd2 = find(atProperty(".name","������������� ���"));
					ew = wbd2.length;
					if (ew>0)
					{
						����������������window().inputChars("2");
						sleep(nWait);
					}	
					else
						break ;

					
				}
				
			//} catch (Exception e) {
				//e.printStackTrace();
				//Log.msg(e);			
			//}

			//sleep(3);
		}	
		
		// Window: scs.exe: ���������� ������������
		okbutton3().click(atPoint(47,12));
		
		// Window: scs.exe: ���������� ������������
		����������������������window(ANY,MAY_EXIT).click(CLOSE_BUTTON);
		
		// Window: scs.exe: ���������� ������������
		��button(ANY,MAY_EXIT).click(atPoint(40,11));
		
	}
}

