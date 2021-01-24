package controller;
/*
 
**����Ͻ� �޼ҵ��

����ó�� 

���� ���� // ���ǿ� �°� ���� 
1) ���� �Ա� +����
2) ������ �±� -����

�ӵ����� �޼ҵ� 
���� ��ŷ ǥ���ϴ°Ŷ� // arraylist
hashmap <id, ���� >
hashmap id >> key hashset 

�÷���Ÿ�� ǥ�� �޼ҵ� ( ����) [ time 00:00]


flie�����


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
	ArrayList<SpaceTrash> list_trash = new ArrayList<SpaceTrash>(); //�浹 Ƚ��(�������� ���)
	ArrayList<Item> list_item = new ArrayList<Item>(); //�����۸���Ʈ
	public ArrayList<Integer> list_score = new ArrayList<Integer>(); //����
	
	Item item = new Item();
	SpaceTrash trash = new SpaceTrash();
	private int scoreUp = 1; //���� ������
	private int saferDown = -1; // ��� �������� ��
	
	
	private String path = "lank_text/lank.txt";	//������Ʈ ���� ���� ���
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
		setPlayer_HP(getMAX_HP()); //���� HP���� Ǯ��
	}
	public boolean saferControl(int speedBG, int timeCnt, String mode) {
		if(timeCnt>150) {//���� sleep�� �����ϹǷ� �׳� ���� ���
			HpReduction(mode); // ���� �޼��� ȣ��
			ScoreIncrease();// ���� �޼��� ȣ��
			return true;
		}else return false;
		
	}	
	public void HpReduction(String mode) {
			if (getPlayer_HP() <= 0) { //���ӿ�������
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
		//������ 10�� �̻��϶����� ���ʽ�
		//������ ����Ʈ�� ���� ó�� 1�Ź�(��������) 2����(���� ����) 3����(hp ȸ��) 
		
		for (Integer i : score) {
			if(i == 2) cntJewelry++;
		}
		setPlayer_Score(getPlayer_Score() + (cntJewelry/10)*10 ); //���ʽ����� ���ؼ� ����	

		if(getPlayer_Score()<0) setPlayer_Score(0);
	}
	
	
	/**
	 * ���� Ŭ��������
	 */
	public void gameClear(int distance) {
		//System.out.println("clearCondition: "+clearCondition);
		if(distance > getCleardistance()) {
			setGameClear(true);
			resultScore(list_score); //Ŭ����� ��������
		}
	}
	public void gameOver() {
		if(getPlayer_HP()<=0) setPlayerAlive(false);			
	}
	
	/**
	 * ��ŷ
	 */
	public void sortRanking() { //���� ���� ������ ����
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
			System.out.println("read ���� ����");
		}
	}
	
	
	

	
}
