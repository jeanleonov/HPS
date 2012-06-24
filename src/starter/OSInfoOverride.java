package starter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class OSInfoOverride {
	final PrintStream errOS;
	final PrintStream outOS;
	
	final static String[] excStrings = {
		"\nINFO:",
		"Retrieving CommandDispatcher for platform null"
	};
	
	public OSInfoOverride() {
		errOS = System.err;
		outOS = System.out;
		
		PrintStream dummy = new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				errOS.write(b);
			}
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				String str = new String(b);
				for(String cur : excStrings) {
					if(str.contains(cur)) return;
				}
				super.write(b, off, len);
			}
		}); 
		
		System.setErr(dummy);
		System.setOut(dummy);
	}
	
	public void dispose() {
		System.setErr(errOS);
		System.setOut(outOS);
	}
}
