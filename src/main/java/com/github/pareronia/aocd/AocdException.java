package com.github.pareronia.aocd;

import java.io.IOException;

public class AocdException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AocdException(String message) {
		super(message);
	}

	public AocdException(IOException e) {
		super(e);
	}
}
