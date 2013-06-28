package Modules;
import java.util.Arrays;
import java.util.List;

import resources.Modules.FormESHelper;
import ru.sabstest.Log;
import ru.sabstest.Settings;




public class FormES extends FormESHelper
{

	public void testMain(Object[] args) 
	{
		List<String> st = Arrays.asList((String[]) args[0]);
		//String num = (String) args[1];
		
		callScript("SABS.VFD",new String[]{Settings.Login.formes.key});
		callScript("SABS.StartSABS",new String[]{Settings.Login.formes.user, Settings.Login.formes.pwd, Settings.Login.formes.sign});

		Menutree().click(atName("ќбработка Ё—"));	

		if(st.contains("Otv"))
		{
			ESpanel().click(atPoint(65,15));
			
			sleep(2.0);
			
			while(Errorwindow().exists())
			{			
				OKerrorbutton().click();			
			}			
		}
		

		if(st.contains("Nach"))
		{
			ESpanel().click(atPoint(25,15));
			
			sleep(2.0);
			
			ESpanel().click(atPoint(45,15));
			
			sleep(2.0);
			
			while(Errorwindow().exists())
			{			
				OKerrorbutton().click();			
			}			
		}

	
		if(st.contains("Vozvr"))
			;//подумать над реализацией

		Menutree().click(atName("Ёлектронные расчеты"));
		Log.msg("Ё— создано.");
		
		callScript("SABS.CloseSABS");
	}
}

