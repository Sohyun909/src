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
		
		// ������Ʈ ����
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
		
		
		MemberDAO dao = new MemberDAO();	// DB ����
		MemberVO vo = new MemberVO(id, played, win);

		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		list = dao.selectRank();
		
		double winRate = ((double)vo.getWin() / (double)vo.getPlayed())*100;
		
		String data = null;
		
		
		for (int i = 0; i < list.size(); i++) {
			vo = list.get(i);
			data = i+1 + "��\n" + "ID : " + vo.getId() + "\n" +
				       vo.getWin() + " ȸ �¸�\n\n";
			jta.append(data);
		}
	
		
		// ������Ʈ ��ġ �� ũ��
		jsp.setBounds(1, 1, 400, 600);
		jpBackground.setBounds(0, 0, 400, 600);
		
		
		// ������Ʈ ����
		jta.setOpaque(false);
		jta.setFont(new Font("���ü", Font.BOLD, 20));
		jta.setForeground(Color.WHITE);
		
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
	
		jpBackground.setLayout(null);

		
		// ������Ʈ �߰�
		jpBackground.add(jsp);
		add(jpBackground);
		

		setTitle("��ŷ");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);		// ���� â�� ����
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
