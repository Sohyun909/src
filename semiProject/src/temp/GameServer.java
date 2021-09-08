package temp;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class GameServer {
	public static void main(String[] args) {
		

		try {
			while (true) {
				// 서버 소켓 생성
				ServerSocket ss = new ServerSocket(5000);
				
				System.out.println("클라이언트 접속 대기중");
				Socket client = ss.accept();

				// 통신담당 객체
				MTServer ms = new MTServer(client);

				ms.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	


}
