package com.github.pareronia.aocd;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class UserTestCase {
	
	private User user;

	private SystemUtils systemUtils;
	
	@Before
	public void setUp() {
		systemUtils = mock(SystemUtils.class);
		when(systemUtils.getAocdDir()).thenReturn(Paths.get("aocdDir"));
		when(systemUtils.getUserIds()).thenReturn(Map.of("token", "uid"));

		user = User.create(systemUtils, "token");
	}

	@Test
	public void getToken() {
		assertThat(user.getToken(), is("token"));
	}
	
	@Test
	public void getId() {
	    assertThat(user.getId(), is("uid"));
	}
	
	@Test
	public void getMemoDir() {
		assertThat(user.getMemoDir().toString(), is(Paths.get("aocdDir", "uid").toString()));
	}
}
