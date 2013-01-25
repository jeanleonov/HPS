package statistic.visualisation;
import genotype.Genome;
import genotype.Genotype;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
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
	
	public void drawNewHistory(String genotypeString, Integer age, SortedMap<Integer, Integer> history) {
		Genotype genotype = Genotype.getGenotype(genotypeString);
		BasicStroke pen = getPenFor(genotype, age);
		Color color = getColorFor(genotype, age);
		Collection<Integer> quantityHistory = history.values();
		int prevYear=0, prevQuantity=0;
		for (Integer quantity : quantityHistory) {
			drawFromTo(prevYear+1, quantity, prevQuantity, color, pen);
			prevYear++;
			prevQuantity = quantity;
		}
	}
	
	public void reset() {
		g2dBI = (Graphics2D) bimg.getGraphics();
		g2dBI.setColor(new Color(220,220,220));
		g2dBI.fillRect(0, 0, width, height);
		g2d = (Graphics2D) getGraphics();
		g2d.setColor(new Color(220,220,220));
		g2d.fillRect(0, 0, width, height);
	}
	
	private Color getColorFor(Genotype genotype, Integer age) {
		Genome  genome1 = genotype.getGenomes()[0],
				genome2 = genotype.getGenomes()[1];
		int red = (genome1.getName() == Genome.GenomeName.R? 120 : 11) + age*10;
		red = red > 255? 255 : red;
		int green = genotype.isFemale()?150:11 + age*10;
		green = green > 255? 255 : green;
		int blue = (genome2.getName() == Genome.GenomeName.R? 120 : 11) + age*10;
		blue = blue > 255? 255 : blue;
		return new Color(red, green, blue);
	}
	
	private BasicStroke getPenFor(Genotype genotype, Integer age) {
		if (age == -1)
			return new BasicStroke(size*2, genotype.isFemale()?BasicStroke.CAP_ROUND:BasicStroke.CAP_SQUARE, 
					BasicStroke.JOIN_BEVEL, 10);
		return new BasicStroke(size, genotype.isFemale()?BasicStroke.CAP_ROUND:BasicStroke.CAP_SQUARE, 
				BasicStroke.JOIN_BEVEL, 10, new float[]{(10-age/2>1)?10-age/2:1, ((10-age>=0)?(10-age)*3:0)}, 0);
	}
	
	private void drawFromTo(int year, int quantity, int prevYearQuantity, Color color, BasicStroke pen) {
		g2d = (Graphics2D) getGraphics();
		g2dBI = (Graphics2D) bimg.getGraphics();
		g2d.setColor(color);
		g2dBI.setColor(color);
		g2d.setClip(0, 0, width, height);
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