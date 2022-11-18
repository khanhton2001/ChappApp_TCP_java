package ChatRoomTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException ; 
import java.net.ServerSocket ; 
import java.net.Socket       ;
import java.util.ArrayList;
import java.util.Scanner; 


public class Server {
	private int port ; 
	public static ArrayList<Socket> listSK ;
	
	public Server(int port) {
		this.port = port ; 
	}
	
	private void execute() throws IOException {
		ServerSocket server = new ServerSocket(port) ;
		WriteServer write = new WriteServer() ;  
		write.start(); 
		System.out.println("Server is listening...");
		while(true) {
			Socket socket = server.accept(); 
			System.out.println("Connected with" + socket);
			Server.listSK.add(socket)         ; 
			ReadServer read = new ReadServer(socket) ; 
			read.start(); 
			 
			
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		Server.listSK = new ArrayList<>() ;
		Server server = new Server(15797) ; 
		server.execute();
	}

}

class ReadServer extends Thread{
	private Socket server ; 
	
	public ReadServer (Socket server) {
		this.server = server ; 
	}
	
	public void run() {
		DataInputStream dis = null ; 
		try {
			dis = new DataInputStream(server.getInputStream()) ;
			while(true) {
				String sms = dis.readUTF() ; 
				for(Socket item : Server.listSK) {
					DataOutputStream dos = new DataOutputStream(item.getOutputStream());
				    dos.writeUTF(sms);
				}
				System.out.println(sms) ; 
			}
	
		}  catch(Exception e) {
			try {
				dis.close(); 
				server.close() ; 
				
			} catch(IOException ex) {
				System.out.println("Disconnected");
			}
		}
	}
	
}

class WriteServer extends Thread{
 
	
	
	public void run() {
		DataOutputStream dos = null ;
		Scanner sc = new Scanner(System.in) ; 
		while(true) {
			String sms = sc.nextLine() ; 
		try {	
			for(Socket item : Server.listSK) {
				 dos = new DataOutputStream(item.getOutputStream()) ; 
				 dos.writeUTF(sms) ; 
				
			} 
		} catch(IOException e) {
			e.printStackTrace();
		}
			
		}
		
		
	}
	
	
}



