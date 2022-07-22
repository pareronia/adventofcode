import org.junit.extensions.dynamicsuite.ClassPath;
import org.junit.extensions.dynamicsuite.Filter;
import org.junit.extensions.dynamicsuite.suite.DynamicSuite;
import org.junit.runner.RunWith;

@RunWith(DynamicSuite.class)
@Filter(UnitTestClassFilter.class)
@ClassPath
public class AllUnitTests {
}
