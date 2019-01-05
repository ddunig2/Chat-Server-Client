import java.net.*;
//import java.nio.ByteBuffer;
//import java.nio.channels.SocketChannel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

@SuppressWarnings("serial")
public class Client extends JFrame {
	//private JSlider mySlider;
	private JTextField typeBox;
	private JTextArea Display;
	private JScrollPane sp;
	//private ServerSocket ser;
	private Socket s;
	private DataInputStream inStream;
	private DataOutputStream outStream;
	String name;
	//SocketChannel socketch;

	public static void main(String arg[]) throws IOException {
		Client Application = new Client();
		Application.setSize(400, 450);
		Application.setTitle("Client Chat Box");
		Application.createGUI();
		Application.setVisible(true);
		Application.setConnection();
	}

	public void setConnection() {
		useIOC();
	}
	
	public void useIOC() {
		name = JOptionPane.showInputDialog("Enter your name");
		try {
			s = new Socket("localhost", 8888);
			Display.setText("You are now conneced");
			send("hi from the clouds");
			typeBox.setEditable(true);
			inStream = new DataInputStream(s.getInputStream());
			outStream = new DataOutputStream(s.getOutputStream());
			//outStream.flush();
			while (true) {
				String recieved = inStream.readUTF();
				if (recieved.equals("END")) {
					typeBox.setEditable(false);
					Display.append("\nServer has chosen to end the chat\nChat disconected.");
					try {
						inStream.close();
						outStream.close();
						s.close();
					} catch (Exception e) {
					}
				} else {
					Display.append("\nServer - " + recieved);
				}
			}
			
		} catch (Exception e) {
		}
	}
    //Not working
	public void useNIOC() {

		try {
			//InetAddress hostIP = InetAddress.getLocalHost();
//			InetSocketAddress myAddress = new InetSocketAddress("localhost", 7878);
//			socketch = SocketChannel.open(myAddress);
//			String msg = "hello";
//			ByteBuffer myBuffer=ByteBuffer.allocate(1024);
//			myBuffer.put(msg.getBytes());
//
//			socketch.write(myBuffer);
		} catch (Exception e) {
		}
	}
	
	public void send(String text) {
		try {
			outStream.writeUTF(name + " - " + text);
			outStream.flush();
		} catch (Exception e) {
		}
	}

	private void createGUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container myPane = getContentPane();
		myPane.setLayout(new FlowLayout());

		Display = new JTextArea(20, 25);
		Display.setEditable(false);
		Display.setLineWrap(true);
		Display.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "ChatBox"));
		sp = new JScrollPane(Display);

		typeBox = new JTextField(25);
		typeBox.setHorizontalAlignment(JTextField.LEFT);
		typeBox.setEditable(false);
		typeBox.addActionListener(new typeBoxAction());
		myPane.add(sp);
		// myPane.add(Display);
		myPane.add(typeBox);
	}

	private class typeBoxAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (!typeBox.isEditable()) {
				
			} else {
				send(event.getActionCommand());
				if (event.getActionCommand().equals("END")) {
					// send(event.getActionCommand());
					typeBox.setEditable(false);
					try {
						inStream.close();
						outStream.close();
						s.close();
					} catch (Exception e) {
					}
					// System.exit(-1);
				} else {
					Display.append("\nMe - " + event.getActionCommand());
					// send(event.getActionCommand());
				}
			}
			typeBox.setText("");
		}
	}

}