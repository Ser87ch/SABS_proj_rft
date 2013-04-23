package ElObmen.EoIn;
import resources.ElObmen.EoIn.EoInOtvetHelper;
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
public class EoInOtvet extends EoInOtvetHelper
{
	/**
	 * Script Name   : <b>EoInOtvet</b>
	 * Generated     : <b>01.04.2013 12:17:43</b>
	 * Description   : Functional Test Script
	 * Original Host : WinNT Version 5.1  Build 2600 (S)
	 * 
	 * @since  2013/04/01
	 * @author Admin
	 */
	public void testMain(Object[] args) 
	{
		
		// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
		// выбор в главном меню
		mainMenuTree().click(atName("Обработка ЭС"));
		
		sleep(8);
		
		// "обработать"
		iconPanel().click(atPoint(60,9));
		
		sleep(8);
		
		// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
		mainMenuTree().click(atName("Электронные расчеты"));
		
		Log.msg("Выполнена обработка ответных пакетов ЭС УФЭБС на АРМ ответисполнителя(тест ЭО).");
		
	}
}

