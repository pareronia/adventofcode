import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({ "" })
@IncludeClassNamePatterns(".*TestCase")
public class AllUnitTests {

}
