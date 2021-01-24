package view_panel;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;


/**
	게임 실행 시 가장 먼저 나타나는 인트로 패널
 */
public class GameIntroPanel extends JPanel {

	// 고정 프레임 사이즈
	private final int FRAME_WIDTH = 1280;
	private final int FRAME_HEIGHT = 720;
	
	//배경 gif 참조변수
	private Image image_BG;
	private ImageIcon image_StartBT;
	private ImageIcon image_StartBT_pressed;
	Container c = getParent();

	//게임 시작 버튼 
	private JButton btn_introToStart;
	
	public GameIntroPanel() {
		//배경 생성
		initBG();
		putBt();
		
	}
		
	private void initBG() {
		//toolkit으로 gif 삽입
		image_BG = Toolkit.getDefaultToolkit().createImage("source/Back/BG_intro2.gif");
	}
	
	private void putBt() {
		
		image_StartBT = new ImageIcon("source/Buttons/start_button.png");
		image_StartBT_pressed = new ImageIcon("source/Buttons/start_button_pressed.png");
		
		//버튼의 위치를 가운데 놓으려고 조정함
		int MID_FRAME_WIDTH = (FRAME_WIDTH-image_StartBT.getIconWidth())/2;
		
		setLayout(null);
		btn_introToStart = new JButton(image_StartBT);
		
		// 버튼 뒤 투명화 처리
		btn_introToStart.setOpaque(false); 
		btn_introToStart.setContentAreaFilled(false);
		btn_introToStart.setBorderPainted(false);

		

		btn_introToStart.setSize(image_StartBT.getIconWidth(), image_StartBT.getIconHeight());
		btn_introToStart.setLocation(MID_FRAME_WIDTH, 450);
		add(btn_introToStart);
		btn_introToStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn_introToStart.setIcon(image_StartBT_pressed);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btn_introToStart.setIcon(image_StartBT);
			}
			
		});
		
		btn_introToStart.addActionListener(new ActionListener() {
			/**
			 게임 시작 버튼을 누르면 게임플레이 화면으로 넘어감
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				removeAll();

				JPanel panel_playing = new GamePlayingPanel();
				add(panel_playing);
				panel_playing.setFocusable(true);
		        panel_playing.requestFocusInWindow();
			
			}
		});
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(image_BG != null) {
			g.drawImage(image_BG, 0, 0, this);
		}

	}
		
	
	
	
}
