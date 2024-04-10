package com.github.pareronia.aocd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTestCase {
	
	private User user;

	private SystemUtils systemUtils;
	
	@BeforeEach
	public void setUp() {
		systemUtils = mock(SystemUtils.class);
		when(systemUtils.getAocdDir()).thenReturn(Paths.get("aocdDir"));
		when(systemUtils.getUserIds()).thenReturn(Map.of("token", "uid"));

		user = new User(systemUtils, "token", "name");
	}

	@Test
	public void getToken() {
		assertThat(user.getToken()).isEqualTo("token");
	}
	
	@Test
	public void getName() {
		assertThat(user.getName()).isEqualTo("name");
	}
	
	@Test
	public void getId() {
	    assertThat(user.getId()).isEqualTo("uid");
	}
	
	@Test
	public void getMemoDir() {
		assertThat(user.getMemoDir().toString()).isEqualTo(Paths.get("aocdDir", "uid").toString());
	}
}
