package edu.odu.jrugh001.yellowclient.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import edu.odu.jrugh001.yellowclient.ServerHandle;
import edu.odu.jrugh001.yellowclient.controller.LoginController;
import edu.odu.jrugh001.yellowclient.net.listening.ClientEventBus;
import edu.odu.jrugh001.yellowclient.net.listening.Opcodes;
import edu.odu.jrugh001.yellowclient.net.listening.Write;
import javafx.scene.paint.Color;
import lombok.Getter;

public class ClientNetwork {
	
	private Socket socket;
	private final int PORT = 3407;
	
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	
	private ClientEventBus eventBus = new ClientEventBus();
	private ServerHandle serverHandle;
	
	private @Getter volatile boolean running = false;
	
	public void connect(String address, String email, String password) {
		
		new Thread(() -> {
			try {
				
				socket = new Socket();
				socket.connect(new InetSocketAddress(address, PORT), 1000*5);
				running = true;
				
			} catch (IOException e) {
				
				// Failed to connect
				if (e instanceof ConnectException) {
					
					LoginController.getInstance().setLoginLabelText("Failed to connect to " + address + ".", Color.RED);
					return;
					
				} else {
					e.printStackTrace();	
				}
			}
			
			setup(email, password);
			
		}, "Connection").start();
	}
	
	private void setup(String email, String password) {
		try {
			outStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		registerListeners();
		
		// Sending login request
		send(Opcodes.LOGIN_REQUEST, outStream -> {
			outStream.writeUTF(email);
			outStream.writeUTF(password);
		});
		
		receivePackets();
	}
	
	
	private void registerListeners() {
		eventBus.registerListener(new ClientLoggingMangement());
		eventBus.registerListener(new ManageChatBox());
	}

	public void send(final char opcode, Write writeCallback) {		
		try {
			outStream.writeChar(opcode);
			writeCallback.accept(outStream);
			outStream.flush();
		} catch (IOException e) {
			if (!(e instanceof SocketException)) {
				e.printStackTrace();
			}
		}
	}
	
	public void receivePackets() {
		
		try {
			inStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		serverHandle = new ServerHandle(outStream, inStream);
		
		new Thread(new Runnable() {
			@Override public void run() {
				while (running) {
					try {
		
						eventBus.publish(inStream.readChar(), serverHandle);
						
					} catch (IOException e) {
						// Socket closed
						if (!(e instanceof SocketException && !running)) {
							break;
						}
					}
				}
			}
		}, "receive").start();	
	}
	
	public void close() {
		running = false;
		try {
			if (socket != null) socket.close();
			if (outStream != null) outStream.close(); 
			if (inStream != null) inStream.close();
			socket = null;
			outStream = null;
			inStream = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		// Double ensuring that the network is closed
		close();
	}	
}
