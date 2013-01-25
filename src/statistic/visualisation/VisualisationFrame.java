package statistic.visualisation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class VisualisationFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private CanvasPanel canvas;
	private ScrollPane scr;
	private StatisticReader reader;
	private Map<String, Map<Integer, SortedMap<Integer, Integer>>> genotypesMap=null;
	
	private int width=1200, height=550;
	
	public VisualisationFrame(String statisticFileURL){
		super("Paint");
		initMe();
		initCanvas(statisticFileURL);
		initControlPanel();
	}
	
	private void initMe(){
		setLayout(new BorderLayout());
		setSize(width+20, height+100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void initCanvas(String statisticFileURL) {
		reader = new StatisticReader(statisticFileURL);
		try {
			canvas = new CanvasPanel(width*8, height, reader.getMaxYear(), reader.getMaxQuantity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		scr   = new ScrollPane();
		scr.add(canvas, BorderLayout.CENTER);
		scr.setMaximumSize(new Dimension(width-50, height-100));
		add(scr);
		startDrawing();
	}
	
	private void initControlPanel() {
		JComboBox<String> genotypeChooser = new JComboBox<String>();
		genotypeChooser.addItem(" ");
		Set<String> genotypes = genotypesMap.keySet();
		for (String genotype : genotypes)
			genotypeChooser.addItem(genotype);
		genotypeChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent genotypeChooserAction) {
				@SuppressWarnings("unchecked")
				JComboBox<String> chooser = (JComboBox<String>)genotypeChooserAction.getSource();
				String genotype = (String) chooser.getSelectedItem();
				if (genotype.trim().isEmpty())
					startDrawing();
				drawOneGenotype(genotype);
			}
		});
		genotypeChooser.setSize(100, 30);
		JPanel panel = new JPanel();
		panel.setSize(width, 32);
		panel.add(genotypeChooser);
		add(panel, BorderLayout.SOUTH);
		panel.setVisible(true);
		genotypeChooser.setVisible(true);
	}
	
	private void startDrawing() {
		try {
			genotypesMap = reader.getGenotypeQuantityHistory();
			Set<String> genotypesKeys = genotypesMap.keySet();
			for (String genotype : genotypesKeys) {
				Map<Integer, SortedMap<Integer, Integer>> agesMap = genotypesMap.get(genotype);
				Set<Integer> agesKeys = agesMap.keySet();
				for (Integer age : agesKeys)
					canvas.drawNewHistory(genotype, age, agesMap.get(age));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void drawOneGenotype(String genotype) {
		canvas.reset();
		Map<Integer, SortedMap<Integer, Integer>> agesMap = genotypesMap.get(genotype);
		Set<Integer> agesKeys = agesMap.keySet();
		for (Integer age : agesKeys)
			canvas.drawNewHistory(genotype, age, agesMap.get(age));
	}

}
