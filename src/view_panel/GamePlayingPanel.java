package view_panel;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import controller.Con_BGM;
import controller.Con_GameSystem;
import main.Main;
import model.Item;
import model.SpaceTrash;

public class GamePlayingPanel extends JPanel implements Runnable {
	//객체
	Con_BGM bgm = new Con_BGM();	//음악 객체
	Con_GameSystem game = new Con_GameSystem();
	SpaceTrash trash = new SpaceTrash(); //쓰레기객체
	Item item = new Item(); //아이템객체

	private Image buffImage;
	private Graphics buffg;

	private Image image_BG;
	private Image image_character;
	private Image image_trash1;
	private Image image_trash2;
	private Image image_trash3;
	private Image image_item1;
	private Image image_item2;
	private Image image_item3;
	private Image image_explosion;
	private Image image_ssi;

	private ImageIcon image_pause;
	private ImageIcon image_gameOver;
	private ImageIcon[] image_counting = new ImageIcon[2];	

	private ImageIcon image_btn_enter;
	private ImageIcon image_btn_enter_pressed ;
	private ImageIcon image_btn_easy;
	private ImageIcon image_btn_normal;
	private ImageIcon image_btn_hard;

	private AlphaComposite alphaComposite;
	Color color_transparent = new Color(0,0,0,0);
	Color color_transparent100 = new Color(255,255,255,100);

	Font font_hp = new Font("Verdana", Font.BOLD, 35); //임시설정
	Font font_score = new Font("Verdana", Font.BOLD, 140); //임시설정


	private Thread animator;

	/**
	 * 위치값
	 */
	private int BG2_x = 0;
	private int BG1_x = 0;

	private int character_y = Main.FRAME_HEIGHT/2-130;
	private int character_x = 50;
	private int Character_speed = 20;



	ArrayList<SpaceTrash> list_trash = new ArrayList<SpaceTrash>(); //쓰레기 리스트
	ArrayList<Item> list_item = new ArrayList<Item>(); //아이템리스트

	private boolean escKeyOn = false; //일시정지용
	private String mode= "Normal";
	private int itemTime = 0;
	private int scoreUp =10;
	private int checkdistance =0;
	private int speedBG = game.getGame_Speed(mode) ; // 배경 이동 속도

	/**
	 * GUI
	 */

	private JLabel label_hp;
	private JLabel label_score;
	private JLabel label_mode;

	private JLabel label_resultScore;
	private JLabel label_gameOver;
	private JLabel label_pause;	
	private JLabel label_pauseInfo;	

	private JLabel[] label_startCount = new JLabel[2];

	private JPanel set_Panel;
	private JButton btn_Easy;
	private JButton btn_Normal;
	private JButton btn_Hard;

	private JProgressBar hpBar;
	private JButton btn_conform;
	private JButton btn_setting;

	private JLabel id_Label;
	private JTextField id_Text;

	//private JPanel panel_pause;


	public GamePlayingPanel() {
		setFocusable(true);
		requestFocusInWindow();
		setLayout(null);

		//배경 생성
		initBG();
		pauseScreen();
		//counting
		for (int i = 0; i < image_counting.length; i++) {
			image_counting[i]= new ImageIcon("source/counting/count_"+i+".png");
			label_startCount[i] = new JLabel(image_counting[i]);
			label_startCount[i].setFont(font_score);

			label_startCount[i].setSize(image_counting[i].getIconWidth(), image_counting[i].getIconHeight());
			label_startCount[i].setLocation(Main.FRAME_WIDTH/2-image_counting[i].getIconWidth()/2, Main.FRAME_HEIGHT/2-image_counting[i].getIconHeight()/2);
			add(label_startCount[i]);
		}
		label_startCount[1].setVisible(false);

		//HP
		label_hp = new JLabel("SAFER"/* : " + game.getMAX_HP()*/);
		label_hp.setFont(font_hp);
		label_hp.setSize(500,35);
		label_hp.setLocation(310, 42);
		label_hp.setForeground(Color.WHITE);
		label_hp.setBackground(color_transparent);
		add(label_hp);

		//SCORE
		label_score = new JLabel("SCORE "+ game.getPlayer_Score());
		label_score.setFont(font_hp);
		label_score.setSize(250, 35);
		label_score.setLocation(50, 42);
		label_score.setForeground(Color.WHITE);
		label_score.setBackground(color_transparent);
		add(label_score);

		//HP bar
		hpBar = new JProgressBar(0,100);
		hpBar.setLocation(450, 45);
		hpBar.setSize(500, 30);
		//hpBar.setForeground(Color.WHITE);
		hpBar.setBackground(color_transparent);
		add(hpBar);

		//mode
		label_mode = new JLabel(mode);
		label_mode.setFont(font_hp);
		label_mode.setSize(500,35);
		label_mode.setLocation(820, 42);
		label_mode.setForeground(Color.WHITE);
		label_mode.setBackground(color_transparent);
		label_mode.setHorizontalAlignment(JLabel.CENTER);
		add(label_mode);

		resultScreen();
		settingScreen();


		//키보드 이벤트 처리
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_UP) {
					characterMoving(-Character_speed);
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					characterMoving(Character_speed);
				}
				if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
					escKeyOn = !escKeyOn;
				}
			}
		});

	}//Constructer end

	private void settingScreen() {
		//btn_setting
		btn_setting = new JButton();
		btn_setting.setSize(40, 40);
		btn_setting.setLocation(1180, 36);
		btn_setting.setFont(font_hp);
		btn_setting.setBackground(Color.WHITE);
		btn_setting.setForeground(Color.BLACK);;
		add(btn_setting);	

		// setting panel
		set_Panel = new JPanel();

		image_btn_easy = new ImageIcon("source/Buttons/setting_easy_button.png"); 
		//image_btn_easy_pressed = new ImageIcon("source/Buttons/setting_easy_button_pressed.png"); 
		image_btn_hard = new ImageIcon("source/Buttons/setting_hard_button.png"); 
		//image_btn_hard_pressed = new ImageIcon("source/Buttons/setting_hard_button_pressed.png"); 
		image_btn_normal = new ImageIcon("source/Buttons/setting_normal_button.png"); 
		//image_btn_normal_pressed = new ImageIcon("source/Buttons/setting_normal_button_pressed.png"); 

		btn_Easy = new JButton(image_btn_easy);
		btn_Normal = new JButton(image_btn_normal);
		btn_Hard = new JButton(image_btn_hard);

		btn_Easy.setSize(image_btn_easy.getIconWidth(), image_btn_easy.getIconHeight());
		btn_Easy.setLocation(Main.FRAME_WIDTH/2 -image_btn_easy.getIconWidth()/2 , 200);
		btn_Easy.setContentAreaFilled(false);
		btn_Easy.setBorderPainted(false);

		btn_Normal.setSize(image_btn_normal.getIconWidth(), image_btn_normal.getIconHeight());
		btn_Normal.setLocation(Main.FRAME_WIDTH/2 -image_btn_normal.getIconWidth()/2 , 300);
		btn_Normal.setContentAreaFilled(false);
		btn_Normal.setBorderPainted(false);

		btn_Hard.setSize(image_btn_hard.getIconWidth(), image_btn_hard.getIconHeight());
		btn_Hard.setLocation(Main.FRAME_WIDTH/2 -image_btn_hard.getIconWidth()/2 , 400);
		btn_Hard.setContentAreaFilled(false);
		btn_Hard.setBorderPainted(false);

		set_Panel.setSize(200, 250);
		set_Panel.setLayout(new GridLayout(3, 1));
		set_Panel.setBackground(color_transparent100);
		set_Panel.setLocation(Main.FRAME_WIDTH/2-set_Panel.getWidth()/2, Main.FRAME_HEIGHT/2-set_Panel.getHeight()/2);

		set_Panel.add(btn_Easy);
		set_Panel.add(btn_Normal);
		set_Panel.add(btn_Hard);	
		add(set_Panel);		
		set_Panel.setVisible(false);

		btn_setting.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				set_Panel.setVisible(!set_Panel.isVisible());
				setting();					
			}
		});
	}
	private void setting() { //설정패널
		btn_Easy.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				speedBG = game.getGame_SpeedEasy();
				game.setCleardistance(game.getEasygoal());
				mode ="Easy";
				label_mode.setText(mode);
				set_Panel.setVisible(false);
			}
		});

		btn_Normal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				speedBG = game.getGame_SpeedNomal();
				game.setCleardistance(game.getNormalgoal());
				mode ="Normal";
				label_mode.setText(mode);
				set_Panel.setVisible(false);
			}
		});


		btn_Hard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				speedBG = game.getGame_SpeedHard();
				game.setCleardistance(game.getHardgoal());
				mode ="Hard";
				label_mode.setText(mode);
				set_Panel.setVisible(false);			
			}
		});

	}

	public void resultScreen() {
		//game end button

		image_btn_enter = new ImageIcon("source/Buttons/enter_button.png");
		image_btn_enter_pressed = new ImageIcon("source/Buttons/enter_button_pressed.png"); 
		btn_conform = new JButton(image_btn_enter);
		btn_conform.setSize(image_btn_enter.getIconWidth(), image_btn_enter.getIconHeight());
		btn_conform.setLocation(Main.FRAME_WIDTH/2 -image_btn_enter.getIconWidth()/2 , 450);
		btn_conform.setVisible(false);
		add(btn_conform);
		btn_conform.setContentAreaFilled(false);
		btn_conform.setBorderPainted(false);

		//최종 점수 출력
		label_resultScore = new JLabel();
		label_resultScore.setFont(font_score);
		label_resultScore.setSize(700, 150);
		label_resultScore.setLocation(Main.FRAME_WIDTH/2-label_resultScore.getWidth()/2, 180);
		label_resultScore.setForeground(Color.WHITE);
		label_resultScore.setHorizontalAlignment(JLabel.CENTER);
		label_resultScore.setVisible(false);
		add(label_resultScore);

		//게임오버
		label_gameOver = new JLabel(image_gameOver);
		label_gameOver.setFont(font_score);
		label_gameOver.setSize(image_gameOver.getIconWidth(), image_gameOver.getIconHeight());
		label_gameOver.setLocation(0, -100);
		label_gameOver.setVisible(false);
		add(label_gameOver);

		int heigth =230;
		id_Label = new JLabel("ID  ");
		id_Label.setForeground(Color.WHITE);
		id_Label.setFont(font_hp);
		id_Label.setSize(100, 90);
		id_Label.setLocation(Main.FRAME_WIDTH/2-165, 120+heigth);
		id_Text = new JTextField("",8);
		id_Text.setSize(250, 30);
		id_Text.setLocation(Main.FRAME_WIDTH/2-90, 150+heigth);
		id_Text.setVisible(false);
		id_Label.setVisible(false);
		add(id_Text);
		add(id_Label);
		id_Text.addKeyListener(new KeyAdapter() { //text필드에서 enter 처리
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()== KeyEvent.VK_ENTER) saveScoreAndTransition();
			}
		});
		addKeyListener(new KeyAdapter() {// text필드 선택하지않고 enter값 입력 처리
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()== KeyEvent.VK_ENTER) saveScoreAndTransition();
			}
		});
		btn_conform.addActionListener( new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!game.isPlayerAlive()) new PanelTransition().changeIntro("돌아가기"); 
				else saveScoreAndTransition();
			}
		});

		btn_conform.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn_conform.setIcon(image_btn_enter_pressed);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btn_conform.setIcon(image_btn_enter);
			}
		});

	}

	private void saveScoreAndTransition() {
		game.setPlayer_ID(id_Text.getText());
		if(!game.getPlayer_ID().equals("")) game.player_info.put(game.getPlayer_ID(), game.getPlayer_Score());
		scoreRank();
		new PanelTransition().changeEnd("게임종료", game);	
		try {
			bgm.BGM_Off();			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void scoreRank() {
		if(!id_Text.getText().equals("")) {
			game.inputLankScore();
			game.putfile();
		}
	}

	private void opacityContorl() {
		if(escKeyOn || !game.isPlayerAlive()) {
			label_score.setForeground(color_transparent100);
			label_hp.setForeground(color_transparent100);
			hpBar.setForeground(color_transparent100);
			btn_setting.setBackground(color_transparent100);
			label_mode.setForeground(color_transparent100);
		}else {
			label_score.setForeground(Color.WHITE);						
			label_hp.setForeground(Color.WHITE);						
			hpBar.setForeground(Color.WHITE);		
			btn_setting.setBackground(Color.WHITE);
			label_mode.setForeground(Color.WHITE);
		}
	}

	private void initBG() {
		setBackground(Color.BLACK);
		setSize(Main.FRAME_WIDTH, Main.FRAME_HEIGHT);
		loadImage();
	}

	/**
	 * 이미지 로딩
	 */
	private void loadImage() {
		image_BG = new ImageIcon("source\\Back\\BG.gif").getImage();

		image_character = new ImageIcon("source\\character\\character.gif").getImage();

		image_explosion = new ImageIcon("source\\explosion_1.png").getImage();
		image_ssi = new ImageIcon("source\\SSI.png").getImage();

		image_trash1 = new ImageIcon("source\\throwing\\trash1.png").getImage();
		image_trash2 = new ImageIcon("source\\throwing\\trash2.png").getImage();
		image_trash3 = new ImageIcon("source\\throwing\\trash3.png").getImage();

		image_item1 = new ImageIcon("source\\throwing\\item1.png").getImage();
		image_item2 = new ImageIcon("source\\throwing\\item2.png").getImage();
		image_item3 = new ImageIcon("source\\throwing\\item3.png").getImage();

		image_gameOver = new ImageIcon("source\\game_over.png");
		image_pause = new ImageIcon("source\\pase_esc.png");

		BG2_x = Main.FRAME_WIDTH;
	}



	//배경그리기 *** 1픽셀 틀어지는거 방법 생각해보기
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//배경 이미지 두개를 번갈아가면서 호출

		if(buffg == null) {
			buffImage = createImage(this.getWidth(), this.getHeight());
			if (buffImage == null) {
				System.out.println("더블 버퍼링용 오프 스크린 생성 실패");
			}else{
				buffg = buffImage.getGraphics();
			}
		}
		Graphics2D g2 = (Graphics2D)buffg;

		buffg.drawImage(image_BG, BG1_x, 0, null); 
		buffg.drawImage(image_BG, BG2_x, 0, null); 
		//g.drawImage(image, BG2_x+150, 0, this);//1과 2 이미지 사이에 틈이 생기는데 처리방법 고민

		if(checkdistance>game.getCleardistance()-1) { // 우주정거장
			buffg.drawImage(image_ssi, BG2_x+1700, 0, null); 
			//System.out.println("클리어 조건 확인");
		}

		buffg.drawImage(image_character, character_x, character_y, null); // 캐릭터 그리기

		try {//예외처리
			if(list_trash != null) {		
				for (SpaceTrash tempTrash : list_trash) {
					if(tempTrash.isGenerated()) {
						if(tempTrash.isCrached()) {
							//System.out.println("충돌이미지 불러오기");
							buffg.drawImage(image_explosion, tempTrash.getX(), tempTrash.getY(), null);
							tempTrash.setX(tempTrash.getINITIAL_X()+100);

						}else {

							if(tempTrash.getType()==1) {
								buffg.drawImage(image_trash1, tempTrash.getX(), tempTrash.getY(), null);
							}else if (tempTrash.getType()==2) {
								buffg.drawImage(image_trash2, tempTrash.getX(), tempTrash.getY(), null);
							}else if (tempTrash.getType()==3) {
								buffg.drawImage(image_trash3, tempTrash.getX(), tempTrash.getY(), null);
							}
						}
					}
				}
			}


			if(list_item != null) {			
				for (Item tempItem : list_item) {
					if(tempItem.isGenerated()) {
						if(tempItem.isCrached()) {
							//System.out.println("충돌이미지 불러오기");
							//buffg.drawImage(image_explosion, tempItem.getX(), tempItem.getY(), null);
							tempItem.setX(tempItem.getINITIAL_X()+100);

						}else {
							if(tempItem.getType()==1) {
								buffg.drawImage(image_item1, tempItem.getX(), tempItem.getY(), null);
							}else if (tempItem.getType()==2) {
								buffg.drawImage(image_item2, tempItem.getX(), tempItem.getY(), null);
							}else if (tempItem.getType()==3) {
								buffg.drawImage(image_item3, tempItem.getX(), tempItem.getY(), null);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}//예외처리 e

		buffg.setColor(Color.BLACK);

		if(escKeyOn) { // esc키를 누를경우 화면을 흐리게 만든다 
			// alpha값을 반투명하게
			alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)100/255); 
			g2.setComposite(alphaComposite); 

			buffg.setColor(Color.BLACK); 
			buffg.fillRect(0, 0, getParent().getWidth(), getParent().getHeight()); 

			// alpha값을 되돌린다 
			alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)255/255); 
			g2.setComposite(alphaComposite); 
		}

		g.drawImage(buffImage, 0, 0, this);

		Toolkit.getDefaultToolkit().sync(); //buffer 처리해주는 듯
	}


	@Override
	public void update(Graphics g) {
		paintComponent(g);
	}

	public void characterMoving(int y) { //캐릭터 움직임 제어(이벤트에서 호출)
		//캐릭터 위치 화면 내에 존재하게 막아둠 
		if(character_y > 550) character_y = 550;
		else if(character_y < 80) character_y = 80;
		else character_y += y;
	}

	public void trash() { // 쓰레기 랜덤생성, 초기값설정
		trash = new SpaceTrash();
		trash.init(mode);
		if(trash.isGenerated()) list_trash.add(trash);		

		if( trash.getX() < -100 ) {
			trash.passed(mode);
		}
	}

	public void item() { // 아이템 랜덤생성, 초기값설정
		item = new Item();
		item.init(list_item);
		if(item.isGenerated()) {
			list_item.add(item);		
		}
		if( item.getX() < -100 ) {
			item.passed(list_item);
		}
	}

	public void crashed() { // 충돌처리

		if( list_trash !=null ) { //쓰레기가 생성됐을 때 충돌 계산 
			for (SpaceTrash tempTrash : list_trash) {
				tempTrash.moving(); // 움직임 제어
				if( character_y+170 < tempTrash.getY()-48 && character_y > tempTrash.getY()-48 || 
						character_y < tempTrash.getY()+48 && character_y+170 > tempTrash.getY()+48 ) { 

					if(character_x < tempTrash.getX()-48 && character_x+50 >tempTrash.getX()-48 || 
							character_x+50 < tempTrash.getX()+48 && character_x >tempTrash.getX()+48 ) {//충돌 처리범위
						tempTrash.setCrached(true);
						if(!game.isInvincibility()) {	//무적상태가 아닐 때만 점수, hp감소						
							game.list_score.add(-10); //충돌시 점수 깎기
							game.setPlayer_Score(game.getPlayer_Score()-10); //쓰레기 충돌시 점수감소
							game.setPlayer_HP(game.getPlayer_HP()-3); //쓰레기 충돌시 hp 감소
						}
						//System.out.println("쓰레기 충돌:"+tempTrash.isCrached());	//충돌처리 확인용					
					}

				}
			}//for
		}

		if(list_item !=null) {//아이템충돌처리
			for (Item tempItem : list_item) {
				tempItem.moving(); 

				if( character_y+170 < tempItem.getY()-48 && character_y > tempItem.getY()-48 || 
						character_y < tempItem.getY()+48 && character_y+170 > tempItem.getY()+48 ) {

					if(character_x < tempItem.getX()-48 && character_x+50 >tempItem.getX()-48 || 
							character_x+50 < tempItem.getX()+48 && character_x >tempItem.getX()+48 ) {
						tempItem.setCrached(true);

						if(tempItem.getType()==1 && !game.isInvincibility()) { //아이템 리스트에 따른 처리 1신발(빨라지기) 2보석(점수 증가) 3포션(hp 회복) 
							//System.out.println("모드스피드 "+game.getGame_Speed(mode));
							game.list_score.add(1);
							speedBG = game.getGame_Speed(mode)/game.getGame_Speed_Up();
							//System.out.println("스피드 "+speedBG);
							game.setInvincibility(true);
						}else if(tempItem.getType()==2){
							game.setPlayer_Score(game.getPlayer_Score()+(scoreUp*3));
							game.list_score.add(2);
						}else {
							game.setPlayer_HP(game.getPlayer_HP()+10);
							game.list_score.add(3);
						}
					}

				}
			}//for
		}
	}


	public void speedBack(int timeCnt) {
		if(speedBG == (game.getGame_Speed(mode)/game.getGame_Speed_Up())) {
			if(timeCnt>63) itemTime++;
			//System.out.println("아이템 지속시간 "+itemTime);
			if(itemTime>1000) {		
				speedBG = game.getGame_Speed(mode);
				game.setInvincibility(false);//무적해제
				itemTime = 0;
			}
		}
	}
	void pauseScreen() {
		label_pause= new JLabel("PAUSE");
		label_pauseInfo = new JLabel("Press Esc to Continue..");
		label_pause.setForeground(color_transparent100);
		label_pause.setFont(font_score);
		label_pause.setSize(600, 150);
		label_pause.setLocation((Main.FRAME_WIDTH-label_pause.getWidth())/2, 180);
		add(label_pause);

		label_pauseInfo.setForeground(color_transparent100);
		label_pauseInfo.setFont(font_hp);
		label_pauseInfo.setSize(600, 50);
		label_pauseInfo.setLocation((Main.FRAME_WIDTH-label_pauseInfo.getWidth())/2+35, Main.FRAME_HEIGHT-350);
		add(label_pauseInfo);
		label_pause.setVisible(false);
		label_pauseInfo.setVisible(false);
	}
	public void pause() {//일시정지 그냥 공회전으로 처리
		//		label_pause = new JLabel(image_pause);
		//		label_pause.setSize(label_pause.getWidth(),label_pause.getHeight());
		//		add(label_pause);

		if(escKeyOn) {
			label_pause.setVisible(true);
			label_pauseInfo.setVisible(true);	
		}
		while(escKeyOn) { //일시정지
			System.out.println(); //확인용

		};
		System.out.println(""); //확인용
		if(!escKeyOn) {
			label_pause.setVisible(false);
			label_pauseInfo.setVisible(false);	
		}
	}

	public void counting() {
		for (int i = 0; i < label_startCount.length-1; i++) {
			try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
			remove(label_startCount[i]);
			label_startCount[i+1].setVisible(true);
		}
		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		label_startCount[label_startCount.length-1].setVisible(false);
	} 


	@Override
	public void addNotify() {
		super.addNotify();
		animator = new Thread(this);
		animator.start();
	}

	@Override
	public void run() {
		bgm.BGM_On();
		game.saferInit();
		counting();

		int timeCnt=0;// 시간을 세기 위한 변수
		while (true) { 
			//checkClearCondition++;
			timeCnt++;

			opacityContorl(); //일시정지시 패널 투명도 조절
			pause(); //일시정지			
			if( game.saferControl(speedBG, timeCnt, mode) ) { // 산소 감소 화면처리
				timeCnt=0;
				String text;
				if(game.getPlayer_Score()>=10000)text = (game.getPlayer_Score()/10000) + "man";
				else text ="SCORE "+game.getPlayer_Score();

				label_score.setText(text);
			}
			hpBar.setValue(game.getPlayer_HP());

			trash(); 
			item();
			crashed();

			speedBack(timeCnt);

			game.gameClear(checkdistance); //게임클리어체크
			game.gameOver(); //게임오버체크
			if(game.isGameClear() || !game.isPlayerAlive()) { //게임끝나면 화면처리
				escKeyOn = true;
				opacityContorl();
				label_resultScore.setVisible(true);
				btn_conform.setVisible(true);
				btn_setting.setEnabled(false);
				if(game.isGameClear()) {
					label_resultScore.setText(""+game.getPlayer_Score());					
					id_Label.setVisible(true);
					id_Text.setVisible(true);
				}else {
					label_gameOver.setVisible(true);					
				}
				break;
			}

			/**
			 * 배경이동
			 */
			if(BG2_x == Main.FRAME_WIDTH || BG1_x == Main.FRAME_WIDTH)
				checkdistance++;
			BG1_x-=5; //이동 시 좌표값 이동 표시
			BG2_x-=5;

			if(BG1_x < -(Main.FRAME_WIDTH)) { // 1번 이미지가 화면에서 사라지면 두번째 이미지의 뒤에 재설정
				BG1_x = Main.FRAME_WIDTH;
			}
			if(BG2_x < -(Main.FRAME_WIDTH)) { //2번 이미지가 사라질때는 1번 이미지의 뒤에 붙음
				BG2_x = Main.FRAME_WIDTH;
			}

			if(trash.isCrached()) try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
			try {Thread.sleep(speedBG);} catch (InterruptedException e) {e.printStackTrace();}
			repaint(); // 반복
			invalidate();

			// 이동 속도조절		

		}
	}
}
