package temp;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;

public class MTServer extends Thread {
	
	Socket client;
	
	
	
	public MTServer(Socket client) {
		super();
		this.client = client;
	}
	
		
	@Override
	public void run() {
		
		// 立加 蜡历 ip 
		System.out.println("client IP : "+client.getInetAddress().getHostAddress());
		String ip = client.getInetAddress().getHostAddress();
		
		// 荐脚何
		try {
			BufferedReader br = new BufferedReader(
									new InputStreamReader(
										client.getInputStream()));
			
		// 惯脚何
			PrintWriter pw = new PrintWriter(
								new BufferedWriter(
									new OutputStreamWriter(
										client.getOutputStream())));
			
			String ready;
			
			while ((ready = br.readLine()) != null) {
				System.out.println("[ "+ip+" ]"+ready);
				pw.println(ready);
				pw.flush();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
