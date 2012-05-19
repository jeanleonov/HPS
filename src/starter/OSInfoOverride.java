package starter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class OSInfoOverride {
	final PrintStream errOS;
	
	public OSInfoOverride() {
		errOS = System.err;
		
		System.setErr(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				errOS.write(b);
			}
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				if((new String(b)).contains("\nINFO:")) return;
				super.write(b, off, len);
			}
		}));
	}
	
	public void dispose() {
		System.setErr(errOS);
	}
}
