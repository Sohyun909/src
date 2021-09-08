package temp;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements Runnable, ActionListener, KeyListener {

	JButton jbtnSend , jbtnExit;
	JTextArea jta;
	JTextField jtf;
	JScrollPane jsp;
	JPanel jp;

	Socket s;
	BufferedReader br;
	PrintWriter pw;

	public Client() {

		super("Chat Client");

		// 컴포넌트 초기화
		jbtnSend = new JButton("전송");
		jbtnExit = new JButton("채팅 종료");
		jta = new JTextArea();
		jtf = new JTextField(30);
		jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jp = new JPanel();

		// 폰트 설정
		Font f = new Font("굴림체", Font.BOLD, 18);
		jta.setFont(f);

		// 컴포넌트 담기
		jp.add(jtf);
		jp.add(jbtnSend);
		jp.add(jbtnExit);
		add(jp, "South");
		add(jsp, "Center");

		// 액션 감지기 부착
		jbtnSend.addActionListener(this);
		jbtnExit.addActionListener(this);
		jtf.addKeyListener(this);

		chatting(); //채팅 메서드 시작

		// 클라이언트 실행창 화면 중앙에 배치
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		int x = (int) (width / 2 - 460 / 2);
		int y = (int) (height / 2 - 600 / 2);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(x, y, 460, 600);
		setVisible(true);

		// JTexArea 수정x
		jta.setEditable(false);

		// 채팅입력 자동 커서
		jtf.requestFocusInWindow();

	}

	public void chatting() {
		Thread th = new Thread(this);
		th.start();
	}

	// 클라이언트가 동시에 실행할 코드
	@Override
	public void run() {
		try {
			s = new Socket("192.168.0.2", 5000);
			jta.append("서버 접속 완료 \n");

			// 서버와 통신
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));

			String msg = null;
			while ((msg = br.readLine()) != null) {
				jta.append(msg + "\n");

				// 스크롤 제일 아래로 설정
				jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
			}

		} catch (UnknownHostException e) {
			System.out.println("서버 ip 주소를 다시 확인하세요");
		} catch (IOException e) {
			System.out.println("접속 실패");
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// 전송버튼 누르면 채팅 전송
		if (e.getSource() == jbtnSend) {
			String msg = jtf.getText().trim();
			pw.println(msg);
			pw.flush();
			jtf.setText("");
		}else if(e.getSource() == jbtnExit) {
			System.exit(0);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// 엔터 누르면 채팅 전송
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			String msg = jtf.getText().trim();
			pw.println(msg);
			pw.flush();
			jtf.setText("");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public static void main(String[] args) {
		Client client = new Client();
	}

}
