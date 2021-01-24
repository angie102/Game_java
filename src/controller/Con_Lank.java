package controller;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import model.Model_Game;

public class Con_Lank {

	private String path = "lank_text/lank.txt";	//프로젝트 내의 파일 경로
	private File f = new File(path);
	
	Model_Game user = new Model_Game();	//모델 클래스 객체 생성

	FileInputStream fis;
	FileOutputStream fos;
	
	byte [] data ;
	Integer[] lanked_score;
	
	

	public Con_Lank() {
		readfile();
		inputLankScore();
		
		putfile();
		readfile();
		
		sortRanking();

		

	}
	void sortRanking() { //점수 높은 순으로 정렬
		List<String> keySetList = new ArrayList<>(user.player_info.keySet());
		
		System.out.println("------value 내림차순------");
		Collections.sort(keySetList, (o1, o2) -> (user.player_info.get(o2).compareTo(user.player_info.get(o1))));
		
		
		
		for (String key : keySetList) {
			System.out.println("key : " + key + " / " + "value : " + user.player_info.get(key));
		}
	}
	
	
	
	void inputLankScore() {
		String id = user.getPlayer_ID();
		Integer score = user.getPlayer_Score();
		
		user.player_info.put(id, score);
	}
	
	void putfile() {
		try {
			fos = new FileOutputStream(f,true);

			String update = user.getPlayer_ID()+":"+user.getPlayer_Score()+",";
			
			fos.write(update.getBytes());
		
		
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {	fos.close();	} catch (IOException e) {	e.printStackTrace();	}
		}
		
	}
	void readfile() {
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
					
					user.player_info.put(currentId, currentScore);
				}
				System.out.print(user.player_info);

			} catch (Exception e) {	e.printStackTrace();	} 
			finally {
				try { if(fis!=null) fis.close(); } catch (IOException e) {	e.printStackTrace(); }
			}
			System.out.println("read 파일 종료");
		}
	}
	public static void main(String[] args) {
		new Con_Lank();

	}

}
