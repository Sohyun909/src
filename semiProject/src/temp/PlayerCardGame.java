package temp;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

import memoryGame.CardButton;
import vo.CardVO;

public class PlayerCardGame extends JFrame implements ActionListener, Runnable, KeyListener, MouseListener {
	Image background;
	CardButton[] btn;
	JButton btnStart;
	
	JLabel lbPlayer1, lbPlayer2, lbScore1, lbScore2;
	JTextArea jta;
	EtchedBorder eborder;
	JPanel pnlCard, pnlScore;
	JScrollPane jsp;
	ImageIcon[] img;
	
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	String[] images = { "fruit01.png", "fruit02.png", "fruit03.png", "fruit04.png", "fruit05.png", "fruit06.png",
			"fruit07.png", "fruit08.png", "fruit09.png", "fruit10.png", "fruit11.png", "fruit12.png", "fruit13.png",
			"fruit14.png", "fruit15.png", "fruit16.png", };

	Timer timer;
	JTextField jtfScore, jtf;
	int score = 0; // 점수
	int successCnt = 0; // 성공횟수

	private static final int gameSize = 16;
	private int[] place;
	private CardButton beforeButton = null;
	private CardButton afterButton = null;
	private boolean isChecked = false;
	private ImageIcon questionImg = null;

	public PlayerCardGame() {
		// 화면 중앙에 창 키기
		setTitle("같은 그림 찾기");
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();

		double width = d.getWidth();
		double height = d.getHeight();

		int x = (int) (width / 2 - 1200 / 2);
		int y = (int) (height / 2 - 680 / 2);
		
		// 뒷배경 설정
				background = new ImageIcon(PlayerCardGame.class.getResource("../img/orgbackground.jpg")).getImage();
				// 판넬에 배경 넣기 (Extract Method)
				pnlScore = jpanelBackground();
				pnlCard = jpanelBackground();

		// 컴포넌트 초기화
		pnlCard = new JPanel();
		pnlScore = new JPanel();
		lbPlayer1 = new JLabel("Turn");
		lbPlayer2 = new JLabel("Player 1", JLabel.CENTER);
		lbScore1 = new JLabel("Score");
		lbScore2 = new JLabel("50  :  50", JLabel.CENTER);
		btnStart = new JButton(new ImageIcon(PlayerCardGame.class.getResource("../img/btnStart.png")));
		eborder = new EtchedBorder(Color.black, Color.black);
		jtfScore = new JTextField("Score    :    " + score);
		jtfScore.setBounds(980, 200, 130, 30);
		add(jtfScore);

		jta = new JTextArea();
		jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jtf = new JTextField();

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

			btn[i].addMouseListener(this);
			pnlCard.add(btn[i]);
		}

		// 폰트 설정
		Font f = new Font("고딕체", Font.BOLD, 20);

		lbPlayer1.setFont(f);
		lbPlayer2.setFont(f);
		lbScore1.setFont(f);
		lbPlayer1.setForeground(Color.white);
		lbPlayer2.setForeground(Color.white);
		lbScore1.setForeground(Color.white);

		pnlScore.setLayout(null);
		lbPlayer1.setBounds(1020, 20, 150, 30);
		lbPlayer2.setBounds(970, 60, 150, 50);
		lbScore1.setBounds(1020, 150, 150, 30);
		lbScore2.setBounds(970, 190, 150, 50);

		btnStart.setBounds(940, 490, 200, 50);

		pnlScore.setBounds(900, 0, 300, 640);
		pnlCard.setBounds(0, 0, 900, 640);

		jsp.setBounds(940, 250, 200, 210);
		jtf.setBounds(940, 465, 200, 20);

		// 컨테이너 담기
		add(lbPlayer1);
		add(lbPlayer2);
		add(lbScore1);
		add(lbScore2);
		add(btnStart);

		add(jsp);
		add(jtf);

		getContentPane().add(pnlCard);
		getContentPane().add(pnlScore);

		jtf.addKeyListener(this);
		btnStart.addActionListener(this);

		chatting();

		setTitle("같은 그림 찾기");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(x, y, 1200, 680);
		setVisible(true);
	}
	
	/**
	 * @return
	 */
	// 판넬에 배경넣기
	public JPanel jpanelBackground() {
		return new JPanel() {
			public void paintComponent(java.awt.Graphics g) {
				g.drawImage(background, 0, 0, 1200, 680, null);
			}
		};
	}

	// 이미지 사이즈 조절 메서드
	private ImageIcon imageSetSize(String filename) {
		ImageIcon icon = new ImageIcon("./src/img/" + filename);
		Image oi = icon.getImage();
		Image changedImage = oi.getScaledInstance(110, 160, Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(changedImage);

		return newIcon;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Start");
		pw.println("Ready");
		pw.flush();

		
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

	public void chatting() {
		Thread th = new Thread(this);
		th.start();
	}

	// 클라이언트가 동시에 실행할 코드
	@Override
	public void run() {
		try {
			s = new Socket("192.168.0.4", 5000);
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

	public static void main(String[] args) {
		PlayerCardGame pg = new PlayerCardGame();

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

	@Override
	public void mouseClicked(MouseEvent e) {
		
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

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}