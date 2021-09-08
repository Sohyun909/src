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
		// ������Ʈ ����
		jpBackground = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(background, 0, 0, 1024, 768, null);
			}
		};
		
		jbtnStart = new JButton(new ImageIcon("src/img/btnStart.jpg"));
		jbtnRank = new JButton(new ImageIcon("src/img/btnRank.jpg"));
		
		
		// ������Ʈ ��ġ �� ũ��
		jpBackground.setBounds(0,0,1024,768);
		
		jbtnStart.setBounds(400, 350, 200, 50);
		jbtnRank.setBounds(400, 450, 200, 50);
		
		
		// ������Ʈ ����
		jpBackground.setLayout(null);
		
		jbtnStart.setFont(new Font("���ü", Font.BOLD, 15));
		jbtnStart.setForeground(Color.WHITE);
		
		jbtnRank.setFont(new Font("���ü", Font.BOLD, 15));
		jbtnRank.setForeground(Color.WHITE);
		
		
		// ������Ʈ �߰�
		jpBackground.add(jbtnStart);
		jpBackground.add(jbtnRank);
		add(jpBackground);

		
		// �̺�Ʈ ó��
		jbtnStart.addActionListener(this);
		jbtnRank.addActionListener(this);
		
		
		setTitle("�޴�");
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
		
		// ���ӽ��� ��ư 
		if(e.getSource() == jbtnStart) {
			// ī�� ���� ����
			CardGame cg = new CardGame(id);
			cg.setGame();
			
			this.setVisible(false);
		}
		// ��ŷ ��ư
		else if(e.getSource() == jbtnRank) {
			System.out.println("��ŷ ��Ȳ");
			
			// ��ŷ Ȯ�� â Ȱ��ȭ
			Rank r = new Rank();
		}
		
	}// actionPerformed() end

}// Menu class end