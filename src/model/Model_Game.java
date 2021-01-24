package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
  	기본 프레임 사이즈 1280 * 720
  	
  	
  	파일 입출력으로 점수 저장, 읽기 메서드 생성
  	end 패널에서 호출
 */

public class Model_Game {

	private final int game_SpeedEasy = 15;
	private final int game_SpeedNomal = 8;
	private final int game_SpeedHard = 5;
	private int game_Speed_Up = 4;
	
	//플레이어 ID와 점수
	private String player_ID = "";
	private int player_Score = 0;
	private boolean isPlayerAlive = true;
	private boolean isGameClear = false;
	private boolean isInvincibility = false;
	
	private final int Easygoal = 20; 
	private final int Normalgoal = 40; 
	private final int Hardgoal = 60; 
	private int Cleardistance = Normalgoal; 
	
	//플레이어 HP(산소탱크)
	private final int MAX_HP = 100;
	private int player_HP;

	//1 신발 2 보석 3포션 , -1쓰레기 맞은 횟수
	ArrayList<Integer> list_score = new ArrayList<Integer>(); //점수
	public HashMap<String, Integer> player_info = new HashMap<>();
	

	public int getEasygoal() {
		return Easygoal;
	}
	public int getNormalgoal() {
		return Normalgoal;
	}
	public int getHardgoal() {
		return Hardgoal;
	}
	public int getPlayer_HP() {
		return player_HP;
	}
	public void setPlayer_HP(int player_HP) {
		this.player_HP = player_HP;
	}
	public int getMAX_HP() {
		return MAX_HP;
	}
	public boolean isGameClear() {
		return isGameClear;
	}
	public void setGameClear(boolean isGameClear) {
		this.isGameClear = isGameClear;
	}
	
	public boolean isPlayerAlive() {
		return isPlayerAlive;
	}
	public void setPlayerAlive(boolean isPlayerAlive) {
		this.isPlayerAlive = isPlayerAlive;
	}
	public int getPlayer_Score() {
		return player_Score;
	}
	public void setPlayer_Score(int player_Score) {
		if(player_Score>0) this.player_Score = player_Score;
		else this.player_Score = 0;	
	}

	public String getPlayer_ID() {
		return player_ID;
	}
	public void setPlayer_ID(String player_ID) {
		this.player_ID = player_ID;
	}
	public HashMap<String, Integer> getPlayer_info() {
		return player_info;
	}
	public void setPlayer_info(HashMap<String, Integer> player_info) {
		this.player_info = player_info;
	}
	public final boolean isInvincibility() {
		return isInvincibility;
	}
	public final void setInvincibility(boolean isInvincibility) {
		this.isInvincibility = isInvincibility;
	}
	public final int getCleardistance() {
		return Cleardistance;
	}
	public final void setCleardistance(int clearCondition) {
		this.Cleardistance = clearCondition;
	}
	public int getGame_Speed(String mode) {
		if(mode.equalsIgnoreCase("Easy")) return game_SpeedEasy;
		else if(mode.equalsIgnoreCase("Hard")) return game_SpeedHard;
		else return game_SpeedNomal;
	}
	public int getGame_Speed_Up() {
		return game_Speed_Up;
	}
	public final int getGame_SpeedNomal() {
		return game_SpeedNomal;
	}
	public final int getGame_SpeedEasy() {
		return game_SpeedEasy;
	}
	public final int getGame_SpeedHard() {
		return game_SpeedHard;
	}	
	



		
	
	
	
}
