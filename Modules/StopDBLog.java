package Modules;

import resources.Modules.StopDBLogHelper;
import ru.sabstest.DeltaDB;

public class StopDBLog extends StopDBLogHelper {

    public void testMain(Object[] args) {
	// List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	DeltaDB.createXML(num);
	DeltaDB.deleteDBLog();
    }
}
