// DO NOT EDIT: This file is automatically generated.
//
// Only the associated template file should be edited directly.
// Helper class files are automatically regenerated from the template
// files at various times, including record actions and test object
// insertion actions.  Any changes made directly to a helper class
// file will be lost when automatically updated.

package resources.ObrProvod.Vvod;

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
 * Script Name   : <b>ContrRpack</b><br>
 * Generated     : <b>2012/07/10 13:25:24</b><br>
 * Description   : Helper class for script<br>
 * Original Host : Windows XP x86 5.1 build 2600 Service Pack 3 <br>
 * 
 * @since  ���� 10, 2012
 * @author Admin
 */
public abstract class ContrRpackHelper extends RationalTestScript
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
	
	

	protected ContrRpackHelper()
	{
		setScriptName("ObrProvod.Vvod.ContrRpack");
	}
	
}

