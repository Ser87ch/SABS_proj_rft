package SABS;
import resources.SABS.RunTestHelper;
import ru.sabstest.TestCase;


public class RunTest extends RunTestHelper
{
	
	public void testMain(Object[] args) 
	{
		TestCase.Step st = (TestCase.Step) args[0];
		
		callScript(st.script, st.options);
	}
}

