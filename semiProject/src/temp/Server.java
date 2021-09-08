package temp;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame implements ActionListener {
	JButton jbtn;
	JTextArea jta;
	JScrollPane jsp;
	ServerSocket ss;
	ArrayList<ConnServer> list = new ArrayList<ConnServer>();

	public Server() {
		super("Chat Server");

		// 컴포넌트 초기화
		jbtn = new JButton("종료");
		jta = new JTextArea();
		jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// 폰트 설정
		Font f = new Font("굴림체", Font.BOLD, 18);
		jta.setFont(f);

		// 컴포넌트 컨테이너에 담기
		add(jbtn, "South");
		add(jsp, "Center");

		// 서버 실행창 화면 중앙에 배치
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		int x = (int) (width / 2 - 400 / 2);
		int y = (int) (height / 2 - 600 / 2);

		// 컴포넌트에 액션 감지기 부착
		jbtn.addActionListener(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(x, y, 400, 600);
		setVisible(true);

		// JTextArea 수정 x
		jta.setEditable(false);

		// 채팅 메서드 실행
		chatStart();
	}

	// 클라이언트 접속 메서드
	public void chatStart() {
		try {
			ss = new ServerSocket(5000);
			jta.append("서버 시작 \n");

			while (true) {
				jta.append("클라이언트 접속 대기중 \n");
				Socket client = ss.accept();
				// 클라이언트 접속하면 ip띄우기
				jta.append("ip : " + client.getInetAddress().getHostAddress() + "접속 \n");

				// 통신 담당 서버에게 접속한 클라이언트를 넘기기
				ConnServer cs = new ConnServer(client);
				list.add(cs); // 접속된 클라이언트를 ArrayList에 담기
				cs.start(); // 채팅 시작

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 통신을 담당할 서버
	public class ConnServer extends Thread {
		Socket client;
		BufferedReader br;
		PrintWriter pw;
		String ip;

		ConnServer(Socket client) {
			this.client = client;

			// 1. 접속 사용자의 ip 출력
			ip = client.getInetAddress().getHostAddress();
			jta.append("현재 서버상태 : " + ip + "접속 \n"); // 접속시 서버창에 알림
			// 클라이언트가 접속할때 모든 클라이언트에게 알림
			broadcast(ip + " 님이 입장하였습니다");

			try {
				// 2. 수신부
				br = new BufferedReader(new InputStreamReader(client.getInputStream()));

				// 3. 발신부
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));

			} catch (IOException e) {
				e.printStackTrace();
			}

		}// ConnServer 생성자 end

		// 동시 처리할 코드 (여러 클라이언트와 통신 연결)
		@Override
		public void run() {
			// 4. 읽기 쓰기 반복
			try {
				while (true) {
					String msg = null;
					msg = br.readLine(); // 클라이언트의 채팅을 빠르게 읽기

					String[] nameChange = msg.split("/");

					if (nameChange[0].equals("닉네임")) {
						String firstIp = ip;
						ip = nameChange[1];
						jta.append(firstIp + " 닉네임 변경됨 -> " + ip);
						broadcast(firstIp + " 닉네임 변경됨 -> " + ip);
					} else {
						jta.append("[" + ip + "] : " + msg + "\n"); // ip와 함께 출력(서버창에)

						// 스크롤 제일 아래로 자동 설정
						jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());

						// ip와 채팅을 서버에 접속한 클라이언트에게 전달
						broadcast("[" + ip + "] : " + msg);
					}
				}
			} catch (IOException e) {
				broadcast(ip + " 님이 채팅방을 나갔습니다");
				list.remove(this);
				jta.append(ip + " 접속 종료 \n");
			}
		}

		public void broadcast(String msg) {
			for (ConnServer x : list) {
				x.pw.println(msg);
				x.pw.flush();
			}
		}

	}// ConnServer class end

	@Override
	public void actionPerformed(ActionEvent e) {
		// 종료 버튼 누르면 채팅 서버창 닫기
		if (e.getSource() == jbtn) {
			System.out.println("서버 종료");
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
	}
}// server class end
