package fr.auretyube.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class Client implements Runnable {
	
	private String host;
	private int port;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean running = false;
	
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void connect(String threadGroup) {
		try {
			socket = new Socket(host, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			new Thread(new ThreadGroup(threadGroup), this, "client").start();
		} catch (ConnectException e ) {
			System.err.println("Unable to connect to the server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			running = false;
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendObject(Object packet) {
		try {
			out.writeObject(packet);
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public void run() {
		try {
			running = true;
			
			while(running) {
				try {
					Object data = in.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SocketException e) {
					close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}