package veiw_frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import view_panel.GameIntroPanel;
import view_panel.GamePlayingPanel;

public class MainFrame extends JFrame {
	private final int FRAME_WIDTH = 1280;
	private final int FRAME_HEIGHT = 720;
	
	public final String PLAY_PAGE = "play page";
	public final String INTRO_PAGE = "intro page";
	
	private CardLayout cardLayout;
	boolean isIntroPanelVisible;

	public JPanel mainPane;
	private JPanel panel_playing;
	private JPanel panel_Intro;
	private JButton btn_introToStart ;

	public MainFrame(){
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setTitle("Space Game");
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setLocation((dim.width/2)-(this.getWidth()/2), (dim.height/2)-(this.getHeight()/2));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		cardLayout = new CardLayout();

		mainPane = new JPanel();
		mainPane.setLayout(cardLayout);

		panel_playing = new GamePlayingPanel();
		panel_Intro = new GameIntroPanel();
		mainPane.add(INTRO_PAGE, panel_Intro);

		showIntroPane();

		btn_introToStart = new JButton("Switch Panes");
		btn_introToStart.addActionListener(e -> switchPanes() );

		setResizable(false);
		setLayout(new BorderLayout());
		add(mainPane);
		setVisible(true);
		
		createBufferStrategy(2);

	}

	void switchPanes() {
		showPalyPanel();
	}

	void showIntroPane() {
		cardLayout.show(mainPane, INTRO_PAGE);
		isIntroPanelVisible = true;
	}

	public void showPalyPanel() {
		mainPane.add(PLAY_PAGE, panel_playing);
		mainPane.remove(panel_Intro);
		
		cardLayout.show(mainPane, PLAY_PAGE);
		panel_playing.setFocusable(true);
		panel_playing.requestFocusInWindow();

		isIntroPanelVisible = false;
	}

}