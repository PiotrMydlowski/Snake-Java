package snake;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static final int WIDTH = 200, HEIGHT = 200;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Menu frame = new Menu();
					//frame.setVisible(true);
					
					JFrame menu = new Menu();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Menu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		this.setUndecorated(true);
		this.setFocusable(true);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnNewButton = new JButton("START");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame game = new JFrame();
				Screen screen = new Screen(WIDTH, HEIGHT);
				game.getContentPane().add(screen);
				game.setUndecorated(true);
				game.setResizable(false);
				game.pack();
				game.setLocationRelativeTo(null);
				game.setVisible(true);
			}
		});
		btnNewButton.setFont(new Font("Calibri", Font.BOLD, 24));
		contentPane.add(btnNewButton, BorderLayout.NORTH);
		
		JButton btnNewButton_1 = new JButton("CLOSE");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnNewButton_1.setFont(new Font("Calibri", Font.BOLD, 24));
		contentPane.add(btnNewButton_1, BorderLayout.SOUTH);
		
		JButton btnNewButton_2 = new JButton("HIGHSCORES");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFrame highscore = new JFrame();
				
				List<String> input = new ArrayList<String>();
				String output = "";
				
				Charset charSet = Charset.forName("ISO-8859-1");
				
				try 
				{
					input = Files.readAllLines(Paths.get("highscore.txt"), charSet);	
				}
				catch (IOException exception) 
				{
					//exception.printStackTrace();
				}
				if(!input.isEmpty())
				{
					output += "<html><p>";
					
					for(int i = 0; i< input.size();i++) 
					{
						output += String.valueOf(i+1);
						output += ". ";
						output += input.get(i);
						output += "<br>";
						
					}	
					output += "<p></html>";
				}
				else 
				{
					output = "There are no highscores now.";
				}
				
				JLabel scoreLabel = new JLabel();
				scoreLabel.setText(output);
				
				//scoreLabel.setLocation(0,0);
				highscore.getContentPane().add(scoreLabel);
								
				highscore.getContentPane().setLayout(new FlowLayout());	
				highscore.setVisible(true);
				highscore.setFocusable(true);
				highscore.setPreferredSize(new Dimension(WIDTH+20, HEIGHT));
				highscore.setResizable(false);
				highscore.pack();
				highscore.setLocationRelativeTo(null);
				highscore.setTitle("HIGHSCORES");
			}
		});
		btnNewButton_2.setFont(new Font("Calibri", Font.BOLD, 24));
		contentPane.add(btnNewButton_2, BorderLayout.NORTH);
	}

}
