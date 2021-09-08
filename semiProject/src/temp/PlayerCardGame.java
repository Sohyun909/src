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
	int score = 0; // ����
	int successCnt = 0; // ����Ƚ��

	private static final int gameSize = 16;
	private int[] place;
	private CardButton beforeButton = null;
	private CardButton afterButton = null;
	private boolean isChecked = false;
	private ImageIcon questionImg = null;

	public PlayerCardGame() {
		// ȭ�� �߾ӿ� â Ű��
		setTitle("���� �׸� ã��");
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();

		double width = d.getWidth();
		double height = d.getHeight();

		int x = (int) (width / 2 - 1200 / 2);
		int y = (int) (height / 2 - 680 / 2);
		
		// �޹�� ����
				background = new ImageIcon(PlayerCardGame.class.getResource("../img/orgbackground.jpg")).getImage();
				// �ǳڿ� ��� �ֱ� (Extract Method)
				pnlScore = jpanelBackground();
				pnlCard = jpanelBackground();

		// ������Ʈ �ʱ�ȭ
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
		btn = new CardButton[gameSize]; // ���ӻ�����(�ο���)�� ���� ��ư ���� ���ϱ�
		for (int i = 0; i < btn.length; i++) { // ó���̹����� question�������� �迭����� �־���
			btn[i] = new CardButton();
			btn[i].setIcon(questionImg);
			btn[i].setIndex(i);

			btn[i].addMouseListener(this);
			pnlCard.add(btn[i]);
		}

		// ��Ʈ ����
		Font f = new Font("���ü", Font.BOLD, 20);

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

		// �����̳� ���
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

		setTitle("���� �׸� ã��");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(x, y, 1200, 680);
		setVisible(true);
	}
	
	/**
	 * @return
	 */
	// �ǳڿ� ���ֱ�
	public JPanel jpanelBackground() {
		return new JPanel() {
			public void paintComponent(java.awt.Graphics g) {
				g.drawImage(background, 0, 0, 1200, 680, null);
			}
		};
	}

	// �̹��� ������ ���� �޼���
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
		setVoToButton(); // ī�� ���� ����
		Server server = new Server();

	}

	private void makePlace() {
		for (int i = 0; i < gameSize; i++) {
			place[i] = i;
		}
	}

	private void shuffle() { // ����
		Random rnd = new Random();
		for (int i = 0; i < 1000; i++) {
			int num = rnd.nextInt(gameSize);

			// ����
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

	// ¦�� �ȸ����� �ٽ� �ǵ�����
	public void back() {
		timer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("1�� �� ������");

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

	// Ŭ���̾�Ʈ�� ���ÿ� ������ �ڵ�
	@Override
	public void run() {
		try {
			s = new Socket("192.168.0.4", 5000);
			jta.append("���� ���� �Ϸ� \n");

			// ������ ���
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));

			String msg = null;
			while ((msg = br.readLine()) != null) {
				jta.append(msg + "\n");

				// ��ũ�� ���� �Ʒ��� ����
				jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
			}

		} catch (UnknownHostException e) {
			System.out.println("���� ip �ּҸ� �ٽ� Ȯ���ϼ���");
		} catch (IOException e) {
			System.out.println("���� ����");
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
		// ���� ������ ä�� ����
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

		if (!isChecked) { // ù��°�� ��ư ������ ��
			beforeButton = btn;
			isChecked = true;
		} else { // �ι�° ��ư ������ ��
					// �ι�° ��ư�� �ٸ� ��ư�� ���(���Ϲ�ư Ŭ�� ����)
			if (beforeButton.getIndex() != btn.getIndex()) {
				afterButton = btn;
				// ������ ��
				if (beforeButton.getCardVO().equals(btn.getCardVO())) {
					System.out.println("����");
					btn.removeActionListener(this); // ������ ���߸� Ŭ�� ���ϰ� ����
					beforeButton.removeActionListener(this);
					score += 10; // ��������
					successCnt++; // ����Ƚ������

					jtfScore.setText("Score    :    " + score);

					if (successCnt == gameSize / 2) { // ���ī�� �� �������� ����޼���
						JOptionPane.showConfirmDialog(this, "Game Over", "���ӳ�!", JOptionPane.PLAIN_MESSAGE);
					}
				}
				// Ʋ���� ��
				else { // ������ Ŭ���� ��ư�� �ε����� ���� �ٽ� ������
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