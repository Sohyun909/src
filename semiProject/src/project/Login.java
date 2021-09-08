package project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.MemberDAO;

public class Login extends JFrame implements ActionListener {
	
	JPanel jp;
	JLabel jlId, jlPw;
	JTextField jtfId;
	JPasswordField jpfPw;
	JButton jbtnLogin, jbtnRegister;
	
	// �α��� ��� �̹���
	Image background = new ImageIcon
            (Login.class.getResource("../img/backgroundMain.jpg")).getImage();
	

	Login() {

		// ������Ʈ ����
		jp = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// g.drawImage( Image��ü, X��ǥ, Y��ǥ, �ʺ�, ����, ImageObserver )
				g.drawImage(background, 0, 0, 1024, 768, null);
			}
		};

		jlId = new JLabel("ID");
		jlPw = new JLabel("PW");
		jtfId = new JTextField();
		jpfPw = new JPasswordField();
		jbtnLogin = new JButton(new ImageIcon("src/img/btnLogin.jpg"));
		jbtnRegister = new JButton("ȸ������");
		

		// ������Ʈ ��ġ �� ũ��
		jp.setBounds(0, 0, 1024, 768);
		jlId.setBounds(360, 310, 80, 50);
		jlPw.setBounds(360, 370, 80, 50);
		jtfId.setBounds(410, 310, 270, 50);
		jpfPw.setBounds(410, 370, 270, 50);
		jbtnLogin.setBounds(360, 430, 320, 50);
		jbtnRegister.setBounds(450, 500, 100, 30);
		
		
		// ������Ʈ ����
		jp.setLayout(null);
		
		jlId.setFont(new Font("���ü", Font.BOLD, 20));
		jlPw.setFont(new Font("���ü", Font.BOLD, 20));
		jlId.setForeground(Color.white);
		jlPw.setForeground(Color.white);
		
		jtfId.setFont(new Font("���ü", Font.BOLD, 20));
		jpfPw.setFont(new Font("���ü", Font.BOLD, 20));
		
		jbtnRegister.setBorderPainted(false);		// ȸ������ ��ư ��輱 ����
		jbtnRegister.setOpaque(false);				// setOpaque : ������ ����
		jbtnRegister.setContentAreaFilled(false);	// setContentAreaFilled : ä���ֱ� ����
		// setOpaque, setContentAreaFilled �� ��� ��Ȱ��ȭ�ؾ� ��ư ����ȭ ����
		
		jbtnRegister.setFont(new Font("���ü", Font.BOLD, 15));
		jbtnRegister.setForeground(Color.white);
		
		jbtnLogin.setFont(new Font("���ü", Font.BOLD, 15));
		jbtnLogin.setForeground(Color.white);
		jbtnLogin.setFocusPainted(false);		// ��Ŀ�� ǥ�� ����
		
		// ������Ʈ ����
		jp.add(jlId);
		jp.add(jlPw);
		jp.add(jtfId);
		jp.add(jpfPw);
		jp.add(jbtnLogin);
		jp.add(jbtnRegister);
		add(jp);
		
		// �̺�Ʈ ó��
		jbtnLogin.addActionListener(this);
		jbtnRegister.addActionListener(this);
		
		
		setTitle("�α���");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1024, 768);
		setLocationRelativeTo(null);	// â ��ġ ȭ�� �߾�����
		setLayout(null);
		setResizable(false);	// â ũ�� ���� �Ұ�
		setVisible(true);
		
	}// Login() end
	
	
	public static void main(String[] args) {
		new Login();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		// �α��� ��ư Ŭ��
		if(e.getSource() == jbtnLogin) {
		
			// �Է¹��� ID, PW 
			String id = jtfId.getText();
			String pw = jpfPw.getText();

			// DB ����
			MemberDAO dao = new MemberDAO();
			
			// �α��� �㰡 ����
			boolean loginPass = dao.exist(id, pw);
			
			// DB ���� ����
			dao.close();
			
			
			// �α��� ���� (ID, PW ��ġ)
			if(loginPass) {
				
				// �޴�â ����
				Menu m = new Menu(id);
				
				// ���� â ��Ȱ��ȭ
				this.setVisible(false);
			}
			// �α��� ���� (ID, PW ����ġ or �������� ����)
			else {
				JOptionPane.showConfirmDialog(this, 
						"���̵� �Ǵ� ��й�ȣ�� �߸� �Է� �Ǿ����ϴ�.\n", "�α��� ����",
						JOptionPane.PLAIN_MESSAGE);
			}
			
		}
		// ȸ������ ��ư Ŭ��
		else if(e.getSource() == jbtnRegister) {
			
			// ȸ������ â ����
			Register rg = new Register();
			
		}
		
	}// actionPerformed() end
	
}// Login class end

