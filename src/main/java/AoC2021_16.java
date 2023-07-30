import static com.github.pareronia.aoc.CharArrayUtils.subarray;
import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.LongStream;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_16 extends AoCBase {
    
    private static final class BITS {

        @RequiredArgsConstructor
        @Getter
        @ToString
        @Builder
        public static final class Packet {
            private final Integer version;
            private final Integer typeId;
            private final Long value;
            private final List<Packet> subPackets;
        }
        
        public interface BITSEventHandler {
            default void startLiteralPacket(final long value) {
                // nop
            }
            
            default void startOperatorPacket(final int type) {
                // nop
            }
            
            default void version(final int version) {
                // nop
            }
            
            default void endLiteralPacket() {
                // nop
            }
            
            default void endOperatorPacket() {
                // nop
            }
        }
        
        @RequiredArgsConstructor
        public static class LoggingBITSHandler implements BITSEventHandler {
            private final boolean debug;

            protected void log(final Object obj) {
                if (!debug) {
                    return;
                }
                System.out.println(obj);
            }

            @Override
            public void startLiteralPacket(final long value) {
                log(String.format("startLiteralPacket[value: %d]", value));
            }
            
            @Override
            public void startOperatorPacket(final int type) {
                log(String.format("startOperatorPacket[type: %d]", type));
            }
            
            @Override
            public void version(final int version) {
                log(String.format("version: %d", version));
            }

            @Override
            public void endLiteralPacket() {
                log("endLiteralPacket");
            }

            @Override
            public void endOperatorPacket() {
                log("endOperatorPacket");
            }
        }
        
        public static class DOMBITSHandler extends LoggingBITSHandler {
            private final Deque<Packet.PacketBuilder> builderStack = new ArrayDeque<>();
            private final Deque<Packet> packetStack = new ArrayDeque<>();
            
            public DOMBITSHandler(final boolean debug) {
                super(debug);
            }

            @Override
            public void startLiteralPacket(final long value) {
                super.startLiteralPacket(value);
                final Packet.PacketBuilder builder = Packet.builder();
                builder.typeId(4);
                builder.value = value;
                this.builderStack.push(builder);
            }

            @Override
            public void startOperatorPacket(final int type) {
                super.startOperatorPacket(type);
                final Packet.PacketBuilder builder = Packet.builder();
                builder.typeId(type);
                this.builderStack.push(builder);
            }

            @Override
            public void version(final int version) {
                super.version(version);
                this.builderStack.peek().version(version);
            }
            
            private void endPacket() {
                final Packet packet = builderStack.pop().build();
                if (builderStack.peek() != null) {
                    if (builderStack.peek().subPackets == null) {
                        builderStack.peek().subPackets(new ArrayList<>(List.of(packet)));
                    } else {
                        builderStack.peek().subPackets.add(packet);
                    }
                } else {
                    packetStack.push(packet);
                }
            }

            @Override
            public void endLiteralPacket() {
                super.endLiteralPacket();
                endPacket();
            }

            @Override
            public void endOperatorPacket() {
                super.endOperatorPacket();
                endPacket();
            }
            
            public Packet getPacket() {
                assert this.builderStack.size() == 0;
                assert this.packetStack.size() == 1;
                return this.packetStack.peek();
            }
        }
        
        @RequiredArgsConstructor(staticName = "createParser")
        public static final class Parser {
            private final BITSEventHandler handler;
        
            public void parseHex(final String hexString) {
                parseBin(StringOps.hexToBin(hexString));
            }
            
            public void parseBin(final String binString) {
                parse(binString.toCharArray(), 0);
            }
            
            private int parse(final char[] binData, final int pos) {
                int i = pos;
                final int tail = binData.length - i;
                if (tail < 8) {
                    return i + tail;
                }
                final int version = binToDec(binData, i, i + 3);
                i += 3;
                final int typeId = binToDec(binData, i, i + 3);
                i += 3;
                if (typeId == 4) {
                    i = parseLiteral(binData, i);
                    handler.version(version);
                    handler.endLiteralPacket();
                } else {
                    handler.startOperatorPacket(typeId);
                    handler.version(version);
                    i = parseOperator(binData, i);
                    handler.endOperatorPacket();
                }
                return i;
            }
            
            private int parseLiteral(final char[] binData, final int pos) {
                int i = pos;
                final StringBuilder value = new StringBuilder();
                while (true) {
                    final char proceed = binData[i];
                    value.append(subarray(binData, i + 1, i + 5));
                    i += 5;
                    if (proceed == '0') {
                        break;
                    }
                }
                handler.startLiteralPacket(Long.parseLong(value.toString(), 2));
                return i;
            }

            private int parseOperator(final char[] binData, final int pos) {
                int i = pos;
                final int lengthTypeId = binData[i];
                i++;
                if (lengthTypeId == '0') {
                    final int size = binToDec(binData, i,  i + 15);
                    i += 15;
                    int cnt = 0;
                    while (true) {
                        final int ii = parse(binData, i);
                        cnt += ii - i;
                        i = ii;
                        if (cnt == size) {
                            break;
                        }
                    }
                } else if (lengthTypeId == '1') {
                    final int cnt = binToDec(binData, i, i + 11);
                    i += 11;
                    for (int j = 0; j < cnt; j++) {
                        i = parse(binData, i);
                    }
                } else {
                    throw new IllegalStateException("unexpected length_type_id");
                }
                return i;
            }

            private int binToDec(final char[] binData,
                            final int startInc, final int endExc) {
                final String s = String.valueOf(
                        subarray(binData, startInc, endExc));
                return Integer.parseInt(s, 2);
            }
        }
    }
    
    private final String hexData;
    
    private AoC2021_16(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.hexData = input.get(0);
    }
    
    public static final AoC2021_16 create(final List<String> input) {
        return new AoC2021_16(input, false);
    }

    public static final AoC2021_16 createDebug(final List<String> input) {
        return new AoC2021_16(input, true);
    }
    
    private static final class VersionSumBITSHandler extends AoC2021_16.BITS.LoggingBITSHandler {
        private int versionSum = 0;

        public VersionSumBITSHandler(final boolean debug) {
            super(debug);
        }

        public int getVersionSum() {
            return versionSum;
        }

        @Override
        public void version(final int version) {
            super.version(version);
            versionSum += version;
        }
    }
    
    private static final class ValueBITSCalcHandler extends AoC2021_16.BITS.DOMBITSHandler {

        public ValueBITSCalcHandler(final boolean debug) {
            super(debug);
        }
        
        private long calcValue(final AoC2021_16.BITS.Packet packet) {
            final List<Long> values = packet.getSubPackets().stream()
                .map(p -> {
                    if (p.getValue() != null) {
                        return p.getValue();
                    }
                    return calcValue(p);
                })
                .collect(toList());
            final LongStream longs = values.stream().mapToLong(Long::longValue);
            if (packet.getTypeId() == 0) {
                return longs.sum();
            } else if (packet.getTypeId() == 1) {
                return longs.reduce(1L, (a, b) -> a * b);
            } else if (packet.getTypeId() == 2) {
                return longs.min().orElseThrow();
            } else if (packet.getTypeId() == 3) {
                return longs.max().orElseThrow();
            } else if (packet.getTypeId() == 5) {
                assert values.size() == 2;
                return values.get(0) > values.get(1) ? 1L : 0L;
            } else if (packet.getTypeId() == 6) {
                assert values.size() == 2;
                return values.get(0) < values.get(1) ? 1L : 0L;
            } else {
                assert values.size() == 2;
                return values.get(0).equals(values.get(1)) ? 1L : 0L;
            }
        }
        
        public long getValue() {
            return calcValue(this.getPacket());
        }
    }
    
    @Override
    public Integer solvePart1() {
        final VersionSumBITSHandler handler = new VersionSumBITSHandler(this.debug);
        BITS.Parser.createParser(handler).parseHex(this.hexData);
        return handler.getVersionSum();
    }
    
    @Override
    public Long solvePart2() {
        final ValueBITSCalcHandler handler = new ValueBITSCalcHandler(this.debug);
        BITS.Parser.createParser(handler).parseHex(this.hexData);
        log(handler.getPacket());
        return handler.getValue();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_16.create(List.of("D2FE28")).solvePart1() == 6;
        assert AoC2021_16.create(List.of("38006F45291200")).solvePart1() == 9;
        assert AoC2021_16.create(List.of("EE00D40C823060")).solvePart1() == 14;
        assert AoC2021_16.create(List.of("C200B40A82")).solvePart2() == 3;
        assert AoC2021_16.create(List.of("04005AC33890")).solvePart2() == 54;
        assert AoC2021_16.create(List.of("880086C3E88112")).solvePart2() == 7;
        assert AoC2021_16.create(List.of("CE00C43D881120")).solvePart2() == 9;
        assert AoC2021_16.create(List.of("D8005AC2A8F0")).solvePart2() == 1;
        assert AoC2021_16.create(List.of("F600BC2D8F")).solvePart2() == 0;
        assert AoC2021_16.create(List.of("9C005AC2F8F0")).solvePart2() == 0;
        assert AoC2021_16.create(List.of("9C0141080250320F1802104A08")).solvePart2() == 1;

        final Puzzle puzzle = Aocd.puzzle(2021, 16);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_16.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_16.create(puzzle.getInputData()).solvePart2())
        );
    }
}