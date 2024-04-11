package com.github.pareronia.aoc.solution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimedTestCase {
    
    @SuppressWarnings("unchecked")
    private final Supplier<Long> nanoTimeSupplier = mock(Supplier.class);
    
    @BeforeEach
    void setUp() {
    }

    @Test
    void test() throws Exception {
        when(nanoTimeSupplier.get()).thenReturn(1000L, 2000L);
        
        final Timed<String> timed = Timed.timed(() -> "abc", nanoTimeSupplier);
        
        assertThat(timed.result()).isEqualTo("abc");
        assertThat(timed.duration().toNanos()).isEqualTo(1000L);
    }
}
