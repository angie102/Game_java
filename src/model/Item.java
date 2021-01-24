package model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Item{
	private int type;
	private boolean isGenerated = false;
	private boolean isCrashed = false;	
	private int speed = 5;
	private final int INITIAL_X = 1280;
	private int x;
	private int y;
	
	public int getINITIAL_X() {
		return INITIAL_X;
	}
	public boolean isCrached() {
		return isCrashed;
	}
	public void setCrached(boolean isCrached) {
		this.isCrashed = isCrached;
	}
	public boolean isGenerated() {
		return isGenerated;
	}
	public void setGenerated(boolean isGenerated) {
		this.isGenerated = isGenerated;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public void setX(int x) {
		 this.x = x;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		 this.y = y;
	}
	
	public void setRandomY() {
		this.y = (int)(Math.random()*470)+80;	
	}
	
	public final int getType() {
		return type;
	}
	public final void setType(int type) {
		this.type = type;
	}
	
	public void isGenerateRandom() {
		if(Math.random()*1000<20) isGenerated = true;
	}
	
	public void init(ArrayList<Item> list_item) {
		if(!isGenerated) {
			x = INITIAL_X;
			isGenerateRandom();
			setRandomY();
			randomType(list_item);
		}
	}
	
	public void moving() {
		x -= speed; 
	}
	
	public void crashed(ArrayList<Item> list_item) {
		setCrached(false);
		isGenerated = false;
		init(list_item);
	}
	
	public void passed(ArrayList<Item> list_item) {
		isGenerated = false;
		init(list_item);
	}
	
	public void randomType(ArrayList<Item> list_item) {
		// 신발 5% 포션 15% 나머보석
		if(isGenerated) {
			int percent =ThreadLocalRandom.current().nextInt(0, 100);
			if (percent <= 5) type = 1;
			else if (percent <= 15) type = 3;
			else type = 2;
			
		}
	}

	
}
