package SABS;
import resources.SABS.StartSABSHelper;
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
import ru.sabstest.*;

public class StartSABS extends StartSABSHelper
{

	public void testMain(Object[] args) 
	{
		try{		
			String user = (String) args[0];
			String pwd = (String) args[1];
			String sign = (String) args[2];
			
			run(Settings.path + "\\bin\\purs_loader.exe",Settings.path + "\\bin");
			
			//DM
			Log.msg("Запускается САБС. Логин: " + user + ".  Профиль: " + sign + ".");
			
			sleep(3);
			
			
			
			//DM+ \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
			/*

			Loginwindow().inputKeys(user + "{ENTER}" + pwd + "{ENTER}");
			Log.msg("Пароль введен.");
			logTestResult("Login", true); 

			SignComnfirmbutton().click();
			
			
			SigncomboBox().select(sign);
			Signokbutton().click();
			
		
			//DM??LoadSignwindow().inputKeys("{ENTER}");
			//SignNextbutton().click();	
			
			//DM??LoadSignwindow().inputKeys("{ENTER}");
			//SignDonebutton().click();
			
			Log.msg("Сигнатура проинициализирована.");

			// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
			SABSwindow().waitForExistence(25.0, 2.0);
			SABSwindow().maximize();
			
			*/

			
			
			доступКСистемеwindow().inputKeys(user + "{ENTER}" + pwd + "{ENTER}");
			sleep(1);
			
			Log.msg("Пароль введен.");
			logTestResult("Login", true);
			
			// Window: purs_loader.exe: Подтверждение
			///даbutton().click(atPoint(46,12));
			даbutton().click();
			
			sleep(1);
			
			// Window: ConvXml.exe: Выберите профиль
			comboBoxcomboBox().click(ARROW);
			///comboBoxcomboBox().click(atText("CONTRKEP"));
			comboBoxcomboBox().click(atText(sign));
			///comboBoxcomboBox().select(sign);

			sleep(1);
			
			///okbutton2().click(atPoint(55,14));
			okbutton2().click();
			
			//sleep(1);
			
			//DM
			Log.msg("САБС запущен.");
			
			//DM+ //////////////////////////////////////////
			
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}
}

