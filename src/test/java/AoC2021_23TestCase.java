import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AoC2021_23TestCase {

    @Test
    public void movesInitial() {
        final AoC2021_23.Amphipod[] hallway = new AoC2021_23.Amphipod[11];
        Arrays.fill(hallway, AoC2021_23.Amphipod.EMPTY);
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
            hallway,
            new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.B },
            new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.C },
            new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.A },
            new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.D });
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.size(), is(28));
    }

    @Test
    public void movesInitial_4() {
        final AoC2021_23.Amphipod[] hallway = new AoC2021_23.Amphipod[11];
        Arrays.fill(hallway, AoC2021_23.Amphipod.EMPTY);
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                hallway,
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.B },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.C },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.A },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.D });
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.size(), is(28));
    }

    @Test
    public void movesEnd() {
        final AoC2021_23.Amphipod[] hallway = new AoC2021_23.Amphipod[11];
        Arrays.fill(hallway, AoC2021_23.Amphipod.EMPTY);
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                hallway,
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.B },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.C },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.D });
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.size(), is(0));
    }

    @Test
    public void movesEnd_4() {
        final AoC2021_23.Amphipod[] hallway = new AoC2021_23.Amphipod[11];
        Arrays.fill(hallway, AoC2021_23.Amphipod.EMPTY);
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                hallway,
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.B },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.C },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.D });
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.size(), is(0));
    }
    
    @Test
    public void moves1() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.C },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.EMPTY });
        assert diagram.getHallway().getAmphipods().length == 11;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.size(), is(8));
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveFromHallway).count(), is(0L));
    }
    
    @Test
    public void moves2() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.B },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.C },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.D });
        assert diagram.getHallway().getAmphipods().length == 11;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveToHallway).count(), is(0L));
        assertThat(moves.size(), is(1));
    }

    @Test
    public void moves3() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.B },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.C },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.D });
        assert diagram.getHallway().getAmphipods().length == 11;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveToHallway).count(), is(6L));
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveFromHallway).count(), is(1L));
        assertThat(moves.size(), is(7));
    }
    
    @Test
    public void moves4() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.B },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.B });
        assert diagram.getHallway().getAmphipods().length == 11;
        assert diagram.freeLeftFromA().size() == 1;
        assert diagram.freeRightFromA().size() == 5;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.size(), is(24));
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveFromHallway).count(), is(0L));
    }

    @Test
    public void moves4_4() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.C },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.A },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.D });
        assert diagram.getHallway().getAmphipods().length == 11;
        assert diagram.freeLeftFromA().size() == 1;
        assert diagram.freeRightFromA().size() == 5;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveFromHallway).count(), is(0L));
        assertThat(moves.size(), is(24));
    }

    @Test
    public void moves5_4() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.C },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.D, AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.A },
                new AoC2021_23.Amphipod[] { AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.C, AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.D });
        assert diagram.getHallway().getAmphipods().length == 11;
        assert diagram.freeLeftFromA().size() == 0;
        assert diagram.freeRightFromA().size() == 5;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveFromHallway).count(), is(0L));
        assertThat(moves.size(), is(20));
    }
    
    @Test
    public void roomVacancy1() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                AoC2021_23.Amphipod.A,
                2,
                new AoC2021_23.Amphipod[] {
                        AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.EMPTY });
        
        final int ans = room.vacancyFor(AoC2021_23.Amphipod.A);
        
        assertThat(ans, is(1));
    }

    @Test
    public void roomVacancy1_4() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                AoC2021_23.Amphipod.A,
                4,
                new AoC2021_23.Amphipod[] {
                        AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A,
                        AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY});
        
        final int ans = room.vacancyFor(AoC2021_23.Amphipod.A);
        
        assertThat(ans, is(2));
    }

    @Test
    public void roomVacancy2() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                AoC2021_23.Amphipod.A,
                2,
                new AoC2021_23.Amphipod[] {
                        AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.EMPTY });
        
        final int ans = room.vacancyFor(AoC2021_23.Amphipod.A);
        
        assertThat(ans, is(-1));
    }

    @Test
    public void roomVacancy2_4() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                AoC2021_23.Amphipod.A,
                4,
                new AoC2021_23.Amphipod[] {
                        AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A,
                        AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.EMPTY});
        
        final int ans = room.vacancyFor(AoC2021_23.Amphipod.A);
        
        assertThat(ans, is(-1));
    }
    
    @Test
    public void roomVacancy3_4() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                AoC2021_23.Amphipod.A,
                4,
                new AoC2021_23.Amphipod[] {
                        AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A,
                        AoC2021_23.Amphipod.EMPTY, AoC2021_23.Amphipod.EMPTY});
        
        final int ans = room.vacancyFor(AoC2021_23.Amphipod.A);
        
        assertThat(ans, is(2));
    }
    
    @Test
    public void availableToMove() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                AoC2021_23.Amphipod.A,
                4,
                new AoC2021_23.Amphipod[] {
                        AoC2021_23.Amphipod.A, AoC2021_23.Amphipod.A,
                        AoC2021_23.Amphipod.B, AoC2021_23.Amphipod.EMPTY});
        
        final int ans = room.availableToMove();
        
        assertThat(ans, is(2));
    }
}