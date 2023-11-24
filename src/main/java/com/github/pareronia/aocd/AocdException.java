package com.github.pareronia.aocd;

public class AocdException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AocdException(final String message) {
		super(message);
	}

	public AocdException(final Exception e) {
		super(e);
	}
}
