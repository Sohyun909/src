package temp;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class GameServer {
	public static void main(String[] args) {
		

		try {
			while (true) {
				// ���� ���� ����
				ServerSocket ss = new ServerSocket(5000);
				
				System.out.println("Ŭ���̾�Ʈ ���� �����");
				Socket client = ss.accept();

				// ��Ŵ�� ��ü
				MTServer ms = new MTServer(client);

				ms.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	


}
