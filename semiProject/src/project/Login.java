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
	
	// 로그인 배경 이미지
	Image background = new ImageIcon
            (Login.class.getResource("../img/backgroundMain.jpg")).getImage();
	

	Login() {

		// 컴포넌트 생성
		jp = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// g.drawImage( Image객체, X좌표, Y좌표, 너비, 높이, ImageObserver )
				g.drawImage(background, 0, 0, 1024, 768, null);
			}
		};

		jlId = new JLabel("ID");
		jlPw = new JLabel("PW");
		jtfId = new JTextField();
		jpfPw = new JPasswordField();
		jbtnLogin = new JButton(new ImageIcon("src/img/btnLogin.jpg"));
		jbtnRegister = new JButton("회원가입");
		

		// 컴포넌트 위치 및 크기
		jp.setBounds(0, 0, 1024, 768);
		jlId.setBounds(360, 310, 80, 50);
		jlPw.setBounds(360, 370, 80, 50);
		jtfId.setBounds(410, 310, 270, 50);
		jpfPw.setBounds(410, 370, 270, 50);
		jbtnLogin.setBounds(360, 430, 320, 50);
		jbtnRegister.setBounds(450, 500, 100, 30);
		
		
		// 컴포넌트 설정
		jp.setLayout(null);
		
		jlId.setFont(new Font("고딕체", Font.BOLD, 20));
		jlPw.setFont(new Font("고딕체", Font.BOLD, 20));
		jlId.setForeground(Color.white);
		jlPw.setForeground(Color.white);
		
		jtfId.setFont(new Font("고딕체", Font.BOLD, 20));
		jpfPw.setFont(new Font("고딕체", Font.BOLD, 20));
		
		jbtnRegister.setBorderPainted(false);		// 회원가입 버튼 경계선 없앰
		jbtnRegister.setOpaque(false);				// setOpaque : 불투명 설정
		jbtnRegister.setContentAreaFilled(false);	// setContentAreaFilled : 채워넣기 설정
		// setOpaque, setContentAreaFilled 를 모두 비활성화해야 버튼 투명화 가능
		
		jbtnRegister.setFont(new Font("고딕체", Font.BOLD, 15));
		jbtnRegister.setForeground(Color.white);
		
		jbtnLogin.setFont(new Font("고딕체", Font.BOLD, 15));
		jbtnLogin.setForeground(Color.white);
		jbtnLogin.setFocusPainted(false);		// 포커스 표시 설정
		
		// 컴포넌트 부착
		jp.add(jlId);
		jp.add(jlPw);
		jp.add(jtfId);
		jp.add(jpfPw);
		jp.add(jbtnLogin);
		jp.add(jbtnRegister);
		add(jp);
		
		// 이벤트 처리
		jbtnLogin.addActionListener(this);
		jbtnRegister.addActionListener(this);
		
		
		setTitle("로그인");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1024, 768);
		setLocationRelativeTo(null);	// 창 위치 화면 중앙으로
		setLayout(null);
		setResizable(false);	// 창 크기 변경 불가
		setVisible(true);
		
	}// Login() end
	
	
	public static void main(String[] args) {
		new Login();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		// 로그인 버튼 클릭
		if(e.getSource() == jbtnLogin) {
		
			// 입력받은 ID, PW 
			String id = jtfId.getText();
			String pw = jpfPw.getText();

			// DB 연결
			MemberDAO dao = new MemberDAO();
			
			// 로그인 허가 논리값
			boolean loginPass = dao.exist(id, pw);
			
			// DB 연결 종료
			dao.close();
			
			
			// 로그인 성공 (ID, PW 일치)
			if(loginPass) {
				
				// 메뉴창 띄우기
				Menu m = new Menu(id);
				
				// 현재 창 비활성화
				this.setVisible(false);
			}
			// 로그인 실패 (ID, PW 불일치 or 존재하지 않음)
			else {
				JOptionPane.showConfirmDialog(this, 
						"아이디 또는 비밀번호가 잘못 입력 되었습니다.\n", "로그인 실패",
						JOptionPane.PLAIN_MESSAGE);
			}
			
		}
		// 회원가입 버튼 클릭
		else if(e.getSource() == jbtnRegister) {
			
			// 회원가입 창 띄우기
			Register rg = new Register();
			
		}
		
	}// actionPerformed() end
	
}// Login class end

