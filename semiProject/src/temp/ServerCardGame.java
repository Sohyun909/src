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
	int score = 0; // ����
	int successCnt = 0; // ����Ƚ��

	private static final int gameSize = 16;
	private int[] place;
	private CardButton beforeButton = null;
	private CardButton afterButton = null;
	private boolean isChecked = false;
	private ImageIcon questionImg = null;

	public ServerCardGame() {
		// ȭ�� �߾ӿ� â Ű��
		setTitle("���� �׸� ã��");
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();

		double width = d.getWidth();
		double height = d.getHeight();

		int x = (int) (width / 2 - 1200 / 2);
		int y = (int) (height / 2 - 680 / 2);

		// ������Ʈ �ʱ�ȭ
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
		btn = new CardButton[gameSize]; // ���ӻ�����(�ο���)�� ���� ��ư ���� ���ϱ�
		for (int i = 0; i < btn.length; i++) { // ó���̹����� question�������� �迭����� �־���
			btn[i] = new CardButton();
			btn[i].setIcon(questionImg);
			btn[i].setIndex(i);

			btn[i].addActionListener(this);
			pnlCard.add(btn[i]);
		}

		lbPlayer2.setFont(new Font("���ü", Font.BOLD, 20));
		lbScore2.setFont(new Font("���ü", Font.BOLD, 20));
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

	// �̹��� ������ ���� �޼���
	private ImageIcon imageSetSize(String filename) {
		ImageIcon icon = new ImageIcon("./src/img/" + filename);
		Image oi = icon.getImage();
		Image changedImage = oi.getScaledInstance(110, 160, Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(changedImage);

		return newIcon;
	}

	// Ŭ���̾�Ʈ ���� �޼���
	public void chatStart() {
		try {
			ss = new ServerSocket(5000);
			jta.append("���� ���� \n");

			while (true) {
				jta.append("Ŭ���̾�Ʈ ���� ����� \n");
				Socket client = ss.accept();
				// Ŭ���̾�Ʈ �����ϸ� ip����
				jta.append("ip : " + client.getInetAddress().getHostAddress() + "���� \n");

				// ��� ��� �������� ������ Ŭ���̾�Ʈ�� �ѱ��
				ConnServer cs = new ConnServer(client);
				list.add(cs); // ���ӵ� Ŭ���̾�Ʈ�� ArrayList�� ���
				cs.start(); // ä�� ����

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ����� ����� ����
	public class ConnServer extends Thread {
		Socket client;
		BufferedReader br;
		PrintWriter pw;
		String ip;

		ConnServer(Socket client) {
			this.client = client;

			// 1. ���� ������� ip ���
			ip = client.getInetAddress().getHostAddress();
			jta.append("���� �������� : " + ip + "���� \n"); // ���ӽ� ����â�� �˸�
			// Ŭ���̾�Ʈ�� �����Ҷ� ��� Ŭ���̾�Ʈ���� �˸�
			broadcast(ip + " ���� �����Ͽ����ϴ�");

			try {
				// 2. ���ź�
				br = new BufferedReader(new InputStreamReader(client.getInputStream()));

				// 3. �߽ź�
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));

			} catch (IOException e) {
				e.printStackTrace();
			}

		}// ConnServer ������ end

		// ���� ó���� �ڵ� (���� Ŭ���̾�Ʈ�� ��� ����)
		@Override
		public void run() {
			// 4. �б� ���� �ݺ�
			try {
				while (true) {
					String msg = null;
					msg = br.readLine(); // Ŭ���̾�Ʈ�� ä���� ������ �б�

					String[] nameChange = msg.split("/");

					if (nameChange[0].equals("�г���")) {
						String firstIp = ip;
						ip = nameChange[1];
						jta.append(firstIp + " �г��� ����� -> " + ip);
						broadcast(firstIp + " �г��� ����� -> " + ip);
					} else {
						jta.append("[" + ip + "] : " + msg + "\n"); // ip�� �Բ� ���(����â��)

						// ��ũ�� ���� �Ʒ��� �ڵ� ����
						jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());

						// ip�� ä���� ������ ������ Ŭ���̾�Ʈ���� ����
						broadcast("[" + ip + "] : " + msg);
					}
				}
			} catch (IOException e) {
				broadcast(ip + " ���� ä�ù��� �������ϴ�");
				list.remove(this);
				jta.append(ip + " ���� ���� \n");
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

	public static void main(String[] args) {
		ServerCardGame sc = new ServerCardGame();
	}
}
