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
 * Script Name   : <b>ContrVvod</b><br>
 * Generated     : <b>2013/06/28 8:28:01</b><br>
 * Description   : Helper class for script<br>
 * Original Host : Windows XP x86 5.1 build 2600 Service Pack 3 <br>
 * 
 * @since  ���� 28, 2013
 * @author Admin
 */
public abstract class ContrVvodHelper extends RationalTestScript
{
	/**
	 * ��������������������������: with default state.
	 *		.text : ��������� ������������ �����
	 * 		.class : TfmDublDoclistMain
	 * 		.processName : purs_loader.exe
	 * 		.name : ��������� ������������ �����
	 */
	protected TopLevelSubitemTestObject ContrDocwindow() 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("ContrDocwindow"));
	}
	/**
	 * ��������������������������: with specific test context and state.
	 *		.text : ��������� ������������ �����
	 * 		.class : TfmDublDoclistMain
	 * 		.processName : purs_loader.exe
	 * 		.name : ��������� ������������ �����
	 */
	protected TopLevelSubitemTestObject ContrDocwindow(TestObject anchor, long flags) 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("ContrDocwindow"), anchor, flags);
	}
	
	/**
	 * �����������������������������������: with default state.
	 *		.text : �������� - �������� ��������� ���������� 
	 * 		.class : TfmInputform001
	 * 		.processName : purs_loader.exe
	 * 		.name : �������� - �������� ��������� ���������� 
	 */
	protected TopLevelSubitemTestObject ControlTextwindow() 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("ControlTextwindow"));
	}
	/**
	 * �����������������������������������: with specific test context and state.
	 *		.text : �������� - �������� ��������� ���������� 
	 * 		.class : TfmInputform001
	 * 		.processName : purs_loader.exe
	 * 		.name : �������� - �������� ��������� ���������� 
	 */
	protected TopLevelSubitemTestObject ControlTextwindow(TestObject anchor, long flags) 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("ControlTextwindow"), anchor, flags);
	}
	
	/**
	 * ���������������: with default state.
	 *		.text : ����������� ���� 
	 * 		.class : TfmdublcontrolInput001
	 * 		.processName : purs_loader.exe
	 * 		.name : ����������� ���� 
	 */
	protected TopLevelSubitemTestObject Inputwindow() 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("Inputwindow"));
	}
	/**
	 * ���������������: with specific test context and state.
	 *		.text : ����������� ���� 
	 * 		.class : TfmdublcontrolInput001
	 * 		.processName : purs_loader.exe
	 * 		.name : ����������� ���� 
	 */
	protected TopLevelSubitemTestObject Inputwindow(TestObject anchor, long flags) 
	{
		return new TopLevelSubitemTestObject(
                        getMappedTestObject("Inputwindow"), anchor, flags);
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
	
	

	protected ContrVvodHelper()
	{
		setScriptName("Modules.ContrVvod");
	}
	
}

