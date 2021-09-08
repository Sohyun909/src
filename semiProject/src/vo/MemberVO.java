package vo;


public class MemberVO {
	
	private int memno;
	private String id;
	private String pw;
	private String name;
	private String email;
	private String filename;
	private int played;
	private int win;
	
	
	MemberVO() {
		
	}
	
	
	public MemberVO(int memno, String id, String pw, String name, 
					String email, String filename, int played, int win) {
		
		super();
		this.memno = memno;
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.email = email;
		this.filename = filename;
		this.played = played;
		this.win = win;
		
	}
	

	public MemberVO(String id, int played, int win) {

		super();
		this.id = id;
		this.played = played;
		this.win = win;

	}
	
	
	public MemberVO(String id) {

		super();
		this.id = id;

	}
	

	public int getMemno() {
		return memno;
	}


	public void setMemno(int memno) {
		this.memno = memno;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPw() {
		return pw;
	}


	public void setPw(String pw) {
		this.pw = pw;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}


	public int getPlayed() {
		return played;
	}


	public void setPlayed(int played) {
		this.played = played;
	}


	public int getWin() {
		return win;
	}


	public void setWin(int win) {
		this.win = win;
	}
	
}// MemberVO class end
