import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AoC2021_23TestCase {

    private static final AoC2021_23.Amphipod A = AoC2021_23.Amphipod.A;
    private static final AoC2021_23.Amphipod B = AoC2021_23.Amphipod.B;
    private static final AoC2021_23.Amphipod C = AoC2021_23.Amphipod.C;
    private static final AoC2021_23.Amphipod D = AoC2021_23.Amphipod.D;
    private static final AoC2021_23.Amphipod EMPTY = AoC2021_23.Amphipod.EMPTY;

    @Test
    public void movesInitial() {
        final AoC2021_23.Amphipod[] hallway = new AoC2021_23.Amphipod[11];
        Arrays.fill(hallway, EMPTY);
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
            hallway,
            new AoC2021_23.Amphipod[] { B, B },
            new AoC2021_23.Amphipod[] { C, C },
            new AoC2021_23.Amphipod[] { D, A },
            new AoC2021_23.Amphipod[] { A, D });
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves).hasSize(28);
    }

    @Test
    public void movesInitial_4() {
        final AoC2021_23.Amphipod[] hallway = new AoC2021_23.Amphipod[11];
        Arrays.fill(hallway, EMPTY);
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                hallway,
                new AoC2021_23.Amphipod[] { B, D, D, B },
                new AoC2021_23.Amphipod[] { C, B, C, C },
                new AoC2021_23.Amphipod[] { D, A, B, A },
                new AoC2021_23.Amphipod[] { A, C, A, D });
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves).hasSize(28);
    }

    @Test
    public void movesEnd() {
        final AoC2021_23.Amphipod[] hallway = new AoC2021_23.Amphipod[11];
        Arrays.fill(hallway, EMPTY);
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                hallway,
                new AoC2021_23.Amphipod[] { A, A },
                new AoC2021_23.Amphipod[] { B, B },
                new AoC2021_23.Amphipod[] { C, C },
                new AoC2021_23.Amphipod[] { D, D });
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves).isEmpty();
    }

    @Test
    public void movesEnd_4() {
        final AoC2021_23.Amphipod[] hallway = new AoC2021_23.Amphipod[11];
        Arrays.fill(hallway, EMPTY);
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                hallway,
                new AoC2021_23.Amphipod[] { A, A, A, A },
                new AoC2021_23.Amphipod[] { B, B, B, B },
                new AoC2021_23.Amphipod[] { C, C, C, C },
                new AoC2021_23.Amphipod[] { D, D, D, D });
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves).isEmpty();
    }
    
    @Test
    public void moves1() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                // ...B.....B.
                //  |C|A|.|.|
                //  |D|A|C|D|
                new AoC2021_23.Amphipod[] { EMPTY, EMPTY, EMPTY, B, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, B, EMPTY },
                new AoC2021_23.Amphipod[] { D, C },
                new AoC2021_23.Amphipod[] { A, A },
                new AoC2021_23.Amphipod[] { C, EMPTY },
                new AoC2021_23.Amphipod[] { D, EMPTY });
        assert diagram.getHallway().getAmphipods().length == 11;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves).hasSize(4);
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveFromHallway)).isEmpty();
    }
    
    @Test
    public void moves2() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                new AoC2021_23.Amphipod[] { A, A, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
                new AoC2021_23.Amphipod[] { EMPTY, EMPTY },
                new AoC2021_23.Amphipod[] { B, B },
                new AoC2021_23.Amphipod[] { C, C },
                new AoC2021_23.Amphipod[] { D, D });
        assert diagram.getHallway().getAmphipods().length == 11;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveToHallway)).isEmpty();
        assertThat(moves).hasSize(1);
    }

    @Test
    public void moves3() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                // A..........
                //  |.|B|C|D|
                //  |A|B|C|D|
                new AoC2021_23.Amphipod[] { A, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
                new AoC2021_23.Amphipod[] { A, EMPTY },
                new AoC2021_23.Amphipod[] { B, B },
                new AoC2021_23.Amphipod[] { C, C },
                new AoC2021_23.Amphipod[] { D, D });
        assert diagram.getHallway().getAmphipods().length == 11;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveToHallway)).isEmpty();
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveFromHallway)).hasSize(1);
        assertThat(moves).hasSize(1);
    }
    
    @Test
    public void moves4() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                new AoC2021_23.Amphipod[] { C, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
                new AoC2021_23.Amphipod[] { D, EMPTY },
                new AoC2021_23.Amphipod[] { A, A },
                new AoC2021_23.Amphipod[] { C, B },
                new AoC2021_23.Amphipod[] { D, B });
        assert diagram.getHallway().getAmphipods().length == 11;
        assert diagram.freeLeftFromA().size() == 1;
        assert diagram.freeRightFromA().size() == 5;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves).hasSize(24);
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveFromHallway)).isEmpty();
    }

    @Test
    public void moves4_4() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                new AoC2021_23.Amphipod[] { B, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
                new AoC2021_23.Amphipod[] { B, D, D, EMPTY },
                new AoC2021_23.Amphipod[] { C, B, C, C },
                new AoC2021_23.Amphipod[] { D, A, B, A },
                new AoC2021_23.Amphipod[] { A, C, A, D });
        assert diagram.getHallway().getAmphipods().length == 11;
        assert diagram.freeLeftFromA().size() == 1;
        assert diagram.freeRightFromA().size() == 5;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveFromHallway)).isEmpty();
        assertThat(moves).hasSize(24);
    }

    @Test
    public void moves5_4() {
        final AoC2021_23.Diagram diagram = new AoC2021_23.Diagram(
                new AoC2021_23.Amphipod[] { B, D, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
                new AoC2021_23.Amphipod[] { B, D, EMPTY, EMPTY },
                new AoC2021_23.Amphipod[] { C, B, C, C },
                new AoC2021_23.Amphipod[] { D, A, B, A },
                new AoC2021_23.Amphipod[] { A, C, A, D });
        assert diagram.getHallway().getAmphipods().length == 11;
        assert diagram.freeLeftFromA().size() == 0;
        assert diagram.freeRightFromA().size() == 5;
        
        final List<AoC2021_23.Move> moves = diagram.moves();
        assertThat(moves.stream().filter(m -> m instanceof AoC2021_23.MoveFromHallway)).isEmpty();
        assertThat(moves).hasSize(20);
    }
    
    @Test
    public void roomVacancy1() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                A,
                2,
                new AoC2021_23.Amphipod[] { A, EMPTY });
        
        final int ans = room.vacancyFor(A);
        
        assertThat(ans).isEqualTo(1);
    }

    @Test
    public void roomVacancy1_4() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                A,
                4,
                new AoC2021_23.Amphipod[] {
                        A, A,
                        EMPTY, EMPTY});
        
        final int ans = room.vacancyFor(A);
        
        assertThat(ans).isEqualTo(2);
    }

    @Test
    public void roomVacancy2() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                A,
                2,
                new AoC2021_23.Amphipod[] { B, EMPTY });
        
        final int ans = room.vacancyFor(A);
        
        assertThat(ans).isEqualTo(-1);
    }

    @Test
    public void roomVacancy2_4() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                A,
                4,
                new AoC2021_23.Amphipod[] {
                        A, A,
                        B, EMPTY});
        
        final int ans = room.vacancyFor(A);
        
        assertThat(ans).isEqualTo(-1);
    }
    
    @Test
    public void roomVacancy3_4() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                A,
                4,
                new AoC2021_23.Amphipod[] {
                        A, A,
                        EMPTY, EMPTY});
        
        final int ans = room.vacancyFor(A);
        
        assertThat(ans).isEqualTo(2);
    }
    
    @Test
    public void availableToMove() {
        final AoC2021_23.Room room = new AoC2021_23.Room(
                A,
                4,
                new AoC2021_23.Amphipod[] {
                        A, A,
                        B, EMPTY});
        
        final int ans = room.availableToMove();
        
        assertThat(ans).isEqualTo(2);
    }
}
