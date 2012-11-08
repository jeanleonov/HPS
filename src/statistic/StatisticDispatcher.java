package statistic;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class StatisticDispatcher extends Agent{

	private static final long serialVersionUID = 1L;
	
	private String fileLocation = "statistic.csv";	// TODO: default value must be at another place
	private Vector<StatisticPackage> packages = new Vector<StatisticPackage>() ;
	
	protected boolean busy = false;

	/**
	 * ���������� ���������, �������� ���������, ��������� �������������
	 */
	@Override
	protected void setup(){
		fileLocation = (String)getArguments()[0];
		addBehaviour(new StatisticDispatcherBehaviour());
		confirmation((AID)getArguments()[1]);
	}

	/**
	 * ��������� ������������� � �������
	 */
	private void confirmation(AID systemStarter) {
		ACLMessage confirm = new ACLMessage(ACLMessage.CONFIRM);
		confirm.addReceiver(systemStarter);
		send(confirm);
	}

	/**
	 * �������� ����� ���������� {�����������, ����, ���, �������������}
	 * @param pack
	 */
	void addPackage(StatisticPackage pack) {
		packages.add(pack);
	}

	/**
	 * ������� ������ � ���� � �������� ��������� �������
	 */
	void exportToFile() {
		busy = true;
		try {
			File file = createFile();
			writeStatistic(file);
			packages.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
		busy = false;
	}
	
	protected boolean isBusy() {
		if(busy) return true;
		if(!packages.isEmpty()) {
			System.err.println("dafuq?");
			return true;
		}
		return false;
	}
	
	/**
	 * ������� ������ � ����
	 * @param file
	 */
	private void writeStatistic(File file) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			for (StatisticPackage pack : packages) {
				bw.write(pack.toString());
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * ������� ��� ������� ������������ ����
	 * @return
	 * @throws IOException
	 */
	private File createFile() throws IOException {
		File file = new File(fileLocation);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
}