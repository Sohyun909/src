package project;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.FileDialog;
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
import vo.MemberVO;

public class Register extends JFrame implements ActionListener {
	
	JPanel jp;
	JLabel jlId, jlPw, jlPw2, jlName, jlEmail, jlImg;
	JTextField jtfId, jtfName, jtfEmail, jtfImg;
	JPasswordField jpfPw, jpfPw2;
	JButton jbtnCheckid, jbtnImg, jbtnJoin, jbtnCancel;
	Login main;
	
	// ȸ������ ��� �̹��� 
	Image background = new ImageIcon
			(Register.class.getResource("../img/backgroundRegister.jpg")).getImage();

	boolean checkId = false;	// ID �ߺ��˻� ��� ����
	
	
	Register() {

		super("Login");
		this.main = main;
		
		// ������Ʈ ����
		jp = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(background, 0, 0, 500, 800, null);
			}
		};
		
		jlId = new JLabel("���̵�");
		jtfId = new JTextField();
		jbtnCheckid = new JButton(new ImageIcon("src/img/btnid.jpg"));
		
		jlPw = new JLabel("��й�ȣ");
		jpfPw = new JPasswordField();
		jlPw2 = new JLabel("��й�ȣ Ȯ��");
		jpfPw2 = new JPasswordField();
		
		jlName = new JLabel("�̸�");
		jtfName = new JTextField();
		
		jlEmail = new JLabel("E-mail");
		jtfEmail = new JTextField();
		
		jlImg = new JLabel("������ ����");
		jtfImg = new JTextField();
		jbtnImg = new JButton(new ImageIcon("src/img/btnImg.jpg"));
		
		jbtnJoin = new JButton(new ImageIcon("src/img/btnJoin.jpg"));
		jbtnCancel = new JButton(new ImageIcon("src/img/btnCancel.jpg"));
		
		
		// ������Ʈ ��ġ �� ũ��
		jp.setLayout(null);
		jp.setBounds(0,0,500,800);
		
		jlId.setBounds(30, 50, 80, 50);
		jtfId.setBounds(150, 60, 190, 30);
		jbtnCheckid.setBounds(360, 60, 100, 30);
		
		jlPw.setBounds(30, 100, 80, 50);
		jpfPw.setBounds(150, 110, 190, 30);
		
		jlPw2.setBounds(30, 150, 100, 50);
		jpfPw2.setBounds(150, 160, 190, 30);
		
		jlName.setBounds(30, 300, 100, 50);
		jtfName.setBounds(150, 310, 190, 30);
		
		jlEmail.setBounds(30, 350, 100, 50);
		jtfEmail.setBounds(150, 360, 190, 30);
		
		jlImg.setBounds(30, 400, 100, 50);
		jtfImg.setBounds(150, 410, 190, 30);
		jbtnImg.setBounds(360, 410, 100, 30);
		
		jbtnJoin.setBounds(90, 650, 130, 40);
		jbtnCancel.setBounds(270, 650, 130, 40);
		
		
		// ������Ʈ ����
		jlId.setFont(new Font("���ü", Font.BOLD, 15));
		jlId.setForeground(Color.WHITE);
		jtfId.setFont(new Font("���ü", Font.BOLD, 15));
		
		jlPw.setFont(new Font("���ü", Font.BOLD, 15));
		jlPw.setForeground(Color.WHITE);
		jpfPw.setFont(new Font("���ü", Font.BOLD, 15));
		
		jlPw2.setFont(new Font("���ü", Font.BOLD, 15));
		jlPw2.setForeground(Color.WHITE);
		jpfPw2.setFont(new Font("���ü", Font.BOLD, 15));
		
		jlName.setFont(new Font("���ü", Font.BOLD, 15));
		jlName.setForeground(Color.WHITE);
		jtfName.setFont(new Font("���ü", Font.BOLD, 15));
		
		jlEmail.setFont(new Font("���ü", Font.BOLD, 15));
		jlEmail.setForeground(Color.WHITE);
		jtfEmail.setFont(new Font("���ü", Font.BOLD, 15));
		
		jlImg.setFont(new Font("���ü", Font.BOLD, 15));
		jlImg.setForeground(Color.WHITE);
		jtfImg.setFont(new Font("���ü", Font.BOLD, 15));
		
		
		// ������Ʈ ����
		jp.add(jlId);
		jp.add(jtfId);
		jp.add(jbtnCheckid);
		jp.add(jlPw);
		jp.add(jpfPw);
		jp.add(jlPw2);
		jp.add(jpfPw2);
		jp.add(jlName);
		jp.add(jtfName);
		jp.add(jlEmail);
		jp.add(jtfEmail);
		jp.add(jlImg);
		jp.add(jtfImg);
		jp.add(jbtnImg);
		jp.add(jbtnJoin);
		jp.add(jbtnCancel);
		add(jp);
		
		
		// �̺�Ʈ ó��
		jbtnCheckid.addActionListener(this);
		jbtnImg.addActionListener(this);
		jbtnJoin.addActionListener(this);
		jbtnCancel.addActionListener(this);

		
		// Jframe ����
		setLayout(null);
		setTitle("ȸ�� ����");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 800);
		setLocationRelativeTo(null);
		setVisible(true);
			
	}
	
	
	public static void main(String[] args) {
		new Register();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
			
		// ID �ߺ� Ȯ�� ��ư
		if(e.getSource() == jbtnCheckid) {
					
			String id = jtfId.getText().trim();
			
			MemberDAO dao = new MemberDAO();	// DB ����
			boolean idOver = dao.idExist(id);	// ID �ߺ� ����
			dao.close();	// DB ���� ����
			
			
			// ID �� �Է����� �ʾ��� ���
			if(id.length() == 0) {
				JOptionPane.showConfirmDialog(this, 
						"���������� ���� ���̵� �����Դϴ�\n�ٽ� �Է����ּ���", "ID �ߺ��˻�",
						JOptionPane.PLAIN_MESSAGE);
			}
			// ID �� �ߺ��� ���
			else if(idOver) {
				JOptionPane.showConfirmDialog(this, 
						"�Է��Ͻ� ���̵�� ��� ���Դϴ�", "ID �ߺ��˻�",
						JOptionPane.PLAIN_MESSAGE);

				jtfId.setText("");
			}
			// ID �� ��� ������ ���
			else {
				JOptionPane.showConfirmDialog(this, 
						"��� ������ ���̵� �Դϴ�", "ID �ߺ��˻�",
						JOptionPane.PLAIN_MESSAGE);
				
				jtfId.setEditable(false);	// �ߺ� Ȯ���� ���� ID ���� �Ұ�
				checkId = true;
			}

		}
		// ���� ���� ��ư
		else if(e.getSource() == jbtnImg) {
			System.out.println("�̹��� ��ư");
			
			// ������ �̹��� ����
			FileDialog fd = new FileDialog(this, "����", FileDialog.LOAD);
			fd.setDirectory("C:\\");	// ���� ���� �⺻ ���
			fd.setVisible(true);
			
			String filePath = fd.getDirectory() + fd.getFile();	// ���� ���� ��θ�
			
			jtfImg.setText(filePath);

		}
		// ���� ��ư
		else if(e.getSource() == jbtnJoin) {
			System.out.println("���� ��ư");
			System.out.println("ID �ߺ��˻� ���� : " + checkId);
			
		
			// ID �ߺ��˻� ����Ͽ��� ���
			if(checkId) {
	
				String id = jtfId.getText().trim();
				String pw = jpfPw.getText().trim();
				String pw2 = jpfPw2.getText().trim();
				String name = jtfName.getText().trim();
				String email = jtfEmail.getText().trim();
				String img = jtfImg.getText().trim();
				
				
				// ��й�ȣ, ��й�ȣ Ȯ���� ��� ��ġ�� ���
				if(pw.equals(pw2)) {
					
					MemberDAO dao = new MemberDAO();	// DB ����
					MemberVO vo = new MemberVO(0, id, pw, name, email, img, 0, 0);
					
					dao.insert(vo);
					dao.close();
					
					
					JOptionPane.showConfirmDialog(this, 
							"���������� ���ԵǾ����ϴ�", "���� �Ϸ�",
							JOptionPane.PLAIN_MESSAGE);
					
					
					checkId = false;
					this.setVisible(false);
					main.setVisible(true);
										
				}
				// ��й�ȣ, ��й�ȣ Ȯ���� ��ġ���� ���� ���
				else {
					
					JOptionPane.showConfirmDialog(this, 
							"��й�ȣ�� �������� �ʽ��ϴ�", "�н����� Ȯ��",
							JOptionPane.PLAIN_MESSAGE);
					
					jpfPw.setText("");
					jpfPw2.setText("");
				}

			}
			// ID �ߺ��˻� ������� ���� ���
			else {
				JOptionPane.showConfirmDialog(this, 
						"���̵� �ߺ��� Ȯ�ε��� �ʾҽ��ϴ�", "ID �ߺ�Ȯ�� �ʿ�",
						JOptionPane.PLAIN_MESSAGE);
			}
		
		}
		// ��� ��ư
		else if(e.getSource() == jbtnCancel) {
			System.out.println("��� ��ư");
			
			this.setVisible(false);
			main.setVisible(true);
		}
		
	}// actionPerformed() end
	
}// Register class end
