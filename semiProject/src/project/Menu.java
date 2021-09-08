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
import javax.swing.JPanel;


public class Menu extends JFrame implements ActionListener {
	
	JPanel jpBackground;
	JButton jbtnStart, jbtnRank;
	
	Image background = new ImageIcon
			(Menu.class.getResource("../img/backgroundMain.jpg")).getImage();
	String id = "null_id";
	

	public Menu(String id) {
		this.id = id;
		// 컴포넌트 생성
		jpBackground = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(background, 0, 0, 1024, 768, null);
			}
		};
		
		jbtnStart = new JButton(new ImageIcon("src/img/btnStart.jpg"));
		jbtnRank = new JButton(new ImageIcon("src/img/btnRank.jpg"));
		
		
		// 컴포넌트 위치 및 크기
		jpBackground.setBounds(0,0,1024,768);
		
		jbtnStart.setBounds(400, 350, 200, 50);
		jbtnRank.setBounds(400, 450, 200, 50);
		
		
		// 컴포넌트 설정
		jpBackground.setLayout(null);
		
		jbtnStart.setFont(new Font("고딕체", Font.BOLD, 15));
		jbtnStart.setForeground(Color.WHITE);
		
		jbtnRank.setFont(new Font("고딕체", Font.BOLD, 15));
		jbtnRank.setForeground(Color.WHITE);
		
		
		// 컴포넌트 추가
		jpBackground.add(jbtnStart);
		jpBackground.add(jbtnRank);
		add(jpBackground);

		
		// 이벤트 처리
		jbtnStart.addActionListener(this);
		jbtnRank.addActionListener(this);
		
		
		setTitle("메뉴");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setSize(1024, 768);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
	}// Menu() end
	
	
	public static void main(String[] args) {
		Menu m = new Menu("null");
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		// 게임시작 버튼 
		if(e.getSource() == jbtnStart) {
			// 카드 게임 실행
			CardGame cg = new CardGame(id);
			cg.setGame();
			
			this.setVisible(false);
		}
		// 랭킹 버튼
		else if(e.getSource() == jbtnRank) {
			System.out.println("랭킹 현황");
			
			// 랭킹 확인 창 활성화
			Rank r = new Rank();
		}
		
	}// actionPerformed() end

}// Menu class end