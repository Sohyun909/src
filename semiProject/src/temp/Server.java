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

		// ������Ʈ �ʱ�ȭ
		jbtn = new JButton("����");
		jta = new JTextArea();
		jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// ��Ʈ ����
		Font f = new Font("����ü", Font.BOLD, 18);
		jta.setFont(f);

		// ������Ʈ �����̳ʿ� ���
		add(jbtn, "South");
		add(jsp, "Center");

		// ���� ����â ȭ�� �߾ӿ� ��ġ
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		int x = (int) (width / 2 - 400 / 2);
		int y = (int) (height / 2 - 600 / 2);

		// ������Ʈ�� �׼� ������ ����
		jbtn.addActionListener(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(x, y, 400, 600);
		setVisible(true);

		// JTextArea ���� x
		jta.setEditable(false);

		// ä�� �޼��� ����
		chatStart();
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
		// ���� ��ư ������ ä�� ����â �ݱ�
		if (e.getSource() == jbtn) {
			System.out.println("���� ����");
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
	}
}// server class end
