package controller;
/*
 
**비즈니스 메소드는

랜덤처리 

점수 증감 // 조건에 맞게 증감 
1) 동전 먹기 +점수
2) 쓰레기 맞기 -점수

속도제어 메소드 
점수 랭킹 표시하는거랑 // arraylist
hashmap <id, 점수 >
hashmap id >> key hashset 

플레이타임 표시 메소드 ( 버퍼) [ time 00:00]


flie입출력


 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.Main;
import model.Item;
import model.Model_Game;
import model.SpaceTrash;


public class Con_GameSystem extends Model_Game {
	ArrayList<SpaceTrash> list_trash = new ArrayList<SpaceTrash>(); //충돌 횟수(점수에서 사용)
	ArrayList<Item> list_item = new ArrayList<Item>(); //아이템리스트
	public ArrayList<Integer> list_score = new ArrayList<Integer>(); //점수
	
	Item item = new Item();
	SpaceTrash trash = new SpaceTrash();
	private int scoreUp = 1; //점수 증가량
	private int saferDown = -1; // 산소 떨어지는 양
	
	
	private String path = "lank_text/lank.txt";	//프로젝트 내의 파일 경로
	private File f = new File(path);

	FileInputStream fis;
	FileOutputStream fos;
	
	byte [] data ;
	Integer[] lanked_score;
	public List<String> keySetList;
	
	
	/**
	 *  HP
	 */

	public void saferInit() {
		setPlayer_HP(getMAX_HP()); //최조 HP설정 풀피
	}
	public boolean saferControl(int speedBG, int timeCnt, String mode) {
		if(timeCnt>150) {//같은 sleep을 공유하므로 그냥 같이 사용
			HpReduction(mode); // 감소 메서드 호출
			ScoreIncrease();// 증감 메서드 호출
			return true;
		}else return false;
		
	}	
	public void HpReduction(String mode) {
			if (getPlayer_HP() <= 0) { //게임오버조건
				setPlayer_HP(0);
				setPlayerAlive(false); 
				return;
			}
			if(!isInvincibility()) {				
				if(mode.equalsIgnoreCase("Easy")) saferDown = -1;
				else if(mode.equalsIgnoreCase("Hard")) saferDown = -5;
				else saferDown = -3;
				setPlayer_HP(getPlayer_HP()+saferDown);
			}


	}
	
	/**
	 * 	SCORE
	 */
	
	public void ScoreIncrease() {
		setPlayer_Score(getPlayer_Score()+scoreUp); 
		
	}
	public void resultScore(ArrayList<Integer> score) {
		int cntJewelry =0;
		//보석이 10개 이상일때마다 보너스
		//아이템 리스트에 따른 처리 1신발(빨라지기) 2보석(점수 증가) 3포션(hp 회복) 
		
		for (Integer i : score) {
			if(i == 2) cntJewelry++;
		}
		setPlayer_Score(getPlayer_Score() + (cntJewelry/10)*10 ); //보너스점수 더해서 저장	

		if(getPlayer_Score()<0) setPlayer_Score(0);
	}
	
	
	/**
	 * 게임 클리어조건
	 */
	public void gameClear(int distance) {
		//System.out.println("clearCondition: "+clearCondition);
		if(distance > getCleardistance()) {
			setGameClear(true);
			resultScore(list_score); //클리어시 점수저장
		}
	}
	public void gameOver() {
		if(getPlayer_HP()<=0) setPlayerAlive(false);			
	}
	
	/**
	 * 랭킹
	 */
	public void sortRanking() { //점수 높은 순으로 정렬
		 keySetList = new ArrayList<>(player_info.keySet());
		Collections.sort(keySetList, (o1, o2) -> (player_info.get(o2).compareTo(player_info.get(o1))));
	}
	
	public void inputLankScore() {
		String id = getPlayer_ID();
		Integer score = getPlayer_Score();
		
		player_info.put(id, score);
	}
	
	public void putfile() {
		try {
			fos = new FileOutputStream(f,true);

			String update = getPlayer_ID()+":"+getPlayer_Score()+",";
			fos.write(update.getBytes());
		
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {	fos.close();	} catch (IOException e) {	e.printStackTrace();	}
		}
		
	}
	public void readfile() {
		//System.out.println(f.exists());
		
		if(f.exists()) {
			try {
				
				fis = new FileInputStream(f);
				
				data = new byte[fis.available()];
				fis.read(data);
				
				String str = new String(data);
				//System.out.println(str);
			
				String []lank = str.split(",");
				for (int i = 0; i < lank.length; i++) {
				//	System.out.println(lank[i]);
					String [] userAndScore = lank[i].split(":");
					
					String currentId = userAndScore[0];
					Integer currentScore = Integer.valueOf(userAndScore[1]);
					
					player_info.put(currentId, currentScore);
				}
				//System.out.print(player_info);

			} catch (Exception e) {	e.printStackTrace();	} 
			finally {
				try { if(fis!=null) fis.close(); } catch (IOException e) {	e.printStackTrace(); }
			}
			System.out.println("read 파일 종료");
		}
	}
	
	
	

	
}
