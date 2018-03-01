package edu.odu.jrugh001.yellowclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServerHandle {
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	
	public String readString() {
		try {
			return inStream.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte readByte() {
		try {
			return inStream.readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0x0;
	}
	
	public Object readObject() {
		try {
			return inStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
