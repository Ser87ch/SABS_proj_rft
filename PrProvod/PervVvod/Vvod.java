package PrProvod.PervVvod;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ListIterator;

import resources.PrProvod.PervVvod.VvodHelper;
import ru.sabstest.*;

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

public class Vvod extends VvodHelper
{

	public void testMain(Object[] args) 
	{


		Menutree().click(atName("Ввод документов"));
		SABSwindow().inputKeys("{TAB}");
		SABSwindow().inputKeys("{ExtDown}{ExtDown}{ENTER}");

		PaymentDocumentList pl = (PaymentDocumentList) args[0]; 

		Docswindow().inputKeys("{F2}");	
	
		

		for(int i = 0; i < pl.size(); i++)
		{
			PaymentDocument pd = pl.get(i);
			Inputwindow().inputKeys(pd.toStr("{ENTER}",true));
			//Inputwindow().inputKeys("{ENTER}{ENTER}{ENTER}");

//			if(ExistDocwindow().exists())
//			{
//				ExistDocOkbutton().click();
//				Log.msg("Похожий документ уже есть в системе.");
//			}

			
			if(Errorwindow().exists())
			{
				ErrorOkbutton().click();				
			}

			ProvodNextbutton().click();
			Log.msg("Документ №" + Integer.toString(i + 1) + " введен в САБС.");
		}	
		Inputwindow().inputKeys("{ESCAPE}");
		Docswindow().inputKeys("{TAB}{ExtDown}");
		
		for(int i = 0; i < pl.size(); i++)
			Docswindow().inputKeys("+{ExtDown}");
				
		Docswindow().inputKeys("{F7}");				
		
		Folderwindow().inputKeys("{ExtDown}{ExtDown}{ExtDown}{ExtDown}{ExtDown}{ExtDown}{ExtDown}{ExtDown}");
		
		
		PrintcheckBox().click();		
		FolderOkbutton().click();
		ExistDocOkbutton().click();
			
		Log.msg("Пачка создана.");
		
		Docswindow().inputKeys("%{F4}");
		Menutree().click(atName("Первичный ввод документов"));			


	}
}

