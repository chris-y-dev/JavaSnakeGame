import javax.swing.JFrame;

public class GameFrame extends JFrame{

	GameFrame(){
	
		this.add(new GamePanel());
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack(); //packs any components we add to fit in JFrame
		this.setVisible(true);
		this.setLocationRelativeTo(null); //sets our window to open in middle of computer
	}
	
}
