import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AoCBaseTestCase {

	@Test
	public void toBlocks_emptyIfInputsEmpty() {
		final List<String> inputs = Collections.emptyList();
		
		final List<List<String>> result = AoCBase.toBlocks(inputs);
		
		assertThat(result).isEmpty();
	}
	
	@Test
	public void toBlocks_one_line() {
		final List<String> inputs = List.of("1");
		
		final List<List<String>> result = AoCBase.toBlocks(inputs);
		
		assertThat(result).containsExactly(List.of("1"));
	}
	
	@Test
	public void toBlocks_one_block() {
		final List<String> inputs = List.of("1", "2");
		
		final List<List<String>> result = AoCBase.toBlocks(inputs);
		
		assertThat(result).containsExactly(List.of("1", "2"));
	}
	
	@Test
	public void toBlocks_one_block_last_empty() {
		final List<String> inputs = List.of("1", "2", "");
		
		final List<List<String>> result = AoCBase.toBlocks(inputs);
		
		assertThat(result).containsExactly(List.of("1", "2"));
	}
	
	@Test
	public void toBlocks_multiple_blocks() {
		final List<String> inputs = List.of("1", "", "2");
		
		final List<List<String>> result = AoCBase.toBlocks(inputs);
		
		assertThat(result).containsExactly(List.of("1"), List.of("2"));
	}
	
	@Test
	public void toBlocks_multiple_blocks_last_empty() {
		final List<String> inputs = List.of("1", "2", "", "3", "");
		
		final List<List<String>> result = AoCBase.toBlocks(inputs);
		
		assertThat(result).containsExactly(List.of("1", "2"), List.of("3"));
	}
}
