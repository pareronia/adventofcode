package com.github.pareronia.aocd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTestCase {
	
	private SystemUtils systemUtils;
	
	@BeforeEach
	void setUp() {
		systemUtils = mock(SystemUtils.class);
	}

	@Test
	void user() {
	    final User user = new User("name", "token", "uid", Paths.get("memoDir"));
		
	    assertThat(user.token()).isEqualTo("token");
		assertThat(user.name()).isEqualTo("name");
		assertThat(user.id()).isEqualTo("uid");
		assertThat(user.memoDir().toString()).isEqualTo(Paths.get("memoDir").toString());
	}
	
	@Test
	void getDefaultUserFromEnv() {
	    when(systemUtils.getTokenFromEnv()).thenReturn("token");
		when(systemUtils.getAocdDir()).thenReturn(Paths.get("aocdDir"));
		when(systemUtils.readMapFromJsonFile(Paths.get("aocdDir", "token2id.json")))
		    .thenReturn(Map.of("token", "uid"));
	    
	    final User user = new User.UserBuilder(systemUtils).build();
		
	    assertThat(user.token()).isEqualTo("token");
		assertThat(user.name()).isEqualTo("default");
		assertThat(user.id()).isEqualTo("uid");
		assertThat(user.memoDir().toString()).isEqualTo(Paths.get("aocdDir", "uid").toString());
	}
	
	@Test
	void getDefaultUserFromTokenFile() {
	    when(systemUtils.getTokenFromEnv()).thenReturn("");
		when(systemUtils.getAocdDir()).thenReturn(Paths.get("aocdDir"));
		when(systemUtils.readFirstLine(Paths.get("aocdDir", "token")))
		    .thenReturn(Optional.of("token"));
		when(systemUtils.readMapFromJsonFile(Paths.get("aocdDir", "token2id.json")))
		    .thenReturn(Map.of("token", "uid"));
	    
	    final User user = new User.UserBuilder(systemUtils).build();
		
	    assertThat(user.token()).isEqualTo("token");
		assertThat(user.name()).isEqualTo("default");
		assertThat(user.id()).isEqualTo("uid");
		assertThat(user.memoDir().toString()).isEqualTo(Paths.get("aocdDir", "uid").toString());
	}
	
	@Test
	void getNamedUser() {
	    when(systemUtils.getAocdDir()).thenReturn(Paths.get("aocdDir"));
	    when(systemUtils.readMapFromJsonFile(Paths.get("aocdDir", "tokens.json")))
	        .thenReturn(Map.of("name", "token"));
		when(systemUtils.readMapFromJsonFile(Paths.get("aocdDir", "token2id.json")))
		    .thenReturn(Map.of("token", "uid"));
	    
	    final User user = new User.UserBuilder(systemUtils).name("name").build();
		
	    assertThat(user.token()).isEqualTo("token");
		assertThat(user.name()).isEqualTo("name");
		assertThat(user.id()).isEqualTo("uid");
		assertThat(user.memoDir().toString()).isEqualTo(Paths.get("aocdDir", "uid").toString());
	}
}
