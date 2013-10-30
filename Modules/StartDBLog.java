package Modules;

import resources.Modules.StartDBLogHelper;
import ru.sabstest.DeltaDB;

public class StartDBLog extends StartDBLogHelper {

    public void testMain(Object[] args) {
	// List<String> st = Arrays.asList((String[]) args[0]);
	// String num = (String) args[1];

	DeltaDB.createDBLog();
    }
}
