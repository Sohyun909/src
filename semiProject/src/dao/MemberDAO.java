package dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vo.MemberVO;

public class MemberDAO {
	
	// 1. ���� ����
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@mydb.cj2lbyl40tfv.ap-northeast-2.rds.amazonaws.com:1521:ORCL";
	String user = "scott";
	String password = "tigertiger1";
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	StringBuffer sb = new StringBuffer();
	
	
	// DB ����
	public MemberDAO() {
		
		// 2. JDBC ����̹� �ε��Ǿ� �ִ��� ���� üũ
		try {
			Class.forName(driver);
			
			// 3. ����
			conn = DriverManager.getConnection(url, user, password);
			
		} catch (ClassNotFoundException e) {
			System.out.println("����̹� �ε� X");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("DB ���� X");
			e.printStackTrace();
		}
		
	}// MemberDAO() end
	
	
	// �α���
	public boolean exist(String id, String pw) {
		
		boolean loginPass = true;
		
		// 4. sql�� �ۼ�
		sb.setLength(0);
		sb.append("SELECT memno, id, pw, name, email, filename, played, win ");
		sb.append("FROM member2 ");
		sb.append("WHERE id = ? AND pw = ? ");
		
		// 5. sql ���� ��ü
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			
			// 6. ��� ����
			rs = pstmt.executeQuery();
			loginPass = rs.next();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return loginPass;
		
	}// isExists() end
	
	
	// ID �ߺ� Ȯ��
	public boolean idExist(String id) {
		
		boolean idOver = true;
		
		// 4. sql�� �ۼ�
		sb.setLength(0);
		sb.append("SELECT id ");
		sb.append("FROM member2 ");
		sb.append("WHERE id = ? ");
		
		// 5. sql ���� ��ü
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			
			// 6. ��� ����
			rs = pstmt.executeQuery();
			idOver = rs.next();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return idOver;
		
	}// idExist() end
	
	
	// ȸ������
	public void insert(MemberVO vo) {
		
		// 4. sql�� �ۼ�
		sb.setLength(0);
		sb.append("INSERT INTO member2 ");
		sb.append("VALUES ( MEMBER2_NO_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ? ) ");

		// 5. sql ���� ��ü
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPw());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getEmail());
			pstmt.setString(5, vo.getFilename());
			pstmt.setInt(6, vo.getPlayed());
			pstmt.setInt(7, vo.getWin());
			
			// 6. ��� ����
			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}// insert() end
	
	
	// ��й�ȣ ����
	public void update(MemberVO vo, String id) {
		
		// 4. sql�� �ۼ�
		sb.setLength(0);
		sb.append("UPDATE member2 ");
		sb.append("SET pw = ? ");
		sb.append("WHERE id = ? ");
		
		// 5. sql ���� ��ü
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, vo.getPw());
			pstmt.setString(2, id);
			
			// 6. ��� ����
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// update() end
	
	
	// ȸ�� Ż��
	public void delete(String id) {
		
		// 4. sql�� �ۼ�
		sb.setLength(0);
		sb.append("DELETE FROM member2 ");
		sb.append("WHERE id = ? ");
		
		// 5. sql ���� ��ü
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			
			// 6. ��� ����
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}// delete() end
	
	
	// ��ŷ Ȯ��
	public ArrayList<MemberVO> selectRank() {
		
		ArrayList<MemberVO> list = new ArrayList<MemberVO>();
		
		// 4. sql�� �ۼ�
		sb.setLength(0);
		sb.append("SELECT id, played, win ");
		sb.append("FROM member2 ");
		sb.append("ORDER BY win DESC ");
		
		// 5. sql ���� ��ü
		try {
			pstmt = conn.prepareStatement(sb.toString());
			
			// 6. ��� ����
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String id = rs.getString("id");
				int played = rs.getInt("played");
				int win = rs.getInt("win");
				
				MemberVO vo = new MemberVO(id, played, win);
				list.add(vo);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
		
	}// select class end
	
	
	// DB ���� ����
	public void close() {
		
		try {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
 		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}// close() end

}// MemberDAO class end
