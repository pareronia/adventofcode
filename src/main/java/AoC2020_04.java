import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.pareronia.aoc.RangeInclusive;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_04
        extends SolutionBase<Set<AoC2020_04.Passport>, Integer, Integer> {
	
	private AoC2020_04(final boolean debug) {
		super(debug);
	}
	
	public static AoC2020_04 create() {
		return new AoC2020_04(false);
	}

	public static AoC2020_04 createDebug() {
		return new AoC2020_04(true);
	}
	
	@Override
    protected Set<Passport> parseInput(final List<String> inputs) {
		return StringOps.toBlocks(inputs).stream()
				.map(Passport::fromInput)
				.collect(toSet());
	}

	@Override
	public Integer solvePart1(final Set<Passport> passports) {
		return countValid(passports, Passport::isValid1);
	}

	@Override
	public Integer solvePart2(final Set<Passport> passports) {
		return countValid(passports, Passport::isValid2);
	}

	private int countValid(
	        final Set<Passport> passports,
	        final Predicate<Passport> predicate
	) {
		return (int) passports.stream().filter(predicate).count();
	}

	@Samples({
	    @Sample(method = "part1", input = TEST, expected = "10"),
	    @Sample(method = "part2", input = TEST, expected = "6"),
	})
	public static void main(final String[] args) throws Exception {
		AoC2020_04.create().run();
	}
	
	private static final String TEST = """
	        ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
	        byr:1937 iyr:2017 cid:147 hgt:183cm
	        
	        iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
	        hcl:#cfa07d byr:1929
	        
	        hcl:#ae17e1 iyr:2013
	        eyr:2024
	        ecl:brn pid:760753108 byr:1931
	        hgt:179cm
	        
	        hcl:#cfa07d eyr:2025 pid:166559648
	        iyr:2011 ecl:brn hgt:59in
	        
	        eyr:1972 cid:100
	        hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926
	        
	        iyr:2019
	        hcl:#602927 eyr:1967 hgt:170cm
	        ecl:grn pid:012533040 byr:1946
	        
	        hcl:dab227 iyr:2012
	        ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277
	        
	        hgt:59cm ecl:zzz
	        eyr:2038 hcl:74454a iyr:2023
	        pid:3556412378 byr:2007
	        
	        pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
	        hcl:#623a2f
	        
	        eyr:2029 ecl:blu cid:129 byr:1989
	        iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm
	        
	        hcl:#888785
	        hgt:164cm byr:2001 iyr:2015 cid:88
	        pid:545766238 ecl:hzl
	        eyr:2022
	        
	        iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
	        """;
	
	record Passport(
		String byr,  // (Birth Year)
		String iyr,  // (Issue Year)
		String eyr,  // (Expiration Year)
		String hgt,  // (Height)
		String hcl,  // (Hair Color)
		String ecl,  // (Eye Color)
		String pid   // (Passport ID)
    ) {
	    
	    public static Passport fromInput(final List<String> block) {
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
	    }
		
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
			return RangeInclusive.between(1920, 2002).contains(Integer.valueOf(this.byr));
		}
		
		private boolean iyrValid() {
			return RangeInclusive.between(2010, 2020).contains(Integer.valueOf(this.iyr));
		}
		
		private boolean eyrValid() {
			return RangeInclusive.between(2020, 2030).contains(Integer.valueOf(this.eyr));
		}
		
		private boolean hgtValid() {
			final int len = this.hgt.length();
            if (this.hgt.endsWith("in")) {
			    final Integer hgt = Integer.valueOf(this.hgt.substring(0, len - 2));
				return RangeInclusive.between(59, 76).contains(hgt);
			} else if (this.hgt.endsWith("cm")) {
			    final Integer hgt = Integer.valueOf(this.hgt.substring(0, len - 2));
				return RangeInclusive.between(150, 193).contains(hgt);
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
		
		public static PassportBuilder builder() {
		    return new PassportBuilder();
		}
		
		public static final class PassportBuilder {
            private String byr;
            private String iyr;
            private String eyr;
            private String hgt;
            private String hcl;
            private String ecl;
            private String pid;
		    
            public PassportBuilder byr(final String byr) {
                this.byr = byr;
                return this;
            }

            public PassportBuilder iyr(final String iyr) {
                this.iyr = iyr;
                return this;
            }

            public PassportBuilder eyr(final String eyr) {
                this.eyr = eyr;
                return this;
            }

            public PassportBuilder hgt(final String hgt) {
                this.hgt = hgt;
                return this;
            }

            public PassportBuilder hcl(final String hcl) {
                this.hcl = hcl;
                return this;
            }

            public PassportBuilder ecl(final String ecl) {
                this.ecl = ecl;
                return this;
            }

            public PassportBuilder pid(final String pid) {
                this.pid = pid;
                return this;
            }

            public PassportBuilder cid(final String cid) {
                return this;
            }

            public Passport build() {
		        return new Passport(byr, iyr, eyr, hgt, hcl, ecl, pid);
		    }
		}
	}
}