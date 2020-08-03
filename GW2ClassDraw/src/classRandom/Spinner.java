package classRandom;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Spinner implements ActionListener, MouseListener
{
	public static Spinner spinner;
	public static final int WIDTH = 388, HEIGHT = 540;
	private Renderer renderer;
	private ArrayList<Image> slides;
	private ArrayList<String> fileNames;
	private int x, slowRoll =5;
	private boolean spinning, firstDraw;
	public double speed = 0, maxSpeed=250,decel=0;
		
	private Random rand = new Random();
	
	private Image tmpImg = null;
	
	public Spinner() {
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);
		renderer = new Renderer();
		x = 0;
		//class names
		List<String> namesList = Arrays.asList(
				"/img/elementalist.png", //core light
				"/img/mesmer.png",
				"/img/necromancer.png",
				"/img/scrapper.png", //HoT medium
				"/img/druid.png",
				"/img/daredevil.png",
				"/img/firebrand.png", //PoF heavy
				"/img/renegade.png",
				"/img/spellbreaker.png",
				"/img/tempest.png", //HoT light
				"/img/chronomancer.png",
				"/img/reaper.png",
				"/img/soulbeast.png", //PoF medium
				"/img/deadeye.png",
				"/img/holosmith.png",
				"/img/guardian.png", //core heavy
				"/img/revenant.png",
				"/img/warrior.png",
				"/img/weaver.png", //PoF light
				"/img/mirage.png",
				"/img/scourge.png",
				"/img/ranger.png", //core medium
				"/img/thief.png",
				"/img/engineer.png",
				"/img/dragonhunter.png", //HoT heavy
				"/img/herald.png",
				"/img/berserker.png"
				);
		fileNames = new ArrayList<String>();
		fileNames.addAll(namesList);
		
		//create slides array
		slides = new ArrayList<Image>();
		for (int i = 0; i<fileNames.size(); i++) {
			tmpImg = getImage(fileNames.get(i));
			slides.add(tmpImg);
		}	
		tmpImg = getImage("/img/Arrow.png");
		
		firstDraw = true;
		jframe.add(renderer);
		jframe.setTitle("Spec Randomizer");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH,HEIGHT);
		jframe.addMouseListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);
			
		timer.start();
		spinning = false;
	}
	
	public static void main(String[] args) {
		spinner = new Spinner();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(spinning == true && speed >=maxSpeed){
			decel = 0.75+(0.95-0.75)*rand.nextDouble();
			spinning = false;
		}
		else if (spinning == false && speed == 0 || speed <0 ) {
			speed=1;
			spinning = true;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//image loop effect
		if (Math.abs(x) > WIDTH*slides.size()) {
			x = Math.abs(x)-WIDTH*(slides.size())+1;
			firstDraw=false;
		}
		
		if (spinning)
		{
			if (firstDraw == true) {
				//slide acceleration reaching max visible
				if (speed < maxSpeed/25*2) {
					x -= speed;
					speed = speed*1.05;
				}
				else if (speed >=maxSpeed/25*2) {
					x-=speed;
				}
			}
			else if (firstDraw ==false) {
				//slide acceleration reaching max
				if (speed < maxSpeed) {
					x -= speed;
					speed = speed*1.05;
				}
				else {
					x-=speed;
				}
			}
		}
		
		else if (!spinning) {
			
			if (speed == 0) { //snap back to full image 
				if (Math.abs(x%WIDTH) <= WIDTH/2) {
					x = x/WIDTH*WIDTH;
				}
				else {
					x = (x/WIDTH-1)*WIDTH;
				}
			}
			
			if (slowRoll >0) {
				x -= speed;
				slowRoll--;
			}
			else if (slowRoll ==0) {
				x -= speed;
				speed = speed*decel;
				slowRoll = 5;
			}
			
			if (speed < 0.5){
				speed = 0;
			}
	 	}
		renderer.repaint();
	}

	public void repaint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		//draw class images
		for (int i = 0; i < slides.size();i++) {
			g2.drawImage(slides.get(i), WIDTH*(i)+x,0, WIDTH, HEIGHT, null);
		}
			g2.drawImage(slides.get(0), WIDTH*(slides.size())+x,0, WIDTH, HEIGHT, null);
		
		//draw pointer arrow
		g2.drawImage(tmpImg,WIDTH/2-38, 0, null);

	}

	public Image getImage(String path)
	{
		BufferedImage tempImage = null;
		try	{
			tempImage = ImageIO.read(Spinner.class.getResource(path));
			//System.out.println("Image added: "+path);
		}
		catch (IOException e) {
			System.out.println("An error occured - "+e.getMessage());
			e.printStackTrace();
		}

		
		return tempImage;
	}
}