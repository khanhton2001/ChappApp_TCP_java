package ChatRoomTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	private InetAddress host ; 
	private int port ; 
	
	public Client(InetAddress host , int port) {
		this.host = host ; 
		this.port = port ;
		
	}
	
	private void execute() throws IOException {
		Socket client = new Socket(host , port) ;
		ReadClient read = new ReadClient(client) ; 
		read.start(); 
		WriteClient write = new WriteClient(client) ; 
		write.start();
				
	}
	
	public static void main(String[] args) throws IOException {
		Client client = new Client(InetAddress.getLocalHost(),15797) ;
		client.execute();
	}
		
}


class ReadClient extends Thread{
	private Socket client; 
	
	public ReadClient(Socket client) {
		this.client = client ; 
	}   
	
	@Override
	public void run() {
		DataInputStream dis = null ;  
		try {
			dis = new DataInputStream(client.getInputStream());
			while(true) {
				String sms = dis.readUTF();
				System.out.println(sms);
			}
			
		} catch(Exception e) {
			try {
				dis.close();
				client.close(); 
				
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				System.out.println("Disconnected");
			}
			
		}
	}
	
}

class WriteClient extends Thread{
	private Socket client ; 
	
	public WriteClient(Socket client) {
		this.client = client ; 
	}
	
	public void run() {
		DataOutputStream dos = null ; 
		Scanner sc = null ; 
		try {			
			dos = new DataOutputStream(client.getOutputStream()) ;
			sc = new Scanner(System.in) ; 
			while(true) {
				String sms = sc.nextLine() ; 
				dos.writeUTF(sms);
			}
		} catch(Exception e) {
			try {
				dos.close();
				client.close(); 
				
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				System.out.println("Disconnected");
			}
		
		}
		
	}
	
}