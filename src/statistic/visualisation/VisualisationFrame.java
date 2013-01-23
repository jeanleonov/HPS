package statistic.visualisation;

import java.awt.BorderLayout;
import java.awt.ScrollPane;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.swing.JFrame;


public class VisualisationFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private CanvasPanel canvas;
	private ScrollPane scr;
	private StatisticReader reader;
	
	private int width=1200, height=600;
	
	public VisualisationFrame(String statisticFileURL){
		super("Paint");
		initMe();
		initMyComponents(statisticFileURL);
	}
	
	private void initMe(){
		setLayout(new BorderLayout());
		setSize(width+20, height+80);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void initMyComponents(String statisticFileURL) {
		try {
			reader = new StatisticReader(statisticFileURL);
			canvas = new CanvasPanel(width*8, height, reader.getMaxYear(), reader.getMaxQuantity());
			scr   = new ScrollPane();
			scr.add(canvas, BorderLayout.CENTER);
			add(scr);
			startDrawing();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void startDrawing() throws IOException {
			Map<String,SortedMap<Integer,Integer>> map = reader.getGenotypeQuantityHistory();
			Set<String> keys = map.keySet();
			for (String key : keys)
				canvas.drawNewHistory(key, map.get(key));
	}

}
