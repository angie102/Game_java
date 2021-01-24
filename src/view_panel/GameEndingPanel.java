package view_panel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Con_GameSystem;



public class GameEndingPanel extends JPanel {

	// ���� ������ ������
	private final int FRAME_WIDTH = 1280;
	private final int FRAME_HEIGHT = 720;

	//��� gif ��������
	private Image backGroundImage;

	private ImageIcon image_buttonRetry;
	private ImageIcon image_buttonRetry_pressed;
	private ImageIcon image_buttonExit;
	private ImageIcon image_buttonExit_pressed;

	//���� �ʱ�ȭ������ ���ư��� ��ư 
	private JButton btn_retry;
	private JButton btn_close;
	
	//��ŷ ȭ�鿡 ǥ���� 1~3�� string ����
	private String lank_First;
	private String lank_Second;
	private String lank_Third;
	
	private String lank_FirstScore;
	private String lank_SecondScore;
	private String lank_ThirdScore;
	
	Font font_lank = new Font("���� ���", Font.BOLD, 28); //�ӽü���
	
	public String getLank_FirstScore() {
		return lank_FirstScore;
	}
	public void setLank_FirstScore(String key) {
		this.lank_FirstScore = game.player_info.get(key)+"";
	}
	public String getLank_SecondScore() {
		return lank_SecondScore;
	}
	public void setLank_SecondScore(String key) {
		this.lank_SecondScore = game.player_info.get(key)+"";
	}
	public String getLank_ThirdScore() {
		return lank_ThirdScore;
	}
	public void setLank_ThirdScore(String key) {
		this.lank_ThirdScore =  game.player_info.get(key)+"";
	}
	public void setLank_First(String key) {
		this.lank_First = key ;
	}
	public String getLank_First() {
		return lank_First;
	}
	public void setLank_Second(String key) {
		this.lank_Second = key ;
	}
	public String getLank_Second() {
		return lank_Second;
	}
	public void setLank_Third(String key) {
		this.lank_Third = key ;
	}
	public String getLank_Third() {
		return lank_Third;
	}
	
	int location_btn_Heigth = 480;

	Con_GameSystem game;//���� ������
	
	
	public GameEndingPanel(Con_GameSystem game) {
		this.game = game;
		//��� ����
		initBG();
		
		//�ʱ�ȭ�� ��ư
		putIntroBt();	
		//�����ϱ� ��ư
		putExitBt();
		

		//��ŷȭ��
		ranking();	//���� ��ŷ �����ϱ� ����
		showLank();	//���ĵ� �����͸� ǥ��

		repaint();
	}
	
	private void ranking() {
		game.readfile();
		game.sortRanking();
		
//		System.out.println(game.keySetList.get(0)); //1�� Ű �� Ȯ�ο�
//		System.out.println(game.keySetList.get(1));	//2��
//		System.out.println(game.keySetList.get(2));	//3��
	}
	
	private void showLank() {
		JPanel panel_showLank = new JPanel();
		
		panel_showLank.setSize(FRAME_WIDTH/3*2, FRAME_HEIGHT/2-30);
		panel_showLank.setLocation(FRAME_WIDTH/6, FRAME_HEIGHT/7);
		panel_showLank.setBackground(new Color(255, 175, 175,0));
		panel_showLank.setLayout(null);
		
		System.out.println(panel_showLank.getX());
		System.out.println(panel_showLank.getY());
		 
		//���̵� ����
		setLank_First(game.keySetList.get(0));
		setLank_Second(game.keySetList.get(1));
		setLank_Third(game.keySetList.get(2));
		//���̵� �� ���� ����
		setLank_FirstScore(game.keySetList.get(0));
		setLank_SecondScore(game.keySetList.get(1));
		setLank_ThirdScore(game.keySetList.get(2));
		
		//���̵� ���̺� 
		JLabel lank_First = new JLabel(getLank_First());
		JLabel lank_Second = new JLabel(getLank_Second());
		JLabel lank_Third = new JLabel(getLank_Third());
		
		//���̵� �Է��� ���� �Է����� ���� ��츦 �Ǻ��ؼ� ���̺� �Է�
		String user_id_data = game.getPlayer_ID()+" �� ";
		String user_id_non = "��� ";
		
		JLabel lank_Mine;
		if(game.getPlayer_ID().equals("")) {
			lank_Mine = new JLabel(user_id_non);
		} else {
			lank_Mine = new JLabel(user_id_data);
		}
		//���� ���̺�
		JLabel lank_FirstScore = new JLabel(getLank_FirstScore());
		JLabel lank_SecondScore = new JLabel(getLank_SecondScore());
		JLabel lank_ThirdScore = new JLabel(getLank_ThirdScore());
		JLabel lank_MineScore = new JLabel(game.getPlayer_Score()+"");
		
		
		//���̵� ���̺� ��ġ ����
		int label_id_width = 250;
		int label_HeigthBleed = 41;
		int label_size_width = 320;
		int label_Heigth = 85;
		
		lank_First.setFont(font_lank);
		lank_First.setSize(label_size_width, 50);
		lank_First.setLocation(label_id_width, label_Heigth);
		lank_First.setForeground(Color.WHITE);
		lank_First.setBackground(Color.PINK);
		lank_First.setHorizontalAlignment(JLabel.LEFT);
		//lank_First.setOpaque(true);

		lank_Second.setFont(font_lank);
		lank_Second.setSize(label_size_width, 50);
		lank_Second.setLocation(label_id_width, label_Heigth+label_HeigthBleed);
		lank_Second.setForeground(Color.WHITE);
		lank_Second.setBackground(Color.PINK);
		lank_Second.setHorizontalAlignment(JLabel.LEFT);
		//lank_Second.setOpaque(true);
	
		lank_Third.setFont(font_lank);
		lank_Third.setSize(label_size_width, 50);
		lank_Third.setLocation(label_id_width, label_Heigth+label_HeigthBleed*2);
		lank_Third.setForeground(Color.WHITE);
		lank_Third.setBackground(Color.PINK);
		lank_Third.setHorizontalAlignment(JLabel.LEFT);
		//lank_Third.setOpaque(true);
		
		lank_Mine.setFont(font_lank);
		lank_Mine.setSize(label_size_width, 50);
		lank_Mine.setLocation(170, 244);
		lank_Mine.setForeground(Color.WHITE);
		lank_Mine.setBackground(Color.PINK);
		lank_Mine.setHorizontalAlignment(JLabel.LEFT);
		//lank_Mine.setOpaque(true);
		
		
		//��ŷ�� ���� ���̺� ����
		int label_score_width = label_id_width+60;
		
		lank_FirstScore.setFont(font_lank);
		lank_FirstScore.setSize(label_size_width, 50);
		lank_FirstScore.setLocation(label_score_width, label_Heigth);
		lank_FirstScore.setForeground(Color.WHITE);
		lank_FirstScore.setBackground(Color.PINK);
		lank_FirstScore.setHorizontalAlignment(JLabel.RIGHT);
		
		lank_SecondScore.setFont(font_lank);
		lank_SecondScore.setSize(label_size_width, 50);
		lank_SecondScore.setLocation(label_score_width, label_Heigth+label_HeigthBleed);
		lank_SecondScore.setForeground(Color.WHITE);
		lank_SecondScore.setBackground(Color.PINK);
		lank_SecondScore.setHorizontalAlignment(JLabel.RIGHT);
		
		lank_ThirdScore.setFont(font_lank);
		lank_ThirdScore.setSize(label_size_width, 50);
		lank_ThirdScore.setLocation(label_score_width, label_Heigth+label_HeigthBleed*2);
		lank_ThirdScore.setForeground(Color.WHITE);
		lank_ThirdScore.setBackground(Color.PINK);
		lank_ThirdScore.setHorizontalAlignment(JLabel.RIGHT);
		
		lank_MineScore.setFont(font_lank);
		lank_MineScore.setSize(label_size_width, 50);
		lank_MineScore.setLocation(210, 244);
		lank_MineScore.setForeground(Color.WHITE);
		lank_MineScore.setBackground(Color.PINK);
		lank_MineScore.setHorizontalAlignment(JLabel.RIGHT);
		
		//��ŷ �гο� ���� ���̱� 
		panel_showLank.add(lank_First);
		panel_showLank.add(lank_FirstScore);

		panel_showLank.add(lank_Second);
		panel_showLank.add(lank_SecondScore);
		
		panel_showLank.add(lank_Third);
		panel_showLank.add(lank_ThirdScore);
		
		panel_showLank.add(lank_Mine);
		panel_showLank.add(lank_MineScore);
		
		
		add(panel_showLank);
	
		setVisible(true);
	}
	
	
	private void initBG() {
		backGroundImage = Toolkit.getDefaultToolkit().createImage("source/Back/BG_rank.gif");
	}

	private void putIntroBt() {
		image_buttonRetry = new ImageIcon("source/Buttons/retry_button.png");
		image_buttonRetry_pressed = new ImageIcon("source/Buttons/retry_button_pressed.png");

		setLayout(null);
		btn_retry = new JButton(image_buttonRetry);
		btn_retry.setSize(image_buttonRetry.getIconWidth(), image_buttonRetry.getIconHeight());
		btn_retry.setLocation(250, location_btn_Heigth);
		add(btn_retry);

		btn_retry.setOpaque(false); 
		btn_retry.setContentAreaFilled(false);
		btn_retry.setBorderPainted(false);


		btn_retry.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn_retry.setIcon(image_buttonRetry_pressed);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btn_retry.setIcon(image_buttonRetry);
			}
		});

		btn_retry.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new PanelTransition().changeIntro("���ư���"); 
			}
		});

	}

	private void putExitBt() {

		image_buttonExit = new ImageIcon("source/Buttons/exit_button.png");
		image_buttonExit_pressed = new ImageIcon("source/Buttons/exit_button_pressed.png");

		setLayout(null);
		btn_close = new JButton(image_buttonExit);
		btn_close.setSize(image_buttonExit.getIconWidth(), image_buttonExit.getIconHeight());
		btn_close.setLocation(700, location_btn_Heigth);
		add(btn_close);

		//System.out.println(CloseBT.getIconWidth()); // ��ư ��ġ �����Ϸ��� ������ ũ�� Ȯ��
		btn_close.setOpaque(false); 
		btn_close.setContentAreaFilled(false);
		btn_close.setBorderPainted(false);

		btn_close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn_close.setIcon(image_buttonExit_pressed);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btn_close.setIcon(image_buttonExit);
			}
		});

		btn_close.addActionListener(new ActionListener() {
			/**
             	�����ư�� ���� ��� ���� �˾��� �߸鼭 ���α׷��� exit��
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "������ �����մϴ�");
				System.exit(0);               
			}
		});

	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(backGroundImage != null) {
			g.drawImage(backGroundImage, 0, 0, this);
		}

	}

}