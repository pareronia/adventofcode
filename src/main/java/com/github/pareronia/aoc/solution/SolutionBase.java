package com.github.pareronia.aoc.solution;

import static com.github.pareronia.aoc.solution.SolutionUtils.lap;
import static com.github.pareronia.aoc.solution.SolutionUtils.printDuration;
import static com.github.pareronia.aoc.solution.SolutionUtils.puzzle;
import static com.github.pareronia.aoc.solution.SolutionUtils.runSamples;

import java.util.List;

import com.github.pareronia.aocd.Puzzle;
import com.github.pareronia.aocd.SystemUtils;

public abstract class SolutionBase<Input, Output1, Output2> implements LoggerEnabled {

    protected final boolean debug;
    protected final Puzzle puzzle;
    protected final Logger logger;
    protected final SystemUtils systemUtils;
	protected boolean trace;
    
    protected SolutionBase(final boolean debug) {
        this.debug = debug;
        this.logger = new Logger(debug);
        this.puzzle = puzzle(this.getClass());
        this.systemUtils = new SystemUtils();
    }

    protected abstract Input parseInput(List<String> inputs);
    
    protected abstract Output1 solvePart1(Input input);
    
    protected abstract Output2 solvePart2(Input input);
    
    protected void samples() {}
    
    protected void run() throws Exception {
        runSamples(this.getClass());
        
        this.samples();
        
        final Timed<Input> timed = Timed.timed(
               () -> this.parseInput(this.getInputData()),
               systemUtils::getSystemNanoTime);
        final Input input = timed.result();
        System.out.println(String.format("Input took %s",
                                   printDuration(timed.duration())));
        puzzle.check(
           () -> lap("Part 1", () -> this.solvePart1(input)),
           () -> lap("Part 2", () -> this.solvePart2(input))
        );
    }

    public Output1 part1(final List<String> inputs) {
        return solvePart1(this.parseInput(inputs));
    }
    
    public Output2 part2(final List<String> inputs) {
        return solvePart2(this.parseInput(inputs));
    }
    
    protected List<String> getInputData() {
        return puzzle.inputData();
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }
	
	protected void setTrace(final boolean trace) {
	    this.trace = true;
	    this.logger.setTrace(trace);
	}
}
