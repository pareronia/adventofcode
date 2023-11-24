import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.github.pareronia.aoc.Range;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Puzzle;

import lombok.Builder;
import lombok.ToString;

public class AoC2020_04 extends AoCBase {
	
	private final Set<Passport> passports;
	
	private AoC2020_04(final List<String> input, final boolean debug) {
		super(debug);
		this.passports = parse(input);
	}
	
	public static AoC2020_04 create(final List<String> input) {
		return new AoC2020_04(input, false);
	}

	public static AoC2020_04 createDebug(final List<String> input) {
		return new AoC2020_04(input, true);
	}
	
	private Set<Passport> parse(final List<String> inputs) {
		final Function<List<String>, Passport> buildPassport = block -> {
			final Passport.PassportBuilder passportBuilder = Passport.builder();
			final Consumer<String> applyField = field -> {
				final String[] fieldSplits = field.split(":");
				try {
					Passport.PassportBuilder.class
						.getMethod(fieldSplits[0], String.class)
						.invoke(passportBuilder, fieldSplits[1]);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					throw new RuntimeException(e);
				}
			};
			block.stream().flatMap(line -> Arrays.stream(line.split(" ")))
			    .forEach(applyField);
			return passportBuilder.build();
		};
		return toBlocks(inputs).stream()
				.map(buildPassport)
				.collect(toSet());
	}

	@Override
	public Long solvePart1() {
		return countValid(Passport::isValid1);
	}

	@Override
	public Long solvePart2() {
		return countValid(Passport::isValid2);
	}

	private long countValid(final Predicate<Passport> predicate) {
		return this.passports.stream().filter(predicate).count();
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2020_04.createDebug(TEST).solvePart1() == 10;
		assert AoC2020_04.createDebug(TEST).solvePart2() == 6;
		
		final Puzzle puzzle = Puzzle.create(2020, 4);
		final List<String> input = puzzle.getInputData();
		puzzle.check(
		    () -> lap("Part 1", AoC2020_04.create(input)::solvePart1),
		    () -> lap("Part 2", AoC2020_04.create(input)::solvePart2)
		);
	}
	
	private static final List<String> TEST = splitLines(
			"ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\r\n" +
			"byr:1937 iyr:2017 cid:147 hgt:183cm\r\n" +
			"\r\n" +
			"iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\r\n" +
			"hcl:#cfa07d byr:1929\r\n" +
			"\r\n" +
			"hcl:#ae17e1 iyr:2013\r\n" +
			"eyr:2024\r\n" +
			"ecl:brn pid:760753108 byr:1931\r\n" +
			"hgt:179cm\r\n" +
			"\r\n" +
			"hcl:#cfa07d eyr:2025 pid:166559648\r\n" +
			"iyr:2011 ecl:brn hgt:59in\r\n" +
			"\r\n" +
			"eyr:1972 cid:100\r\n" +
			"hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926\r\n" +
			"\r\n" +
			"iyr:2019\r\n" +
			"hcl:#602927 eyr:1967 hgt:170cm\r\n" +
			"ecl:grn pid:012533040 byr:1946\r\n" +
			"\r\n" +
			"hcl:dab227 iyr:2012\r\n" +
			"ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277\r\n" +
			"\r\n" +
			"hgt:59cm ecl:zzz\r\n" +
			"eyr:2038 hcl:74454a iyr:2023\r\n" +
			"pid:3556412378 byr:2007\r\n" +
			"\r\n" +
			"pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\r\n" +
			"hcl:#623a2f\r\n" +
			"\r\n" +
			"eyr:2029 ecl:blu cid:129 byr:1989\r\n" +
			"iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm\r\n" +
			"\r\n" +
			"hcl:#888785\r\n" +
			"hgt:164cm byr:2001 iyr:2015 cid:88\r\n" +
			"pid:545766238 ecl:hzl\r\n" +
			"eyr:2022\r\n" +
			"\r\n" +
			"iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719"
	);
	
	@Builder
	@ToString
	private static final class Passport {
		private final String byr;  // (Birth Year)
		private final String iyr;  // (Issue Year)
		private final String eyr;  // (Expiration Year)
		private final String hgt;  // (Height)
		private final String hcl;  // (Hair Color)
		private final String ecl;  // (Eye Color)
		private final String pid;  // (Passport ID)
		private final String cid;  // (Country ID)

		public boolean isValid1() {
			return this.byr != null
				&& this.iyr != null
				&& this.eyr != null
				&& this.hgt != null
				&& this.hcl != null
				&& this.ecl != null
				&& this.pid != null;
		}
		
		private boolean byrValid() {
			return Range.between(1920, 2002).contains(Integer.valueOf(this.byr));
		}
		
		private boolean iyrValid() {
			return Range.between(2010, 2020).contains(Integer.valueOf(this.iyr));
		}
		
		private boolean eyrValid() {
			return Range.between(2020, 2030).contains(Integer.valueOf(this.eyr));
		}
		
		private boolean hgtValid() {
		    final Integer hgt = Integer.valueOf(this.hgt.substring(0, this.hgt.length() - 2));
			if (this.hgt.endsWith("in")) {
				return Range.between(59, 76).contains(hgt);
			} else if (this.hgt.endsWith("cm")) {
				return Range.between(150, 193).contains(hgt);
			} else {
				return false;
			}
		}
		
		private boolean hclValid() {
			if (!this.hcl.startsWith("#") || this.hcl.length() != 7) {
				return false;
			}
			return Utils.asCharacterStream(this.hcl.substring(1))
			        .filter(c -> "0123456789abcdef".indexOf(c) == -1)
			        .count() == 0;
		}
		
		private boolean eclValid() {
			return asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
					.contains(this.ecl);
		}
		
		private boolean pidValid() {
			return this.pid.length() == 9 && StringUtils.isNumeric(this.pid);
		}
		
		public boolean isValid2() {
			return isValid1()
					&& byrValid() && iyrValid() && eyrValid() && hgtValid()
					&& hclValid() && eclValid() && pidValid();
		}
	}
}