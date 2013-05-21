// DO NOT EDIT: This file is automatically generated.
//
// Only the associated template file should be edited directly.
// Helper class files are automatically regenerated from the template
// files at various times, including record actions and test object
// insertion actions.  Any changes made directly to a helper class
// file will be lost when automatically updated.

package resources.ElObmen.EoOut;

import com.rational.test.ft.object.interfaces.*;
import com.rational.test.ft.object.interfaces.SAP.*;
import com.rational.test.ft.object.interfaces.WPF.*;
import com.rational.test.ft.object.interfaces.siebel.*;
import com.rational.test.ft.object.interfaces.flex.*;
import com.rational.test.ft.object.interfaces.dojo.*;
import com.rational.test.ft.object.interfaces.generichtmlsubdomain.*;
import com.rational.test.ft.script.*;
import com.rational.test.ft.vp.IFtVerificationPoint;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;
/**
 * Script Name   : <b>EoOutContr</b><br>
 * Generated     : <b>2013/04/17 18:50:34</b><br>
 * Description   : Helper class for script<br>
 * Original Host : Windows XP x86 5.1 build 2600 Service Pack 3 <br>
 * 
 * @since  ������ 17, 2013
 * @author Admin
 */
public abstract class EoOutContrHelper extends RationalTestScript
{
	/**
	 * ���������������: with default state.
	 *		.text : ��������� ������
	 * 		.class : TdxBarControl
	 * 		.name : ��������� ������
	 * 		.classIndex : 0
	 */
	protected GuiSubitemTestObject IconPanel() 
	{
		return new GuiSubitemTestObject(
                        getMappedTestObject("IconPanel"));
	}
	/**
	 * ���������������: with specific test context and state.
	 *		.text : ��������� ������
	 * 		.class : TdxBarControl
	 * 		.name : ��������� ������
	 * 		.classIndex : 0
	 */
	protected GuiSubitemTestObject IconPanel(TestObject anchor, long flags) 
	{
		return new GuiSubitemTestObject(
                        getMappedTestObject("IconPanel"), anchor, flags);
	}
	
	/**
	 * TdxBarSubMenuControl: with default state.
	 *		.text : null
	 * 		.class : TdxBarSubMenuControl
	 * 		.processName : purs_loader.exe
	 * 		.name : null
	 */
	protected TopLevelSubitemTestObject fddMenu() 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("fddMenu"));
	}
	/**
	 * TdxBarSubMenuControl: with specific test context and state.
	 *		.text : null
	 * 		.class : TdxBarSubMenuControl
	 * 		.processName : purs_loader.exe
	 * 		.name : null
	 */
	protected TopLevelSubitemTestObject fddMenu(TestObject anchor, long flags) 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("fddMenu"), anchor, flags);
	}
	
	/**
	 * Outline: with default state.
	 *		.class : .Outline
	 * 		.name : null
	 * 		.classIndex : 0
	 */
	protected GuiSubitemTestObject mainMenuTree() 
	{
		return new GuiSubitemTestObject(
                        getMappedTestObject("mainMenuTree"));
	}
	/**
	 * Outline: with specific test context and state.
	 *		.class : .Outline
	 * 		.name : null
	 * 		.classIndex : 0
	 */
	protected GuiSubitemTestObject mainMenuTree(TestObject anchor, long flags) 
	{
		return new GuiSubitemTestObject(
                        getMappedTestObject("mainMenuTree"), anchor, flags);
	}
	
	/**
	 * ��: with default state.
	 *		.text : ��
	 * 		.class : .Pushbutton
	 * 		.name : ��
	 * 		.classIndex : 0
	 */
	protected GuiTestObject ��button() 
	{
		return new GuiTestObject(
                        getMappedTestObject("��button"));
	}
	/**
	 * ��: with specific test context and state.
	 *		.text : ��
	 * 		.class : .Pushbutton
	 * 		.name : ��
	 * 		.classIndex : 0
	 */
	protected GuiTestObject ��button(TestObject anchor, long flags) 
	{
		return new GuiTestObject(
                        getMappedTestObject("��button"), anchor, flags);
	}
	
	

	protected EoOutContrHelper()
	{
		setScriptName("ElObmen.EoOut.EoOutContr");
	}
	
}
