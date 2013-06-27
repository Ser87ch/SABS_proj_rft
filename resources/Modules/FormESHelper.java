// DO NOT EDIT: This file is automatically generated.
//
// Only the associated template file should be edited directly.
// Helper class files are automatically regenerated from the template
// files at various times, including record actions and test object
// insertion actions.  Any changes made directly to a helper class
// file will be lost when automatically updated.

package resources.Modules;

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
 * Script Name   : <b>FormES</b><br>
 * Generated     : <b>2013/06/27 11:47:37</b><br>
 * Description   : Helper class for script<br>
 * Original Host : Windows XP x86 5.1 build 2600 Service Pack 3 <br>
 * 
 * @since  ���� 27, 2013
 * @author Admin
 */
public abstract class FormESHelper extends RationalTestScript
{
	/**
	 * ���������������: with default state.
	 *		.text : ��������� ������
	 * 		.class : TdxBarControl
	 * 		.name : ��������� ������
	 * 		.classIndex : 0
	 */
	protected GuiSubitemTestObject ESpanel() 
	{
		return new GuiSubitemTestObject(
                        getMappedTestObject("ESpanel"));
	}
	/**
	 * ���������������: with specific test context and state.
	 *		.text : ��������� ������
	 * 		.class : TdxBarControl
	 * 		.name : ��������� ������
	 * 		.classIndex : 0
	 */
	protected GuiSubitemTestObject ESpanel(TestObject anchor, long flags) 
	{
		return new GuiSubitemTestObject(
                        getMappedTestObject("ESpanel"), anchor, flags);
	}
	
	/**
	 * ������: with default state.
	 *		.text : ������
	 * 		.class : #32770
	 * 		.processName : purs_loader.exe
	 * 		.name : ������
	 */
	protected TopLevelSubitemTestObject Errorwindow() 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("Errorwindow"));
	}
	/**
	 * ������: with specific test context and state.
	 *		.text : ������
	 * 		.class : #32770
	 * 		.processName : purs_loader.exe
	 * 		.name : ������
	 */
	protected TopLevelSubitemTestObject Errorwindow(TestObject anchor, long flags) 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("Errorwindow"), anchor, flags);
	}
	
	/**
	 * Outline: with default state.
	 *		.class : .Outline
	 * 		.name : null
	 * 		.classIndex : 0
	 */
	protected GuiSubitemTestObject Menutree() 
	{
		return new GuiSubitemTestObject(
                        getMappedTestObject("Menutree"));
	}
	/**
	 * Outline: with specific test context and state.
	 *		.class : .Outline
	 * 		.name : null
	 * 		.classIndex : 0
	 */
	protected GuiSubitemTestObject Menutree(TestObject anchor, long flags) 
	{
		return new GuiSubitemTestObject(
                        getMappedTestObject("Menutree"), anchor, flags);
	}
	
	/**
	 * ��: with default state.
	 *		.text : ��
	 * 		.class : .Pushbutton
	 * 		.name : ��
	 * 		.classIndex : 0
	 */
	protected GuiTestObject OKerrorbutton() 
	{
		return new GuiTestObject(
                        getMappedTestObject("OKerrorbutton"));
	}
	/**
	 * ��: with specific test context and state.
	 *		.text : ��
	 * 		.class : .Pushbutton
	 * 		.name : ��
	 * 		.classIndex : 0
	 */
	protected GuiTestObject OKerrorbutton(TestObject anchor, long flags) 
	{
		return new GuiTestObject(
                        getMappedTestObject("OKerrorbutton"), anchor, flags);
	}
	
	/**
	 * _044582002���������������������: with default state.
	 *		.text : 044582002 �� ����� ������ ��������
	 * 		.class : TpursMainForm
	 * 		.processName : purs_loader.exe
	 * 		.name : 044582002 �� ����� ������ ��������
	 */
	protected TopLevelSubitemTestObject SABSwindow() 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("SABSwindow"));
	}
	/**
	 * _044582002���������������������: with specific test context and state.
	 *		.text : 044582002 �� ����� ������ ��������
	 * 		.class : TpursMainForm
	 * 		.processName : purs_loader.exe
	 * 		.name : 044582002 �� ����� ������ ��������
	 */
	protected TopLevelSubitemTestObject SABSwindow(TestObject anchor, long flags) 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("SABSwindow"), anchor, flags);
	}
	
	

	protected FormESHelper()
	{
		setScriptName("Modules.FormES");
	}
	
}
