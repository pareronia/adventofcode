package com.github.pareronia.aocd;

import static com.github.pareronia.aoc.CharArrayUtils.subarray;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.pareronia.aocd.RunServer.RequestHandler;

public class RunServerTest {

	private RunServer runServer;
	
	private final RequestHandler requestHandler = Runner.createRequestHandler(mock(SystemUtils.class));
	
	@BeforeEach
	public void setUp() {
		runServer = new RunServer(5555, requestHandler);
		new Thread(() -> {
			synchronized (this) {
				final int status = runServer.start();
				assertThat(status).isEqualTo(RunServer.OK);
			}
		}).start();
	}
	
	@AfterEach
	public void tearDown() {
		runServer.stop();
	}

	@Test
	public void test() throws Exception {
		final List<String> lines = new ArrayList<>();
		lines.add("2020");
		lines.add("1");
		lines.add("1721");
		lines.add("979");
		lines.add("366");
		lines.add("299");
		lines.add("675");
		lines.add("1456");
		lines.add("END");
		
		doTest(asList("HELLO"), "HELLO" + System.lineSeparator());
		doTest(lines, "514579\r\n241861950" + System.lineSeparator());
		doTest(asList("2020", "2", "1-3 a: abcde\r\n1-3 b: cdefg\r\n2-9 c: ccccccccc", "END"),
			   "2\r\n1" + System.lineSeparator());
		doTest(asList("STOP"), "STOP" + System.lineSeparator());
	}

	private void doTest(final List<String> in, final String expectedResponse) throws Exception, IOException {
		final long start = System.nanoTime();
		try (final Socket socket = new Socket(InetAddress.getLoopbackAddress(), 5555)) {
			assertThat(socket.isConnected()).isTrue();
			
			final BufferedWriter dos = new BufferedWriter(
					new OutputStreamWriter(new DataOutputStream(socket.getOutputStream())));
			for (int i = 0; i < in.size(); i++) {
				dos.write(in.get(i) + System.lineSeparator());
			}
			dos.flush();
			
			if (in.equals(asList("STOP"))) {
				return;
			}
			final BufferedReader dis = new BufferedReader(
					new InputStreamReader(new DataInputStream(socket.getInputStream())));
			String response = null;
			final char[] c = new char[1024];
			final int n = dis.read(c, 0, 1024);
			if (n > 0) {
				response = new String(subarray(c, 0, n));
			}
			assertThat(response).isEqualTo(expectedResponse);
		} finally {
			System.out.println(String.format("Took %d µs", (System.nanoTime() - start) / 1000));
		}
	}
}
