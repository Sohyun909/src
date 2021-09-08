package memoryGame;


import javax.swing.ImageIcon;
import javax.swing.JButton;

import vo.CardVO;

public class CardButton extends JButton{
	private CardVO cardVO;
	private int index;
	
	public CardButton() {
		super();
	}
	public CardButton(String str) {
		super(str);
	}
	
	public CardVO getCardVO() {
		return cardVO;
	}
	public void setCardVO(CardVO cardVO) {
		this.cardVO = cardVO;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	
}
