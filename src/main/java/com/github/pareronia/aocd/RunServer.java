package com.github.pareronia.aocd;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunServer {
	
	public static final int OK = 0;
	public static final int ERROR = 1;
	private static final int MAX_REQUEST_SIZE = 10_000;
	private static final String HELLO = "HELLO";
	private static final String END = "END";
	private static final String STOP = "STOP";
	
	private static final Logger log = Logger.getLogger(RunServer.class.getName());
	
	private final int port;
	private final RequestHandler requestHandler;
	private ServerSocket server;

	public static void main(final String[] args) throws Exception {
		new RunServer(
		        5555,
		        Runner.createRequestHandler(new SystemUtils()))
		    .start();
	}
	
	public RunServer(final int port, final RequestHandler requestHandler) {
		this.port = port;
		this.requestHandler = requestHandler;
		log.setLevel(Level.WARNING);
	}
	
	public int start() {
		try {
			this.server = new ServerSocket(port);
			log.info("Server started");
			log.info("Waiting for a client ...");
			while (!this.server.isClosed()) {
				try (final Socket socket = this.server.accept()) {
					log.info("Client accepted");
					handleClientConnection(socket);
				}
			}
		} catch (final IOException e) {
			if (this.server == null || !this.server.isClosed()) {
				log.log(Level.SEVERE, e.getMessage(), e);
				return ERROR;
			}
		}

		return OK;
	}

	private void handleClientConnection(final Socket socket) throws IOException {
		final BufferedReader dis = new BufferedReader(
				new InputStreamReader(new DataInputStream(socket.getInputStream())));
		
		final List<String> request = new ArrayList<>();
		String str;
		do {
			str = dis.readLine();
			if (asList(HELLO, END, STOP).contains(str)) {
				break;
			}
			request.add(str);
		} while (request.size() < MAX_REQUEST_SIZE);
		
		if (str.equals(STOP)) {
			stop();
			return;
		}
		String response;
		if (str.equals(HELLO)) {
			response = HELLO;
		} else {
			try {
				response = requestHandler.handle(request);
			} catch (final Exception e) {
				response = "ERROR: " + e;
				log.log(Level.SEVERE, e.getMessage(), e);
//				e.printStackTrace(System.err);
			}
		}
		final BufferedWriter dos = new BufferedWriter(
				new OutputStreamWriter(new DataOutputStream(socket.getOutputStream())));
		dos.write(response + System.lineSeparator());
		dos.flush();
	}
	
	public int stop() {
		try {
			if (!this.server.isClosed()) {
				log.info("Server stopping");
				this.server.close();
			}
			return OK;
		} catch (final IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			return ERROR;
		}
	}
	
	interface RequestHandler {
		String handle(List<String> request) throws Exception;
	}
}
