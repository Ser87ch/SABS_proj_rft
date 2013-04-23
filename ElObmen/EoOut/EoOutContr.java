package ElObmen.EoOut;
import resources.ElObmen.EoOut.EoOutContrHelper;
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
public class EoOutContr extends EoOutContrHelper
{
	/**
	 * Script Name   : <b>EoOutContr</b>
	 * Generated     : <b>01.04.2013 12:18:06</b>
	 * Description   : Functional Test Script
	 * Original Host : WinNT Version 5.1  Build 2600 (S)
	 * 
	 * @since  2013/04/01
	 * @author Admin
	 */
	public void testMain(Object[] args) 
	{
		
		// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
		mainMenuTree().click(atName("Контроль ЭС (УФЭБС)"));
		
		sleep(8);
		
		// сформировать начальные
		IconPanel().click(atPoint(41,13));
		
		sleep(4);
		
		// вызов меню работы с дискетой 
		IconPanel().click(atPoint(191,10));
		
		// Window: purs_loader.exe: 
		// копировать на дискету
		fddMenu().click(atPoint(35,38));
		
		sleep(8);
		
		// Window: purs_loader.exe: Внимание
		// копирование завершено
		окbutton().click(atPoint(46,12));
		
		// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
		mainMenuTree().click(atName("Электронные расчеты"));
		
		Log.msg("Выполнено формирование начальных ЭС ЭО на АРМ контролера(тест ЭО).");
	}
}

