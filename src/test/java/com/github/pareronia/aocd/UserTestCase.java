package com.github.pareronia.aocd;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class UserTestCase {
	
	private User user;
	
	@Before
	public void setUp() {
		user = User.create("token", Paths.get("memoDir"));
	}

	@Test
	public void getToken() {
		assertThat(user.getToken(), is("token"));
	}
	
	@Test
	public void getMemoDir() {
		assertThat(user.getMemoDir().toString(), is("memoDir"));
	}
}
