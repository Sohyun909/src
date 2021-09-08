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

		// ������Ʈ �ʱ�ȭ
		jbtnSend = new JButton("����");
		jbtnExit = new JButton("ä�� ����");
		jta = new JTextArea();
		jtf = new JTextField(30);
		jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jp = new JPanel();

		// ��Ʈ ����
		Font f = new Font("����ü", Font.BOLD, 18);
		jta.setFont(f);

		// ������Ʈ ���
		jp.add(jtf);
		jp.add(jbtnSend);
		jp.add(jbtnExit);
		add(jp, "South");
		add(jsp, "Center");

		// �׼� ������ ����
		jbtnSend.addActionListener(this);
		jbtnExit.addActionListener(this);
		jtf.addKeyListener(this);

		chatting(); //ä�� �޼��� ����

		// Ŭ���̾�Ʈ ����â ȭ�� �߾ӿ� ��ġ
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension d = tool.getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		int x = (int) (width / 2 - 460 / 2);
		int y = (int) (height / 2 - 600 / 2);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(x, y, 460, 600);
		setVisible(true);

		// JTexArea ����x
		jta.setEditable(false);

		// ä���Է� �ڵ� Ŀ��
		jtf.requestFocusInWindow();

	}

	public void chatting() {
		Thread th = new Thread(this);
		th.start();
	}

	// Ŭ���̾�Ʈ�� ���ÿ� ������ �ڵ�
	@Override
	public void run() {
		try {
			s = new Socket("192.168.0.2", 5000);
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

	@Override
	public void actionPerformed(ActionEvent e) {

		// ���۹�ư ������ ä�� ����
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

	public static void main(String[] args) {
		Client client = new Client();
	}

}
