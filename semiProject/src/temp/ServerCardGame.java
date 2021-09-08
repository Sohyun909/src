package temp;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import memoryGame.CardButton;
import temp.Server.ConnServer;
import vo.CardVO;

public class ServerCardGame extends JFrame implements ActionListener {
	CardButton[] btn;
	CardButton btnStart, btnNewG;
	JLabel lbPlayer1, lbPlayer2, lbScore1, lbScore2;
	JTextArea jta;
	EtchedBorder eborder;
	JPanel pnlCard, pnlScore;
	JScrollPane jsp;
	ImageIcon[] img;
	ServerSocket ss;
	ArrayList<ConnServer> list = new ArrayList<ConnServer>();
	String[] images = { "fruit01.png", "fruit02.png", "fruit03.png", "fruit04.png", "fruit05.png", "fruit06.png",
			"fruit07.png", "fruit08.png", "fruit09.png", "fruit10.png", "fruit11.png", "fruit12.png", "fruit13.png",
			"fruit14.png", "fruit15.png", "fruit16.png", };

	Timer timer;
	JTextField jtfScore;
	int score = 0; // 점수
	int successCnt = 0; // 성공횟수

	private static final int gameSize = 16;
	private int[] place;
	private CardButton beforeButton = null;
	private CardButton afterButton = null;
	private boolean isChecked = false;
	private ImageIcon questionImg = null;

	public ServerCardGame() {
		// 화면 중앙에 창 키기
		setTitle("같은 그림 찾기");
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();

		double width = d.getWidth();
		double height = d.getHeight();

		int x = (int) (width / 2 - 1200 / 2);
		int y = (int) (height / 2 - 680 / 2);

		// 컴포넌트 초기화
		pnlCard = new JPanel();
		pnlScore = new JPanel();
		lbPlayer1 = new JLabel("Turn");
		lbPlayer2 = new JLabel("Player 1", JLabel.CENTER);
		lbScore1 = new JLabel("Score");
		lbScore2 = new JLabel("50  :  50", JLabel.CENTER);
		btnStart = new CardButton("START");
		btnNewG = new CardButton("NEW GAME");
		eborder = new EtchedBorder(Color.black, Color.black);
		jtfScore = new JTextField("Score    :    " + score);
		jtfScore.setBounds(980, 200, 130, 30);
		add(jtfScore);

		jta = new JTextArea();
		jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		GridLayout grid = new GridLayout(4, 8);
		grid.setVgap(3);
		grid.setHgap(3);
		pnlCard.setLayout(grid);

		questionImg = imageSetSize("question.png");
		btn = new CardButton[gameSize]; // 게임사이즈(인원수)에 맞춰 버튼 개수 정하기
		for (int i = 0; i < btn.length; i++) { // 처음이미지를 question사진으로 배열사용해 넣어줌
			btn[i] = new CardButton();
			btn[i].setIcon(questionImg);
			btn[i].setIndex(i);

			btn[i].addActionListener(this);
			pnlCard.add(btn[i]);
		}

		lbPlayer2.setFont(new Font("고딕체", Font.BOLD, 20));
		lbScore2.setFont(new Font("고딕체", Font.BOLD, 20));
		lbPlayer2.setBorder(eborder);
		lbScore2.setBorder(eborder);

		pnlScore.setLayout(null);
		lbPlayer1.setBounds(1020, 20, 150, 30);
		lbPlayer2.setBounds(970, 60, 150, 50);
		lbScore1.setBounds(1020, 150, 150, 30);
		lbScore2.setBounds(970, 190, 150, 50);

		btnStart.setBounds(940, 490, 200, 50);
		btnNewG.setBounds(940, 560, 200, 50);

		pnlScore.setBounds(900, 0, 300, 640);
		pnlCard.setBounds(0, 0, 900, 640);

		jsp.setBounds(940, 250, 200, 230);

		pnlScore.add(lbPlayer1);
		pnlScore.add(lbPlayer2);
		pnlScore.add(lbScore1);
		pnlScore.add(lbScore2);
		pnlScore.add(btnStart);
		pnlScore.add(btnNewG);
		
		add(jsp);
		
		getContentPane().add(pnlCard);
		getContentPane().add(pnlScore);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(x, y, 1200, 680);
		setVisible(true);
		chatStart();
	}

	// 이미지 사이즈 조절 메서드
	private ImageIcon imageSetSize(String filename) {
		ImageIcon icon = new ImageIcon("./src/img/" + filename);
		Image oi = icon.getImage();
		Image changedImage = oi.getScaledInstance(110, 160, Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(changedImage);

		return newIcon;
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

		CardButton btn = (CardButton) e.getSource();
		btn.setIcon(btn.getCardVO().getImage());

		if (!isChecked) { // 첫번째로 버튼 눌렀을 때
			beforeButton = btn;
			isChecked = true;
		} else { // 두번째 버튼 눌렀을 때
					// 두번째 버튼이 다른 버튼일 경우(동일버튼 클릭 방지)
			if (beforeButton.getIndex() != btn.getIndex()) {
				afterButton = btn;
				// 맞췄을 때
				if (beforeButton.getCardVO().equals(btn.getCardVO())) {
					System.out.println("정답");
					btn.removeActionListener(this); // 지워서 맞추면 클릭 못하게 고정
					beforeButton.removeActionListener(this);
					score += 10; // 점수증가
					successCnt++; // 성공횟수증가

					jtfScore.setText("Score    :    " + score);

					if (successCnt == gameSize / 2) { // 모든카드 다 뒤집으면 종료메세지
						JOptionPane.showConfirmDialog(this, "Game Over", "게임끝!", JOptionPane.PLAIN_MESSAGE);
					}
				}
				// 틀렸을 때
				else { // 이전에 클릭한 버튼의 인덱스를 통해 다시 뒤집기
					back();
				}
				isChecked = false;
			}
		}
	}

	public void setGame() {
		place = new int[gameSize];

		makePlace();
		shuffle();
		setVoToButton(); // 카드 전부 보임
		Server server = new Server();

	}

	private void makePlace() {
		for (int i = 0; i < gameSize; i++) {
			place[i] = i;
		}
	}

	private void shuffle() { // 섞기
		Random rnd = new Random();
		for (int i = 0; i < 1000; i++) {
			int num = rnd.nextInt(gameSize);

			// 섞기
			int temp = place[0];
			place[0] = place[num];
			place[num] = temp;
		}
	}

	private void setVoToButton() {
		for (int i = 0; i < gameSize / 2; i++) {
			CardVO cardVO = new CardVO(imageSetSize(images[i]));
			btn[place[i * 2]].setCardVO(cardVO);
			btn[place[i * 2 + 1]].setCardVO(cardVO);
		}
	}

	// 짝이 안맞을시 다시 되돌리기
	public void back() {
		timer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("1초 후 뒤집기");

				afterButton.setIcon(questionImg);
				beforeButton.setIcon(questionImg);

				beforeButton = null;
				timer.stop();
			}
		});
		timer.start();
	}

	public static void main(String[] args) {
		ServerCardGame sc = new ServerCardGame();
	}
}
