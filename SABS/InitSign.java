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
	
		// имя профиля и имиджа дискеты 
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

		// Window: scs.exe: Выберите профиль
		comboBoxcomboBox().click(ARROW);
		comboBoxcomboBox().click(atText("CONTRKEP"));
		okbutton().click(atPoint(57,11));
		
		// Window: scs.exe: Укажите ключ для инициализации ДСЧ
		// ????
		// для удобства отладки
		int ew = 0;

		TestObject[] win = find(atProperty(".name","Укажите ключ для инициализации ДСЧ"));
		ew = win.length;
		
		if (ew>0)
		{
			//try {

				// Window: scs.exe: Инициализация датчика случайных чисел
			
				double nWait = 0.01 ;

				// ???
				list1table().click(atCell(atRow(atText("9632PFYIBN01")), 
		                            atColumn(atText("Имена ключей"))));

				okbutton2().click(atPoint(33,9));
				
				// Window: scs.exe: Биологический ДСЧ
				//биологическийДСЧ(ANY,MAY_EXIT).inputChars("1212121212121212121212121212121212121212");
				for(int i = 1;  i<50;  i++)
				{
					TestObject[] wbd1 = find(atProperty(".name","Биологический ДСЧ"));
					ew = wbd1.length;
					if (ew>0)
					{
						биологическийДСЧwindow().inputChars("1");
						sleep(nWait);
					}	
					else
						break ;

					TestObject[] wbd2 = find(atProperty(".name","Биологический ДСЧ"));
					ew = wbd2.length;
					if (ew>0)
					{
						биологическийДСЧwindow().inputChars("2");
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
		
		// Window: scs.exe: Справочник сертификатов
		okbutton3().click(atPoint(47,12));
		
		// Window: scs.exe: Справочник сертификатов
		справочникСертификатовwindow(ANY,MAY_EXIT).click(CLOSE_BUTTON);
		
		// Window: scs.exe: Справочник сертификатов
		даbutton(ANY,MAY_EXIT).click(atPoint(40,11));
		
	}
}

