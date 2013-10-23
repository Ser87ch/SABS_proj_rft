package Modules;
import resources.Modules.Gen274Helper;
import ru.sabstest.ED244;
import ru.sabstest.ED274;
import ru.sabstest.GenerateFromED273List;
import ru.sabstest.GenerateFromXMLList;
import ru.sabstest.Pack;
import ru.sabstest.Settings;

public class Gen274 extends Gen274Helper
{
	
	public void testMain(Object[] args) 
	{
		//List<String> st = Arrays.asList((String[]) args[0]);
		String num = (String) args[1];
		
		ED274.EPDNoList.readXML(Settings.testProj + "settings\\generation\\" + num + ".xml");
				
		GenerateFromXMLList rl = new GenerateFromXMLList();
		rl.generateFromXML(Settings.testProj + "settings\\generation\\" + num + ".xml");
		
		GenerateFromED273List pl = new GenerateFromED273List();
		pl.generateFromGenerateFromXML(rl);
		callScript("SABS.CreateSignedXML",new Object[]{Settings.path + "post\\kuFI\\", pl});
		Pack.copyESIS(num);
	}
}

