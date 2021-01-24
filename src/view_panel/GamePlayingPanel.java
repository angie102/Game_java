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
	//��ü
	Con_BGM bgm = new Con_BGM();	//���� ��ü
	Con_GameSystem game = new Con_GameSystem();
	SpaceTrash trash = new SpaceTrash(); //�����ⰴü
	Item item = new Item(); //�����۰�ü

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

	Font font_hp = new Font("Verdana", Font.BOLD, 35); //�ӽü���
	Font font_score = new Font("Verdana", Font.BOLD, 140); //�ӽü���


	private Thread animator;

	/**
	 * ��ġ��
	 */
	private int BG2_x = 0;
	private int BG1_x = 0;

	private int character_y = Main.FRAME_HEIGHT/2-130;
	private int character_x = 50;
	private int Character_speed = 20;



	ArrayList<SpaceTrash> list_trash = new ArrayList<SpaceTrash>(); //������ ����Ʈ
	ArrayList<Item> list_item = new ArrayList<Item>(); //�����۸���Ʈ

	private boolean escKeyOn = false; //�Ͻ�������
	private String mode= "Normal";
	private int itemTime = 0;
	private int scoreUp =10;
	private int checkdistance =0;
	private int speedBG = game.getGame_Speed(mode) ; // ��� �̵� �ӵ�

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

		//��� ����
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


		//Ű���� �̺�Ʈ ó��
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
	private void setting() { //�����г�
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

		//���� ���� ���
		label_resultScore = new JLabel();
		label_resultScore.setFont(font_score);
		label_resultScore.setSize(700, 150);
		label_resultScore.setLocation(Main.FRAME_WIDTH/2-label_resultScore.getWidth()/2, 180);
		label_resultScore.setForeground(Color.WHITE);
		label_resultScore.setHorizontalAlignment(JLabel.CENTER);
		label_resultScore.setVisible(false);
		add(label_resultScore);

		//���ӿ���
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
		id_Text.addKeyListener(new KeyAdapter() { //text�ʵ忡�� enter ó��
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()== KeyEvent.VK_ENTER) saveScoreAndTransition();
			}
		});
		addKeyListener(new KeyAdapter() {// text�ʵ� ���������ʰ� enter�� �Է� ó��
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()== KeyEvent.VK_ENTER) saveScoreAndTransition();
			}
		});
		btn_conform.addActionListener( new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!game.isPlayerAlive()) new PanelTransition().changeIntro("���ư���"); 
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
		new PanelTransition().changeEnd("��������", game);	
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
	 * �̹��� �ε�
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



	//���׸��� *** 1�ȼ� Ʋ�����°� ��� �����غ���
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//��� �̹��� �ΰ��� �����ư��鼭 ȣ��

		if(buffg == null) {
			buffImage = createImage(this.getWidth(), this.getHeight());
			if (buffImage == null) {
				System.out.println("���� ���۸��� ���� ��ũ�� ���� ����");
			}else{
				buffg = buffImage.getGraphics();
			}
		}
		Graphics2D g2 = (Graphics2D)buffg;

		buffg.drawImage(image_BG, BG1_x, 0, null); 
		buffg.drawImage(image_BG, BG2_x, 0, null); 
		//g.drawImage(image, BG2_x+150, 0, this);//1�� 2 �̹��� ���̿� ƴ�� ����µ� ó����� ���

		if(checkdistance>game.getCleardistance()-1) { // ����������
			buffg.drawImage(image_ssi, BG2_x+1700, 0, null); 
			//System.out.println("Ŭ���� ���� Ȯ��");
		}

		buffg.drawImage(image_character, character_x, character_y, null); // ĳ���� �׸���

		try {//����ó��
			if(list_trash != null) {		
				for (SpaceTrash tempTrash : list_trash) {
					if(tempTrash.isGenerated()) {
						if(tempTrash.isCrached()) {
							//System.out.println("�浹�̹��� �ҷ�����");
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
							//System.out.println("�浹�̹��� �ҷ�����");
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
		}//����ó�� e

		buffg.setColor(Color.BLACK);

		if(escKeyOn) { // escŰ�� ������� ȭ���� �帮�� ����� 
			// alpha���� �������ϰ�
			alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)100/255); 
			g2.setComposite(alphaComposite); 

			buffg.setColor(Color.BLACK); 
			buffg.fillRect(0, 0, getParent().getWidth(), getParent().getHeight()); 

			// alpha���� �ǵ����� 
			alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)255/255); 
			g2.setComposite(alphaComposite); 
		}

		g.drawImage(buffImage, 0, 0, this);

		Toolkit.getDefaultToolkit().sync(); //buffer ó�����ִ� ��
	}


	@Override
	public void update(Graphics g) {
		paintComponent(g);
	}

	public void characterMoving(int y) { //ĳ���� ������ ����(�̺�Ʈ���� ȣ��)
		//ĳ���� ��ġ ȭ�� ���� �����ϰ� ���Ƶ� 
		if(character_y > 550) character_y = 550;
		else if(character_y < 80) character_y = 80;
		else character_y += y;
	}

	public void trash() { // ������ ��������, �ʱⰪ����
		trash = new SpaceTrash();
		trash.init(mode);
		if(trash.isGenerated()) list_trash.add(trash);		

		if( trash.getX() < -100 ) {
			trash.passed(mode);
		}
	}

	public void item() { // ������ ��������, �ʱⰪ����
		item = new Item();
		item.init(list_item);
		if(item.isGenerated()) {
			list_item.add(item);		
		}
		if( item.getX() < -100 ) {
			item.passed(list_item);
		}
	}

	public void crashed() { // �浹ó��

		if( list_trash !=null ) { //�����Ⱑ �������� �� �浹 ��� 
			for (SpaceTrash tempTrash : list_trash) {
				tempTrash.moving(); // ������ ����
				if( character_y+170 < tempTrash.getY()-48 && character_y > tempTrash.getY()-48 || 
						character_y < tempTrash.getY()+48 && character_y+170 > tempTrash.getY()+48 ) { 

					if(character_x < tempTrash.getX()-48 && character_x+50 >tempTrash.getX()-48 || 
							character_x+50 < tempTrash.getX()+48 && character_x >tempTrash.getX()+48 ) {//�浹 ó������
						tempTrash.setCrached(true);
						if(!game.isInvincibility()) {	//�������°� �ƴ� ���� ����, hp����						
							game.list_score.add(-10); //�浹�� ���� ���
							game.setPlayer_Score(game.getPlayer_Score()-10); //������ �浹�� ��������
							game.setPlayer_HP(game.getPlayer_HP()-3); //������ �浹�� hp ����
						}
						//System.out.println("������ �浹:"+tempTrash.isCrached());	//�浹ó�� Ȯ�ο�					
					}

				}
			}//for
		}

		if(list_item !=null) {//�������浹ó��
			for (Item tempItem : list_item) {
				tempItem.moving(); 

				if( character_y+170 < tempItem.getY()-48 && character_y > tempItem.getY()-48 || 
						character_y < tempItem.getY()+48 && character_y+170 > tempItem.getY()+48 ) {

					if(character_x < tempItem.getX()-48 && character_x+50 >tempItem.getX()-48 || 
							character_x+50 < tempItem.getX()+48 && character_x >tempItem.getX()+48 ) {
						tempItem.setCrached(true);

						if(tempItem.getType()==1 && !game.isInvincibility()) { //������ ����Ʈ�� ���� ó�� 1�Ź�(��������) 2����(���� ����) 3����(hp ȸ��) 
							//System.out.println("��彺�ǵ� "+game.getGame_Speed(mode));
							game.list_score.add(1);
							speedBG = game.getGame_Speed(mode)/game.getGame_Speed_Up();
							//System.out.println("���ǵ� "+speedBG);
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
			//System.out.println("������ ���ӽð� "+itemTime);
			if(itemTime>1000) {		
				speedBG = game.getGame_Speed(mode);
				game.setInvincibility(false);//��������
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
	public void pause() {//�Ͻ����� �׳� ��ȸ������ ó��
		//		label_pause = new JLabel(image_pause);
		//		label_pause.setSize(label_pause.getWidth(),label_pause.getHeight());
		//		add(label_pause);

		if(escKeyOn) {
			label_pause.setVisible(true);
			label_pauseInfo.setVisible(true);	
		}
		while(escKeyOn) { //�Ͻ�����
			System.out.println(); //Ȯ�ο�

		};
		System.out.println(""); //Ȯ�ο�
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

		int timeCnt=0;// �ð��� ���� ���� ����
		while (true) { 
			//checkClearCondition++;
			timeCnt++;

			opacityContorl(); //�Ͻ������� �г� ���� ����
			pause(); //�Ͻ�����			
			if( game.saferControl(speedBG, timeCnt, mode) ) { // ��� ���� ȭ��ó��
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

			game.gameClear(checkdistance); //����Ŭ����üũ
			game.gameOver(); //���ӿ���üũ
			if(game.isGameClear() || !game.isPlayerAlive()) { //���ӳ����� ȭ��ó��
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
			 * ����̵�
			 */
			if(BG2_x == Main.FRAME_WIDTH || BG1_x == Main.FRAME_WIDTH)
				checkdistance++;
			BG1_x-=5; //�̵� �� ��ǥ�� �̵� ǥ��
			BG2_x-=5;

			if(BG1_x < -(Main.FRAME_WIDTH)) { // 1�� �̹����� ȭ�鿡�� ������� �ι�° �̹����� �ڿ� �缳��
				BG1_x = Main.FRAME_WIDTH;
			}
			if(BG2_x < -(Main.FRAME_WIDTH)) { //2�� �̹����� ��������� 1�� �̹����� �ڿ� ����
				BG2_x = Main.FRAME_WIDTH;
			}

			if(trash.isCrached()) try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
			try {Thread.sleep(speedBG);} catch (InterruptedException e) {e.printStackTrace();}
			repaint(); // �ݺ�
			invalidate();

			// �̵� �ӵ�����		

		}
	}
}
