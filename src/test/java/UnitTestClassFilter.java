import org.junit.extensions.dynamicsuite.TestClassFilter;

public class UnitTestClassFilter implements TestClassFilter {
    @Override
    public boolean include(String className) {
        return className.endsWith("TestCase");
    }

    @SuppressWarnings("rawtypes")
	@Override
    public boolean include(Class cls) {
    	return include(cls.getSimpleName());
    }
}