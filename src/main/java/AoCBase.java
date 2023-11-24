import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.Logger;
import com.github.pareronia.aoc.solution.SolutionUtils;
import com.github.pareronia.aocd.Puzzle;

public abstract class AoCBase {

    protected final Logger logger;
	protected final boolean debug;
	protected boolean trace;
	
	protected static Puzzle puzzle(final Class<? extends AoCBase> klass) {
	    return SolutionUtils.puzzle(klass);
	}
	
	protected static List<String> splitLines(final String input) {
		return StringOps.splitLines(input);
	}
	
	protected static List<List<String>> toBlocks(final List<String> inputs) {
	    return StringOps.toBlocks(inputs);
	}
    
    protected static String printDuration(final Duration duration) {
        return SolutionUtils.printDuration(duration);
    }

	protected static <V> V lap(final String prefix, final Callable<V> callable) throws Exception {
	    return SolutionUtils.lap(prefix, callable);
	}

	protected AoCBase(final boolean debug) {
		this.debug = debug;
		this.logger = new Logger(debug);
	}

	public Object solvePart1() {
		return 0L;
	}
	
	public Object solvePart2() {
		return 0L;
	}
	
	protected void setTrace(final boolean trace) {
	    this.logger.setTrace(trace);
	}
	
	protected void log(final Object obj) {
	    this.logger.log(obj);
	}

	protected void trace(final Object obj) {
	    this.logger.trace(obj);
	}

	protected void log(final Supplier<Object> supplier) {
	    this.logger.log(supplier);
	}

	protected void trace(final Supplier<Object> supplier) {
	    this.logger.trace(supplier);
	}
}