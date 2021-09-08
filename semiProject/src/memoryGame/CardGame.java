package memoryGame;


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
import java.util.Random;

import javax.swing.Action;
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

import vo.CardVO;

public class CardGame extends JFrame implements ActionListener, MouseListener {
	Socket s;
	BufferedReader br;
	PrintWriter pw;

	Image background1, background2;
	CardButton[] btn;
	JButton btnStart;

	JPanel pnlReady, pnlCard, pnlScore;
	JLabel lbPlayer1, lbPlayer2, lbScore1, lbScore2;

	EtchedBorder eborder;

	ImageIcon[] img;
	String[] images = { "fruit01.png", "fruit02.png", "fruit03.png", "fruit04.png", "fruit05.png", "fruit06.png",
			"fruit07.png", "fruit08.png", "fruit09.png", "fruit10.png", "fruit11.png", "fruit12.png", "fruit13.png",
			"fruit14.png", "fruit15.png", "fruit16.png", };

	Timer timer;
	int score = 0; // 점수
	int successCnt = 0; // 성공횟수

	private static int gameSize = 16;
	private int[] place;
	private CardButton beforeButton = null;
	private CardButton afterButton = null;
	private boolean isChecked = false;
	private ImageIcon questionImg = null;

	public CardGame() {

		// 화면 중앙에 창 키기
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();

		double width = d.getWidth();
		double height = d.getHeight();

		int x = (int) (width / 2 - 800 / 2);
		int y = (int) (height / 2 - 600 / 2);

		// 뒷배경 설정
		background1 = new ImageIcon(CardGame.class.getResource("../img/orgbackground.jpg")).getImage();
		background2 = new ImageIcon(CardGame.class.getResource("../img/door2.jpg")).getImage();
		// 판넬에 배경 넣기 (Extract Method)
		pnlScore = jpanelBackground(background1);
		pnlCard = jpanelBackground(background1);
		pnlReady = jpanelBackground(background2);
		

		// 컴포넌트 초기화
		lbPlayer1 = new JLabel("Turn");
		lbPlayer2 = new JLabel("Player 1", JLabel.CENTER);
		lbScore1 = new JLabel("Score");
		lbScore2 = new JLabel("0", JLabel.CENTER);
		btnStart = new JButton(new ImageIcon(CardGame.class.getResource("../img/btnStart.png")));
		eborder = new EtchedBorder(Color.GRAY, Color.GRAY);
	


		GridLayout grid = new GridLayout(4, 8);
		grid.setVgap(3);
		grid.setHgap(3);
		pnlCard.setLayout(grid);

		questionImg = imageSetSize("questionblack.png");
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
		lbScore2.setFont(f);
		lbPlayer1.setForeground(Color.white);
		lbPlayer2.setForeground(Color.white);
		lbScore1.setForeground(Color.white);
		lbScore2.setForeground(Color.white);

		lbPlayer2.setBorder(eborder);
		lbScore2.setBorder(eborder);
		

		// 레이아웃 지정
		lbPlayer1.setBounds(1020, 20, 150, 30);
		lbPlayer2.setBounds(970, 60, 150, 50);
		lbScore1.setBounds(1020, 120, 150, 30);
		lbScore2.setBounds(970, 160, 150, 50);
		btnStart.setBounds(940, 550, 200, 50);
		pnlCard.setBounds(0, 0, 900, 660);
		pnlReady.setBounds(0, 0, 600, 680);
		pnlScore.setBounds(900, 0, 300, 680);


		// 컨테이너 담기
		add(lbPlayer1);
		add(lbPlayer2);
		add(lbScore1);
		add(lbScore2);
		add(btnStart);
		getContentPane().add(pnlCard);
		getContentPane().add(pnlScore);
		getContentPane().add(pnlReady);

		btnStart.addActionListener(this);
		pnlCard.setVisible(false);
		pnlReady.setVisible(true);

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
	public JPanel jpanelBackground(Image img) {
		return new JPanel() {
			public void paintComponent(java.awt.Graphics g) {
				g.drawImage(img, 0, 0, 900, 680, null);
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

		if (e.getSource() == btnStart) {
			pnlReady.setVisible(false);
			pnlCard.setVisible(true);
			

		}

	}

	public void setGame() {
		place = new int[gameSize];

		makePlace();
		shuffle();
		setVoToButton(); // 카드 전부 보임

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

	

	@Override

	public void mouseClicked(MouseEvent e) {

		CardButton btn = (CardButton) e.getSource();
		btn.setIcon(btn.getCardVO().getImage());

		if (e.getSource() == btn) {

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

						lbScore2.setText(score+"");

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
