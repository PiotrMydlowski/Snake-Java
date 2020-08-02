package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import java.awt.Font;

public class Screen extends JPanel implements Runnable, KeyListener 
{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 200, HEIGHT = 200;
	private Thread thread; //w¹tek
	private boolean running = false; //flaga uruchomienia
	private BodyPart b; //czêœæ wê¿a
	private ArrayList<BodyPart> snake; //w¹¿
	private Apple apple; //jab³ko
	private ArrayList<Apple> apples; //jab³ka
	private Random r;
	private int xCoor = 10, yCoor = 10; //pocz¹tkowe po³o¿enie
	private int size = 5; //pocz¹tkowy rozmiar
	private int snakeSpeed=250000*4; //prêdkoœæ wê¿a
	// pocz¹tkowy kierunek ruchu
	private boolean right = true, left = false, up = false, down =false; private int ticks = 0; // licznik tików
	
	public Screen(int WIDTH,int HEIGHT) 
	{
		setFocusable(true);
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		r = new Random();
		snake = new ArrayList<BodyPart>();
		apples = new ArrayList<Apple>();
		start();
	}
	
	public int getLength() 
	{// wynik to d³ugoœæ wê¿a, wiêc nie da siê uzyskaæ <5
		return snake.size();
	}
	
	public void tick() 
	{
		//jezeli nie ma weza to stworz
		if (snake.size() == 0) 
		{//nowa czesc weza (x,y,rozmiar kwadratu w siatce)
			b = new BodyPart(xCoor, yCoor, 10);
			//dodanie czesci do listy weza
			snake.add(b);
		}
		
		//jezeli nie ma jablka to stworz
		if(apples.size() == 0) 
		{//losowanie wsp dla jablka
			int xCoor = r.nextInt(19);
			int yCoor = r.nextInt(19);
			//dodanie jablka
			apple = new Apple(xCoor, yCoor, 10);
			apples.add(apple);
		}
		
		// sprawdzenie czy waz zjadl jablko
		//jezeli tak to zwieksz rozmiar i usun jablko
		for(int i = 0; i < apples.size(); i++) 
		{
			if(xCoor == apples.get(i).getxCoor() &&
					yCoor == apples.get(i).getyCoor()) 
			{
				size++;
				apples.remove(i);
				i++;
			}
		}
		
		//sprawdzenie czy waz nie ugryzl ogona
		for(int i =0; i < snake.size(); i++) 
		{
			if(xCoor == snake.get(i).getxCoor() &&
					yCoor == snake.get(i).getyCoor()) 
			{
				if(i != snake.size() - 1) 
				{
					stop();
				}
			}
		}
		
		//sprawdzenie czy w¹¿ wszed³ w œcianê
		if(xCoor < 0 || xCoor > 19 || yCoor < 0 || yCoor > 19) 
		{
			stop();
		}
		
		ticks++;
		//po wykonaniu tej metody wiecej razy ni¿ wartosc snakeSpeed
		if(ticks > snakeSpeed) 
		{	//sprawdzenie który przycisk byl wcisniety
		//dodanie lub odjecie 1 od odpowiednich wspolrzednych
			if(right) xCoor++;
			if(left) xCoor--;
			if(up) yCoor--;
			if(down) yCoor++;
			//wyzerowanie licznika
			ticks = 0;
			//dodawanie elementu wê¿a dla nowych wspolrzednych
			b = new BodyPart(xCoor, yCoor, 10);
			snake.add(b);
			//sprawdzenie czy w¹¿ jest wiêkszy od wartoœci size
			//jezeli tak to usuwana jest ostatnia czesc
			if(snake.size() > size) 
			{
				snake.remove(0);
			}
		}
	}
	
	public void paint(Graphics g) 
	{
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		for (int i = 0; i < WIDTH / 10; i++) 
		{
			g.drawLine(i * 10, 0, i * 10, HEIGHT);
		}
		
		for (int i = 0; i < HEIGHT / 10; i++) 
		{
			g.drawLine(0, i * 10, WIDTH, i * 10);
		}
		
		//printing score on the screen
		g.setColor(Color.RED);
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		String score = "Score: " + String.valueOf(this.getLength());
		g.drawString(score,10,20);
		
		for (int i = 0; i < snake.size(); i++) 
		{
			snake.get(i).draw(g);
		}
		
		for(int i = 0; i < apples.size(); i++) 
		{
			apples.get(i).draw(g);
		}
	}
	
	public void start() 
	{
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void updateHighscore()
	{//funkcja odczytuje plik z wynikami dodaje obecny w kolejnoœci, a nastêpnie zapisuje size pierwszych wyników
		List<String> input = new ArrayList<String>();
		List<String> output = new ArrayList<String>();
		List<Integer> values = new ArrayList<Integer>();
		List<String> dates = new ArrayList<String>();
		String s;
		int size = 10;
		
		Charset charSet = Charset.forName("ISO-8859-1");
		
		try 
		{
			input = Files.readAllLines(Paths.get("highscore.txt"), charSet);	
		}
		catch (IOException exception) 
		{
			new File("highscore.txt");
			//exception.printStackTrace();
		}
		if(!input.isEmpty())
		{	
			for(int i = 0; i< input.size();i++) 
			{
				values.add(Integer.parseInt(input.get(i).split(",")[0]));
				dates.add((input.get(i).split(",")[1]));
			}
		
			for(int i = 0; i< size;i++) 
			{
				
				if(i == input.size()) 
				{
					values.add(this.getLength());
					Date date = new Date();
					dates.add(date.toString());
					break;
				}
				
				if(values.get(i)<this.getLength()) 
				{
					values.add(i, this.getLength());
					Date date = new Date();
					dates.add(i, date.toString());
					break;
				}
			}
				
		}
		
		else 
		{
			values.add(this.getLength());
			Date date = new Date();
			dates.add(date.toString());
		}

		
		for(int i = 0; i< dates.size();i++) 
		{
			if(i > (size -1)) 
			{
				break;
			}
			s = "";
			s += String.valueOf(values.get(i));
			s += ",";
			s += dates.get(i);
			output.add(s);
		}
		
		/*
		for(int i = 0; i< output.size();i++) 
		{
			System.out.println(output.get(i));
		}
		*/
		
		try 
		{
			Files.write(Paths.get("highscore.txt"), output, StandardOpenOption.CREATE);
		} 
		catch (IOException exception) 
		{
			exception.printStackTrace();
		}
		
		
	}
	
	public void stop() 
	{
		running = false;
		setVisible(false);
		final Frame[] frames = Frame.getFrames();
		for (final Frame frame : frames) 
		{
			if (frame.isVisible() && frame.isActive()) 
			{
				frame.dispose();
			}
		}
		
		updateHighscore();
		
		try 
		{
		thread.join();
		} 
		catch (InterruptedException e) 
		{ 
			e.printStackTrace();
		}
	}
		
	public void run() 
	{
		while (running) 
		{
			tick();
			repaint();
		}
	}
		
	@Override
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_RIGHT && !left) 
		{
			up = false;
			down = false;
			right = true;
		}
		
		if(key == KeyEvent.VK_LEFT && !right) 
		{
			up = false;
			down = false;
			left = true;
		}
		
		if(key == KeyEvent.VK_UP && !down) 
		{
			left = false;
			right = false;
			up = true;
		}
		
		if(key == KeyEvent.VK_DOWN && !up) 
		{
			left = false;
			right = false;
			down = true;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) 
	{
	}
		
	public void keyTyped(KeyEvent arg0) 
	{
	}
	
}
