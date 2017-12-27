package appwarp.s2.cards;

import android.content.Context;
import android.widget.ImageView;

public class CardView extends ImageView {

	private int id = -1;
	private int initX;
	private int initY;
	
	public CardView(Context context, int id, int x, int y) {
		super(context);
		this.id = id;
		this.initX = x;
		this.initY = y;
		super.setX(x);
		super.setY(y);
	}
	
	public CardView(Context context, int id){
		super(context);
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public int getInitX(){
		return initX;
	}
	
	public int getInitY(){
		return initY;
	}

}
