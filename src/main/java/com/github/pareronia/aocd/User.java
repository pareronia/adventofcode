/**
Some parts of this code:

Copyright (c) 2016 wim glenn

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.github.pareronia.aocd;

import static java.util.stream.Collectors.toSet;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.pareronia.aoc.StringUtils;

public record User(String name, String token, String id, Path memoDir) {

    public static User getDefaultUser() {
        return User.builder().build();
    }

    public static UserBuilder builder() {
        return new UserBuilder(new SystemUtils());
    }

    public static class UserBuilder {
        private final SystemUtils systemUtils;
        private String name;

        protected UserBuilder(final SystemUtils systemUtils) {
            this.systemUtils = systemUtils;
        }

        public UserBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public User build() {
            return build(this.name);
        }

        public Set<User> getAllUsers() {
            return getTokens().keySet().stream().map(this::build).collect(toSet());
        }

        private User build(String name) {
            final String token;
            if (name == null) {
                name = "default";
                token = getToken();
            } else {
                token = getTokens().get(name);
            }
            Objects.requireNonNull(token);
            final String id = getUserIds().get(token);
            final Path memoDir = systemUtils.getAocdDir().resolve(id);
            return new User(name, token, id, memoDir);
        }

        public String getToken() {
            final String tokenFromEnv = systemUtils.getTokenFromEnv();
            if (StringUtils.isNotBlank(tokenFromEnv)) {
                return tokenFromEnv;
            }
            return systemUtils.readFirstLine(systemUtils.getAocdDir().resolve("token"))
                    .orElseThrow(() -> new AocdException("Missing session ID"));
        }

        private Map<String, String> getUserIds() {
            final Path path = systemUtils.getAocdDir().resolve("token2id.json");
            return systemUtils.readMapFromJsonFile(path);
        }

        private Map<String, String> getTokens() {
            final Path path = systemUtils.getAocdDir().resolve("tokens.json");
            return systemUtils.readMapFromJsonFile(path);
        }
    }
}
