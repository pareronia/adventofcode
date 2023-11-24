package com.github.pareronia.aoc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StringUtilsTestCase {

    @Test
    void empty() {
        assertThat(StringUtils.isEmpty(null)).isTrue();
        assertThat(StringUtils.isEmpty("")).isTrue();
        assertThat(StringUtils.isEmpty(" ")).isFalse();
        assertThat(StringUtils.isEmpty("bob")).isFalse();
        assertThat(StringUtils.isEmpty("  bob  ")).isFalse();
    }
    
    @Test
    void isBlank( ) {
        assertThat(StringUtils.isBlank(null)).isTrue();
        assertThat(StringUtils.isBlank("")).isTrue();
        assertThat(StringUtils.isBlank(" ")).isTrue();
        assertThat(StringUtils.isBlank("bob")).isFalse();
        assertThat(StringUtils.isBlank("  bob  ")).isFalse();
    }
    
    @Test
    void isNotBlank() {
        assertThat(StringUtils.isNotBlank(null)).isFalse();
        assertThat(StringUtils.isNotBlank("")).isFalse();
        assertThat(StringUtils.isNotBlank(" ")).isFalse();
        assertThat(StringUtils.isNotBlank("bob")).isTrue();
        assertThat(StringUtils.isNotBlank("  bob  ")).isTrue();
    }

    @Test
    void isAllLowerCase() {
        assertThat(StringUtils.isAllLowerCase(null)).isFalse();
        assertThat(StringUtils.isAllLowerCase("")).isFalse();
        assertThat(StringUtils.isAllLowerCase("  ")).isFalse();
        assertThat(StringUtils.isAllLowerCase("abc")).isTrue();
        assertThat(StringUtils.isAllLowerCase("abC")).isFalse();
        assertThat(StringUtils.isAllLowerCase("ab c")).isFalse();
        assertThat(StringUtils.isAllLowerCase("ab1c")).isFalse();
        assertThat(StringUtils.isAllLowerCase("ab/c")).isFalse();
    }

    @Test
    void isNumeric() {
         assertThat(StringUtils.isNumeric(null)).isFalse();
         assertThat(StringUtils.isNumeric("")).isFalse();
         assertThat(StringUtils.isNumeric("  ")).isFalse();
         assertThat(StringUtils.isNumeric("123")).isTrue();
         assertThat(StringUtils.isNumeric("\u0967\u0968\u0969")).isTrue();
         assertThat(StringUtils.isNumeric("12 3")).isFalse();
         assertThat(StringUtils.isNumeric("ab2c")).isFalse();
         assertThat(StringUtils.isNumeric("12-3")).isFalse();
         assertThat(StringUtils.isNumeric("12.3")).isFalse();
         assertThat(StringUtils.isNumeric("-123")).isFalse();
         assertThat(StringUtils.isNumeric("+123")).isFalse();
    }
    
    @Test
    void repeat() {
        assertThat(StringUtils.repeat('e', 0)).isEqualTo("");
        assertThat(StringUtils.repeat('e', 3)).isEqualTo("eee");
        assertThat(StringUtils.repeat('e', -2)).isEqualTo("");
    }
    
    @Test
    void reverse() {
        assertThat(StringUtils.reverse(null)).isNull();
        assertThat(StringUtils.reverse("")).isEqualTo("");
        assertThat(StringUtils.reverse("bat")).isEqualTo("tab");
    }

    @Test
    void leftPad() {
        assertThat(StringUtils.leftPad(null, 3, 'z')).isNull();
        assertThat(StringUtils.leftPad("", 3, 'z')).isEqualTo("zzz");
        assertThat(StringUtils.leftPad("bat", 3, 'z')).isEqualTo("bat");
        assertThat(StringUtils.leftPad("bat", 5, 'z')).isEqualTo("zzbat");
        assertThat(StringUtils.leftPad("bat", 1, 'z')).isEqualTo("bat");
        assertThat(StringUtils.leftPad("bat", -1, 'z')).isEqualTo("bat");
    }
    
    @Test
    void stripStart() {
        assertThat(StringUtils.stripStart(null, "*")).isNull();
        assertThat(StringUtils.stripStart("", "*")).isEqualTo("");
        assertThat(StringUtils.stripStart("abc", "")).isEqualTo("abc");
        assertThat(StringUtils.stripStart("abc", null)).isEqualTo("abc");
        assertThat(StringUtils.stripStart("  abc", null)).isEqualTo("abc");
        assertThat(StringUtils.stripStart("abc  ", null)).isEqualTo("abc  ");
        assertThat(StringUtils.stripStart(" abc ", null)).isEqualTo("abc ");
        assertThat(StringUtils.stripStart("yxabc  ", "xyz")).isEqualTo("abc  ");
    }
    
    @Test
    void stripEnd() {
        assertThat(StringUtils.stripEnd(null, "*")).isNull();
        assertThat(StringUtils.stripEnd("", "*")).isEqualTo("");
        assertThat(StringUtils.stripEnd("abc", "")).isEqualTo("abc");
        assertThat(StringUtils.stripEnd("abc", null)).isEqualTo("abc");
        assertThat(StringUtils.stripEnd("  abc", null)).isEqualTo("  abc");
        assertThat(StringUtils.stripEnd("abc  ", null)).isEqualTo("abc");
        assertThat(StringUtils.stripEnd(" abc ", null)).isEqualTo(" abc");
        assertThat(StringUtils.stripEnd("  abcyx", "xyz")).isEqualTo("  abc");
        assertThat(StringUtils.stripEnd("120.00", ".0")).isEqualTo("12");
    }
    
    @Test
    void strip() {
        assertThat(StringUtils.strip(null, "*")).isNull();
        assertThat(StringUtils.strip("", "*")).isEqualTo("");
        assertThat(StringUtils.strip("abc", "")).isEqualTo("abc");
        assertThat(StringUtils.strip("abc", null)).isEqualTo("abc");
        assertThat(StringUtils.strip("  abc", null)).isEqualTo("abc");
        assertThat(StringUtils.strip("abc  ", null)).isEqualTo("abc");
        assertThat(StringUtils.strip(" abc ", null)).isEqualTo("abc");
        assertThat(StringUtils.strip("  abcyx", "xyz")).isEqualTo("  abc");
    }

    @Test
    void countMatchesChar() {
        assertThat(StringUtils.countMatches(null, 'z')).isEqualTo(0);
        assertThat(StringUtils.countMatches("", 'z')).isEqualTo(0);
        assertThat(StringUtils.countMatches("abba", null)).isEqualTo(0);
        assertThat(StringUtils.countMatches("abba", 'a')).isEqualTo(2);
        assertThat(StringUtils.countMatches("abba", 'b')).isEqualTo(2);
        assertThat(StringUtils.countMatches("abba", 'x')).isEqualTo(0);
        
    }

    @Test
    void countMatches() {
        assertThat(StringUtils.countMatches(null, "z")).isEqualTo(0);
        assertThat(StringUtils.countMatches("", "z")).isEqualTo(0);
        assertThat(StringUtils.countMatches("abba", null)).isEqualTo(0);
        assertThat(StringUtils.countMatches("abba", "")).isEqualTo(0);
        assertThat(StringUtils.countMatches("abba", "a")).isEqualTo(2);
        assertThat(StringUtils.countMatches("abba", "b")).isEqualTo(2);
        assertThat(StringUtils.countMatches("abba", "ab")).isEqualTo(1);
        assertThat(StringUtils.countMatches("abba", "xxx")).isEqualTo(0);
    }
    
    @Test
    void substringBefore() {
        assertThat(StringUtils.substringBefore(null, "z")).isNull();
        assertThat(StringUtils.substringBefore("", "z")).isEqualTo("");
        assertThat(StringUtils.substringBefore("abc", "a")).isEqualTo("");
        assertThat(StringUtils.substringBefore("abcba", "b")).isEqualTo("a");
        assertThat(StringUtils.substringBefore("abc", "c")).isEqualTo("ab");
        assertThat(StringUtils.substringBefore("abc", "d")).isEqualTo("abc");
        assertThat(StringUtils.substringBefore("abc", "")).isEqualTo("");
        assertThat(StringUtils.substringBefore("abc", null)).isEqualTo("abc");
    }
}
