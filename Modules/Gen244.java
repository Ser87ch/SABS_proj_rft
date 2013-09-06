package Modules;
import resources.Modules.Gen244Helper;
import ru.sabstest.ED244;
import ru.sabstest.GenerateFromED243List;
import ru.sabstest.Pack;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;

public class Gen244 extends Gen244Helper
{
	
	public void testMain(Object[] args) 
	{
		//List<String> st = Arrays.asList((String[]) args[0]);
		String num = (String) args[1];
		
		
		ED244.CodeList.readXML(Settings.testProj + "settings\\generation\\" + num + ".xml");
		
		ReadEDList rl = new ReadEDList();
		rl.readFolderByType(Settings.fullfolder + "\\output\\" + num + "\\" ,"ED243");	
		
		GenerateFromED243List pl = new GenerateFromED243List();
		pl.generateFromReadEDList(rl);
		callScript("SABS.CreateSignedXML",new Object[]{Settings.path + "post\\kuFI\\", pl});
		Pack.copyESIS(num);
	}
}

