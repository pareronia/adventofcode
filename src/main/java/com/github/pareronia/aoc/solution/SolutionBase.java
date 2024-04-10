package com.github.pareronia.aoc.solution;

import static com.github.pareronia.aoc.solution.SolutionUtils.lap;
import static com.github.pareronia.aoc.solution.SolutionUtils.printDuration;
import static com.github.pareronia.aoc.solution.SolutionUtils.puzzle;
import static com.github.pareronia.aoc.solution.SolutionUtils.runSamples;

import java.util.List;
import java.util.function.Supplier;

import com.github.pareronia.aocd.Puzzle;
import com.github.pareronia.aocd.SystemUtils;

public abstract class SolutionBase<Input, Output1, Output2> {

    protected final boolean debug;
    protected final Puzzle puzzle;
    protected final Logger logger;
    protected final SystemUtils systemUtils;
    
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
        final Input input = timed.getResult();
        System.out.println(String.format("Input took %s",
                                   printDuration(timed.getDuration())));
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
        return puzzle.getInputData();
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
