package statistic.visualisation;
import java.util.Collection;
import java.util.Random;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.SortedMap;

import javax.swing.JPanel;

public class CanvasPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage bimg;
	private int size=2,
		width,
		height,
		numberOfYears, maxQuantity;
	private Graphics2D g2d, g2dBI;
	private BasicStroke pen;
	
	public CanvasPanel(int w, int h, int numberOfYears, int maxQuantity){
		super(null);
		width =  w;	height = h;
		setPreferredSize(new Dimension(width, height));
		setBackground(new Color(220,220,220));
		bimg = new	BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g2dBI = (Graphics2D) bimg.getGraphics();
		g2dBI.setColor(new Color(220,220,220));
		g2dBI.fillRect(0, 0, width, height);
		this.numberOfYears = numberOfYears;
		this.maxQuantity = maxQuantity;
		setMaximumSize(new Dimension(width, height));
	}
	
	public void drawNewHistory(String genotype, SortedMap<Integer, Integer> history) {
		Collection<Integer> quantityHistory = history.values();
		int prevYear=0, prevQuantity=0;
		Random rand = new Random();
		Color color = new Color(Math.abs(rand.nextInt()%200), Math.abs(rand.nextInt()%200), Math.abs(rand.nextInt()%200));
		for (Integer quantity : quantityHistory) {
			drawFromTo(prevYear+1, quantity, color, prevQuantity);
			prevYear++;
			prevQuantity = quantity;
		}
	}
	
	private void drawFromTo(int year, int quantity, Color color, int prevYearQuantity) {
		g2d = (Graphics2D) getGraphics();
		g2dBI = (Graphics2D) bimg.getGraphics();
		g2d.setColor(color);
		g2dBI.setColor(color);
		g2d.setClip(0, 0, width, height);
		pen = new BasicStroke(size, BasicStroke.CAP_ROUND, 
				BasicStroke.JOIN_ROUND);
		g2d.setStroke(pen);
		g2dBI.setStroke(pen);
		int prevX = width*(year-2)/numberOfYears,
			prevY = height - height*prevYearQuantity/maxQuantity,
			x = width*(year-1)/numberOfYears,
			y = height - height*quantity/maxQuantity;
		g2d.drawLine(prevX, prevY, x, y);
		g2dBI.drawLine(prevX, prevY, x, y);
	}
	
	@Override
	public void paint(Graphics gr) {
		g2d = (Graphics2D) gr;
		g2d.drawImage(bimg, 0, 0, this);
	}
}