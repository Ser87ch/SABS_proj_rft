package ElObmen.EoOut;
import resources.ElObmen.EoOut.EoOutOtvetHelper;
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
public class EoOutOtvet extends EoOutOtvetHelper
{
	/**
	 * Script Name   : <b>EoOutOtvet</b>
	 * Generated     : <b>01.04.2013 12:17:57</b>
	 * Description   : Functional Test Script
	 * Original Host : WinNT Version 5.1  Build 2600 (S)
	 * 
	 * @since  2013/04/01
	 * @author Admin
	 */
	public void testMain(Object[] args) 
	{
		
		// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
		mainMenuTree().click(atName("Обработка ЭС"));
		
		sleep(8);
		
		// Сформировать начальные
		IconPanel().click(atPoint(44,9));
		
		sleep(4);
		
		mainMenuTree().click(atName("Электронные расчеты"));
	
		Log.msg("Выполнено формирование начальных ЭС ЭО на АРМ ответисполнителя(тест ЭО).");	
	}
}

