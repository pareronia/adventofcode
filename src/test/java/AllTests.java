import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.pareronia.aocd.PuzzleTestCase;
import com.github.pareronia.aocd.RunnerTestCase;
import com.github.pareronia.aocd.UserTestCase;

@RunWith(Suite.class)
@SuiteClasses({
	AoCBaseTestCase.class,
	FigthTestCase.class,
	NessieFinderTestCase.class,
	TileMatcherTestCase.class,
	TilePermutatorTestCase.class,
	PuzzleTestCase.class,
	RunnerTestCase.class,
	UserTestCase.class,
})
public class AllTests {

}
