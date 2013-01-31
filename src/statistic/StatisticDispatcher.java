package statistic;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import starter.Shared;

public class StatisticDispatcher extends Agent{

	private static final long serialVersionUID = 1L;
	
	private String fileLocation = Shared.DEFAULT_STATISTIC_FILE;
	private Vector<StatisticPackage> packages = new Vector<StatisticPackage>() ;
	
	protected boolean busy = false;

	/**
	 * Обработать аргументы, добавить поведения, отправить подтверждение
	 */
	@Override
	protected void setup(){
		fileLocation = (String)getArguments()[0];
		addBehaviour(new StatisticDispatcherBehaviour());
		confirmation((AID)getArguments()[1]);
	}

	/**
	 * Отправить подтверждение о запуске
	 */
	private void confirmation(AID systemStarter) {
		ACLMessage confirm = new ACLMessage(ACLMessage.CONFIRM);
		confirm.addReceiver(systemStarter);
		send(confirm);
	}

	/**
	 * Добавить пакет статистики {эксперимент, зона, год, распределение}
	 * @param pack
	 */
	void addPackage(StatisticPackage pack) {
		packages.add(pack);
	}

	/**
	 * Сделать запись в файл и очистить коллекцию пакетов
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
	 * Сделать запись в файл
	 * @param file
	 */
	private void writeStatistic(File file) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			for (StatisticPackage pack : packages)
				bw.write(pack.toString());
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Создать или открыть существующий файл
	 * @return
	 * @throws IOException
	 */
	private File createFile() throws IOException {
		File file = new File(fileLocation);
		if (!file.exists())
			file.createNewFile();
		return file;
	}
}