import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class users implements Runnable {
	DataInputStream inS;
	DataOutputStream outS;
	public void users(DataInputStream in, DataOutputStream out) {
		inS = in;
		outS = out;
	}
	public void run() {
		while(true) {
			try {
				String recieved = inS.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
