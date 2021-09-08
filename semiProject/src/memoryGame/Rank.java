package memoryGame;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dao.MemberDAO;
import vo.MemberVO;

public class Rank extends JFrame {
	
	JPanel jpBackground;
	JTextArea jta;
	JScrollPane jsp;
	
	String id;
	int played;
	int win;
	
	Image background1 = new ImageIcon
			(Menu.class.getResource("../img/org_background.jpg")).getImage();
	
	Image background2 = new ImageIcon
			(Menu.class.getResource("../img/board.jpg")).getImage();
	
	
	Rank() {
		
		// 컴포넌트 생성
		jpBackground = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(background2, 0, 0, 400, 600, null);
			}
		};
		
		jta = new JTextArea();
		
		jsp = new JScrollPane(jta,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		MemberDAO dao = new MemberDAO();	// DB 연결
		MemberVO vo = new MemberVO(id, played, win);

		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		list = dao.selectRank();
		
		double winRate = ((double)vo.getWin() / (double)vo.getPlayed())*100;
		
		String data = null;
		
		
		for (int i = 0; i < list.size(); i++) {
			vo = list.get(i);
			data = i+1 + "위\n" + "ID : " + vo.getId() + "\n" +
				       vo.getWin() + " 회 승리\n\n";
			jta.append(data);
		}
	
		
		// 컴포넌트 위치 및 크기
		jsp.setBounds(1, 1, 400, 600);
		jpBackground.setBounds(0, 0, 400, 600);
		
		
		// 컴포넌트 설정
		jta.setOpaque(false);
		jta.setFont(new Font("고딕체", Font.BOLD, 20));
		jta.setForeground(Color.WHITE);
		
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
	
		jpBackground.setLayout(null);

		
		// 컴포넌트 추가
		jpBackground.add(jsp);
		add(jpBackground);
		

		setTitle("랭킹");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);		// 현재 창만 종료
		setLayout(null);
		setSize(400, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
	}// Rank() end
	
	
	public static void main(String[] args) {
		new Rank();
	}

}// Rank class end
