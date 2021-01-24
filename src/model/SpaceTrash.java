package model;


public class SpaceTrash{
	private int type;
	private boolean isGenerated = false;
	private boolean isCrashed = false;	
	private int speed = 9;
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
	
	public void isGenerateRandom(String mode) {
		int percent;
		if(mode.equalsIgnoreCase("Easy")) percent = 7;
		else if(mode.equalsIgnoreCase("Hard")) percent = 10;
		else percent = 15;
		if( ( (int)(Math.random()*1000) ) < percent  ) isGenerated = true;
	}
	
	public void init(String mode) {
		if(!isGenerated) {
			x = INITIAL_X;
			isGenerateRandom(mode);
			setRandomY();
			randomType();
		}
	}
	
	public void moving() {
		x -= speed; 
	}
	
	public void crashed(String mode) {
		setCrached(false);
		isGenerated =false;
		init(mode);
	}
	
	public void passed(String mode) {
		isGenerated = false;
		init(mode);
	}
	
	public void randomType() {
		this.type =  (int)(Math.random()*3)+1;
	}

	
}
