
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Launcher{
	public static void main(String[] args) {
		JFrame f = new JFrame("Standing by to launch . . . ");
		f.setSize(400,600);
		f.setLayout(null);
		f.setVisible(true);
		
		JButton button_start = new JButton("Start Game");
		button_start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				f.dispose();
				new GameBoard();
			}
			
		});
		
		
		button_start.setBounds(5,510,380,50);
		f.add(button_start);
	
	}
	
}
