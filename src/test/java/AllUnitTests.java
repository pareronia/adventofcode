import org.junit.extensions.dynamicsuite.Directory;
import org.junit.extensions.dynamicsuite.Filter;
import org.junit.extensions.dynamicsuite.suite.DynamicSuite;
import org.junit.runner.RunWith;

@RunWith(DynamicSuite.class)
@Filter(UnitTestClassFilter.class)
@Directory
public class AllUnitTests {
}
