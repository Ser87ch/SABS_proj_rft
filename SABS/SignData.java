package SABS;
import java.io.File;

import resources.SABS.SignDataHelper;
import ru.sabstest.Log;
import ru.sabstest.Pack;
import ru.sabstest.Settings;

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
import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;

public class SignData extends SignDataHelper
{
	
	public void testMain(Object[] args) 
	{
		try {
			Settings.testProj = TSSUtility.getScriptOption("projFolder");
			
		} catch (TSSException e) {
			e.printStackTrace();
			Log.msg(e);			
		}		
		
		if(Settings.testProj == null)
			Settings.testProj = (String) args[0];
		
		Log.createGen();
		
		Settings.readXML(Settings.testProj + "default\\general.xml", true);
		Settings.loadFromDB();
		Settings.GenSpack.readXML(Settings.testProj + "default\\gen\\genspack.xml");		
		
		File dir = new File(Settings.testProj + "data\\");
		
		String[] children = dir.list();
		String filename = children[children.length - 1];
		Settings.datafolder = Settings.testProj + "data\\" + filename + "\\";
		
		String sfilename = Pack.getSPackName();
		
		Log.msg("������� ������ �� ���������� S �������.");
		
		callScript("SABS.VFD",new String[]{Settings.GenSpack.keyobr});
		
		callScript("SABS.SignFile",new String[]{Settings.GenSpack.signobr, Settings.datafolder + "input\\003\\spack.txt", Settings.datafolder + "input\\003\\spack_1sgn.txt"});
		callScript("SABS.SignFile",new String[]{Settings.GenSpack.signobr, Settings.datafolder + "input\\004\\spack.txt", Settings.datafolder + "input\\004\\spack_1sgn.txt"});
		callScript("SABS.SignFile",new String[]{Settings.GenSpack.signobr, Settings.datafolder + "input\\005\\spack.txt", Settings.datafolder + "input\\005\\spack_1sgn.txt"});
		
		callScript("SABS.VFD",new String[]{Settings.GenSpack.keycontr});
		callScript("SABS.SignFile",new String[]{Settings.GenSpack.signcontr, Settings.datafolder + "input\\003\\spack_1sgn.txt", Settings.datafolder + "input\\003\\" + sfilename});		
		callScript("SABS.SignFile",new String[]{Settings.GenSpack.signcontr, Settings.datafolder + "input\\004\\spack_1sgn.txt", Settings.datafolder + "input\\004\\" + sfilename});		
		callScript("SABS.SignFile",new String[]{Settings.GenSpack.signcontr, Settings.datafolder + "input\\005\\spack_1sgn.txt", Settings.datafolder + "input\\005\\" + sfilename});
		
		Log.close();
	}
}

