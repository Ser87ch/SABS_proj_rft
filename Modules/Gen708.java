package Modules;
import resources.Modules.Gen708Helper;
import ru.sabstest.GenerateFromED744List;
import ru.sabstest.Pack;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;

public class Gen708 extends Gen708Helper
{
	
	public void testMain(Object[] args) 
	{
		//List<String> st = Arrays.asList((String[]) args[0]);
		String num = (String) args[1];
		
		ReadEDList rl = new ReadEDList();
		rl.readFolderByType(Settings.path + "post\\kPuO\\","ED744_VER");	
		
		GenerateFromED744List pl = new GenerateFromED744List();
		pl.generateFromReadEDList(rl);
		callScript("SABS.CreateSignedXML",new Object[]{Settings.path + "post\\kPuI\\", pl});
		Pack.copyESIS(num);
	}
}

