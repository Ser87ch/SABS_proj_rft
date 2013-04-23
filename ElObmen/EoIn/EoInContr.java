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

		// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
		// Выбор пункта меню 
		mainMenuTree().click(atName("Контроль ЭС (УФЭБС)"));
		
		sleep(2);
		
		// выбор иконки "работа с дискетой"
		IconPanel().click(atPoint(191,12));
		
		// Window: purs_loader.exe:
		// выбор пункта "Копировать с дискеты" в меню работы с дискетой
		WorkWithFddMenu().click(atPoint(38,11));
		
		sleep(2);
		
		// Window: purs_loader.exe: Внимание
		// Копирование завершено 
		окbutton().click(atPoint(30,12));
		
		// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
		// "Обработать ответные"
		IconPanel().click(atPoint(63,16));

		sleep(2);
		
		// Window: purs_loader.exe: 044582002 ПУ БАНКА РОССИИ ЗАПАДНОЕ
		mainMenuTree().click(atName("Электронные расчеты"));
		
		Log.msg("Выполнена обработка ответных пакетов ЭС ВЭР-ПУ на АРМ контролера(тест ЭО).");
	}
}

