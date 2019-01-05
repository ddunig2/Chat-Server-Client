import java.net.*;
import java.nio.channels.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

@SuppressWarnings("serial")
public class Server extends JFrame {
    private JTextField typeBox;
    private JTextArea Display;
    private JScrollPane sp;
    private ServerSocket ser;
    private Socket s;
    private DataInputStream inStream;
    private DataOutputStream outStream;
    boolean ok = true;
    Selector selector;
    ServerSocketChannel ssc;
    InetSocketAddress hostAdress;
    SelectionKey selectKy;
    
    users user[] = new users[8];
    public static void main(String arg[]) throws IOException{
        Server Application = new Server();
        Application.setSize(400,450);
        Application.setTitle("Server Chat Box");
        Application.createGUI();
        Application.setVisible(true);
        Application.setConnection();
    }

    public void setConnection(){
    	useIOC();
    }
    
    public void useIOC() {
    	try {
            Display.setText("waiting for connection....");
            ser = new ServerSocket(8888, 100);
            s = ser.accept();
            Display.setText("You are now connected to client");
            typeBox.setEditable(true);
            inStream = new DataInputStream(s.getInputStream());
            outStream = new DataOutputStream(s.getOutputStream());
            outStream.flush();
            while(true){
                String recieved = inStream.readUTF();
                if(recieved.equals("END"))
                {
                    typeBox.setEditable(false);
                    Display.append
                    ("\nClient has chosen to end the chat\nChat disconected.");
                    try{
                        inStream.close();
                        outStream.close();
                        s.close();
                    }
                    catch(Exception e){
                    }
                }
                else{
                    Display.append("\n" + recieved);
                }
            }
        }catch (Exception e) {

        }
            
    }
    
    //Doesnt not work properly
    public void useNIOC() {
//        try {
//            Display.setText("waiting for connection...."); 
//            selector = Selector.open();
//            hostAdress = new InetSocketAddress("localhost",7878);
//            ssc = ServerSocketChannel.open();
//            ssc.bind(hostAdress);
//            ssc.configureBlocking(false);
//            int ops  = ssc.validOps();
//            selectKy = ssc.register(selector, ops,null);
//            while(true) {
//            	int noOfKeys = selector.select();
//            	Set selectedKeys = selector.selectedKeys();
//            	Iterator iter = selectedKeys.iterator();
//            	while (iter.hasNext()) {
//            		SelectionKey ky = (SelectionKey) iter.next();
//            		if(ky.isAcceptable()) {
//            			String msg = "welcome";
//            			ByteBuffer message = ByteBuffer.wrap(msg.getBytes());
//            			SocketChannel client = ssc.accept();
//            			client.configureBlocking(false);
//            			client.register(selector, SelectionKey.OP_READ);
//            			client.write(message);
//            		}
//            		else if(ky.isReadable()) {
//            			SocketChannel client = (SocketChannel) ky.channel();
//            			ByteBuffer buffer = ByteBuffer.allocate(256);
//            			client.read(buffer);
//            			ByteArrayInputStream bis = null;
//            	        DataInputStream ois = null;
//            	        String recieved;
//            	        try {
//            	            bis = new ByteArrayInputStream(buffer.array());
//            	            ois = new DataInputStream(bis);
//            	            recieved = ois.readUTF();
//            	            takeCare(recieved);
//            	        } finally {
//            	            if (bis != null) {
//            	                bis.close();
//            	            }
//            	            if (ois != null) {
//            	                ois.close();
//            	            }
//            	        }
//            	        Set selectedKeysw = selector.selectedKeys();
//                    	Iterator iterw = selectedKeysw.iterator();
//                    	while (iterw.hasNext()) {
//                    		SelectionKey kyw = (SelectionKey) iterw.next();
//                    		if(kyw.isWritable()) {
//                    			SocketChannel clientw = (SocketChannel) kyw.channel();
//                    			client.write(buffer);
//                    		}
//                    	}
//            		}
//            	}
//            	iter.remove();
//            }
//        }catch (Exception e) {
//
//        }
    }
    
    public void takeCare(String recieved) {
		if (recieved.equals("END")) {
			typeBox.setEditable(false);
			Display.append("\nClient has chosen to end the chat\nChat disconected.");
		}
		// try{
		// inStream.close();
		// outStream.close();
		// s.close();
		// }
		// catch(Exception e){
		// }
		// }
		else {
			Display.append("\nClient - " + recieved);
		}
	}

    public void send(String text){
        try{
            outStream.writeUTF(text);
            outStream.flush();
        }
        catch(Exception e) {
        }
    }

    private void createGUI()
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container myPane = getContentPane();
        myPane.setLayout(new FlowLayout());

        Display = new JTextArea(20,25);
        Display.setEditable(false);
        Display.setLineWrap(true);
        Display.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "ChatBox"));
        sp = new JScrollPane(Display);

        typeBox = new JTextField(25);
        typeBox.setHorizontalAlignment(JTextField.LEFT);
        typeBox.setEditable(false);
        typeBox.addActionListener(new typeBoxAction());
        myPane.add(sp);
        //myPane.add(Display);
        myPane.add(typeBox);
    }

    private class typeBoxAction implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if(!typeBox.isEditable())
            {

            }
            else 
            {
                send(event.getActionCommand());
                if(event.getActionCommand().equals("END")){
                    typeBox.setEditable(false);
                    try{
                        inStream.close();
                        outStream.close();
                        s.close();
                    }
                    catch(Exception e){
                    }
                    //System.exit(-1);
                }
                else 
                {
                    Display.append("\nMe - " + event.getActionCommand());
                }
            }
            typeBox.setText("");
        }
    }

}
