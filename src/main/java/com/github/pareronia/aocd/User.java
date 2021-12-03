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

import java.nio.file.Path;

public class User {
	
	private final String token;
	private final Path memoDir;
	private final String id;

	private User(final SystemUtils systemUtils, final String token) {
		this.token = token;
		this.id = systemUtils.getUserIds().get(token);
		this.memoDir = systemUtils.getAocdDir().resolve(this.id);
	}

	public String getToken() {
		return token;
	}

	public Path getMemoDir() {
		return memoDir;
	}
	
	public String getId() {
	    return id;
	}

	public static User create(final SystemUtils systemUtils, final String token) {
		return new User(systemUtils, token);
	}

	public static User getDefaultUser() {
		final SystemUtils systemUtils = new SystemUtils();
		final String token = systemUtils.getToken();
		return new User(systemUtils, token);
	}
}
