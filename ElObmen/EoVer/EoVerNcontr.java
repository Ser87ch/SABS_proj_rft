package ElObmen.EoVer;
import resources.ElObmen.EoVer.EoVerNcontrHelper;
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
public class EoVerNcontr extends EoVerNcontrHelper
{
	/**
	 * Script Name   : <b>EoVerNcont</b>
	 * Generated     : <b>09.04.2013 17:17:14</b>
	 * Description   : Functional Test Script
	 * Original Host : WinNT Version 5.1  Build 2600 (S)
	 * 
	 * @since  2013/04/09
	 * @author Admin
	 */
	public void testMain(Object[] args) 
	{
		
		// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
		mainMenuTree().click(atName("Контроль ЭС "));

		sleep(8);
		
		// Обработка начальных ЭС
		iconPanel().click(atPoint(41,15));
		
		sleep(4);
		
		// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
		mainMenuTree().click(atName("Электронные расчеты"));
		
		Log.msg("Выполнено формирование начальных ЭС ВЭР-ПУ на АРМ контролера(тест ЭО).");
	}
}

