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
	
	// 회원가입 배경 이미지 
	Image background = new ImageIcon
			(Register.class.getResource("../img/backgroundRegister.jpg")).getImage();

	boolean checkId = false;	// ID 중복검사 통과 논리값
	
	
	Register() {

		super("Login");
		this.main = main;
		
		// 컴포넌트 생성
		jp = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(background, 0, 0, 500, 800, null);
			}
		};
		
		jlId = new JLabel("아이디");
		jtfId = new JTextField();
		jbtnCheckid = new JButton(new ImageIcon("src/img/btnid.jpg"));
		
		jlPw = new JLabel("비밀번호");
		jpfPw = new JPasswordField();
		jlPw2 = new JLabel("비밀번호 확인");
		jpfPw2 = new JPasswordField();
		
		jlName = new JLabel("이름");
		jtfName = new JTextField();
		
		jlEmail = new JLabel("E-mail");
		jtfEmail = new JTextField();
		
		jlImg = new JLabel("프로필 사진");
		jtfImg = new JTextField();
		jbtnImg = new JButton(new ImageIcon("src/img/btnImg.jpg"));
		
		jbtnJoin = new JButton(new ImageIcon("src/img/btnJoin.jpg"));
		jbtnCancel = new JButton(new ImageIcon("src/img/btnCancel.jpg"));
		
		
		// 컴포넌트 위치 및 크기
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
		
		
		// 컴포넌트 설정
		jlId.setFont(new Font("고딕체", Font.BOLD, 15));
		jlId.setForeground(Color.WHITE);
		jtfId.setFont(new Font("고딕체", Font.BOLD, 15));
		
		jlPw.setFont(new Font("고딕체", Font.BOLD, 15));
		jlPw.setForeground(Color.WHITE);
		jpfPw.setFont(new Font("고딕체", Font.BOLD, 15));
		
		jlPw2.setFont(new Font("고딕체", Font.BOLD, 15));
		jlPw2.setForeground(Color.WHITE);
		jpfPw2.setFont(new Font("고딕체", Font.BOLD, 15));
		
		jlName.setFont(new Font("고딕체", Font.BOLD, 15));
		jlName.setForeground(Color.WHITE);
		jtfName.setFont(new Font("고딕체", Font.BOLD, 15));
		
		jlEmail.setFont(new Font("고딕체", Font.BOLD, 15));
		jlEmail.setForeground(Color.WHITE);
		jtfEmail.setFont(new Font("고딕체", Font.BOLD, 15));
		
		jlImg.setFont(new Font("고딕체", Font.BOLD, 15));
		jlImg.setForeground(Color.WHITE);
		jtfImg.setFont(new Font("고딕체", Font.BOLD, 15));
		
		
		// 컴포넌트 부착
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
		
		
		// 이벤트 처리
		jbtnCheckid.addActionListener(this);
		jbtnImg.addActionListener(this);
		jbtnJoin.addActionListener(this);
		jbtnCancel.addActionListener(this);

		
		// Jframe 설정
		setLayout(null);
		setTitle("회원 가입");
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
			
		// ID 중복 확인 버튼
		if(e.getSource() == jbtnCheckid) {
					
			String id = jtfId.getText().trim();
			
			MemberDAO dao = new MemberDAO();	// DB 연결
			boolean idOver = dao.idExist(id);	// ID 중복 논리값
			dao.close();	// DB 연결 종료
			
			
			// ID 를 입력하지 않았을 경우
			if(id.length() == 0) {
				JOptionPane.showConfirmDialog(this, 
						"정상적이지 않은 아이디 형식입니다\n다시 입력해주세요", "ID 중복검사",
						JOptionPane.PLAIN_MESSAGE);
			}
			// ID 가 중복일 경우
			else if(idOver) {
				JOptionPane.showConfirmDialog(this, 
						"입력하신 아이디는 사용 중입니다", "ID 중복검사",
						JOptionPane.PLAIN_MESSAGE);

				jtfId.setText("");
			}
			// ID 가 사용 가능할 경우
			else {
				JOptionPane.showConfirmDialog(this, 
						"사용 가능한 아이디 입니다", "ID 중복검사",
						JOptionPane.PLAIN_MESSAGE);
				
				jtfId.setEditable(false);	// 중복 확인이 끝난 ID 수정 불가
				checkId = true;
			}

		}
		// 사진 변경 버튼
		else if(e.getSource() == jbtnImg) {
			System.out.println("이미지 버튼");
			
			// 저장할 이미지 선택
			FileDialog fd = new FileDialog(this, "열기", FileDialog.LOAD);
			fd.setDirectory("C:\\");	// 파일 선택 기본 경로
			fd.setVisible(true);
			
			String filePath = fd.getDirectory() + fd.getFile();	// 선택 파일 경로명
			
			jtfImg.setText(filePath);

		}
		// 가입 버튼
		else if(e.getSource() == jbtnJoin) {
			System.out.println("가입 버튼");
			System.out.println("ID 중복검사 상태 : " + checkId);
			
		
			// ID 중복검사 통과하였을 경우
			if(checkId) {
	
				String id = jtfId.getText().trim();
				String pw = jpfPw.getText().trim();
				String pw2 = jpfPw2.getText().trim();
				String name = jtfName.getText().trim();
				String email = jtfEmail.getText().trim();
				String img = jtfImg.getText().trim();
				
				
				// 비밀번호, 비밀번호 확인이 모두 일치할 경우
				if(pw.equals(pw2)) {
					
					MemberDAO dao = new MemberDAO();	// DB 연결
					MemberVO vo = new MemberVO(0, id, pw, name, email, img, 0, 0);
					
					dao.insert(vo);
					dao.close();
					
					
					JOptionPane.showConfirmDialog(this, 
							"정상적으로 가입되었습니다", "가입 완료",
							JOptionPane.PLAIN_MESSAGE);
					
					
					checkId = false;
					this.setVisible(false);
					main.setVisible(true);
										
				}
				// 비밀번호, 비밀번호 확인이 일치하지 않을 경우
				else {
					
					JOptionPane.showConfirmDialog(this, 
							"비밀번호가 동일하지 않습니다", "패스워드 확인",
							JOptionPane.PLAIN_MESSAGE);
					
					jpfPw.setText("");
					jpfPw2.setText("");
				}

			}
			// ID 중복검사 통과하지 못한 경우
			else {
				JOptionPane.showConfirmDialog(this, 
						"아이디 중복이 확인되지 않았습니다", "ID 중복확인 필요",
						JOptionPane.PLAIN_MESSAGE);
			}
		
		}
		// 취소 버튼
		else if(e.getSource() == jbtnCancel) {
			System.out.println("취소 버튼");
			
			this.setVisible(false);
			main.setVisible(true);
		}
		
	}// actionPerformed() end
	
}// Register class end
