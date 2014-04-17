import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Class model reprezentujuci logiku odosielania.
 * @author Maci ThinkPad
 *
 */
public class Model {
	InetSocketAddress receiverAddress;						//receiverInetSocketAdress
	DatagramSocket dSocket;
	ReceiveThread recThread=null;
	int fragmentSize=65508;
	
	
	//TEST VALUES
	final static String DEF_RECEIVER_IP="192.168.2.254";
	final static int DEF_RECEIVER_PORT=7777;
	final static int DEF_SENDER_PORT=5555;
	
	/**
	 *Konstruktor. 
	 */
	public Model() {
//		createSocketOnPort(DEF_SENDER_PORT);
//		//setReceiverValues(DEF_RECEIVER_IP, DEF_RECEIVER_PORT);
//		setReceiverLocalhost(DEF_RECEIVER_PORT);
//		sendMessage("Ahoj ako sa mas?");	
	}
	
	/**
	 * Vytvorim si adresu receivera.
	 * @param ipString
	 * @param port
	 */
	public void setReceiverValues(String ipString,int port){
		InetAddress recAddress = null;
		
		if (ipString.equals("localhost")){
			try {
				recAddress=InetAddress.getLocalHost();
				receiverAddress=new InetSocketAddress(recAddress, port);
				View.getInstance().infoMessage("Receiver IP: "+ipString+" ,port: "+receiverAddress.getPort());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else{
			try {
				recAddress=InetAddress.getByName(ipString);
				receiverAddress=new InetSocketAddress(recAddress, port);
				View.getInstance().infoMessage("Receiver IP "+ipString+" on port "+receiverAddress.getPort());
			} catch (UnknownHostException e) {
				View.getInstance().errorMessage("Unknown Host IP");
			}
		}	

	}
	
	
	/**
	 * Vytvorim socket na porte.
	 * @param port
	 */
	public void createSocketOnPort(int port){
		//uzavri socket
		if ( dSocket != null ){
			dSocket.close();
		}
		
		
		try {
			dSocket=new DatagramSocket(port);
		} catch (SocketException e) {
			View.getInstance().errorMessage("Error socket exception");
			e.printStackTrace();
		}
	  
	   View.getInstance().infoMessage("Local port="+dSocket.getLocalPort());
	   
	   //kill vlakna
	   if(recThread != null){
		   recThread.kill();
	   }
	   
	   recThread=new ReceiveThread(dSocket);
	   recThread.start();
	   
	}
	
	/**
	 * Nastavi hodnotu fragmentu.
	 * @param size
	 */
	public void setFragmentSize(int size){
		this.fragmentSize=size;
		View.getInstance().infoMessage("Fragmet size: "+fragmentSize);
	}
	
	
	/**
	 * Rozbitie spravy.
	 * @param message
	 */
	public void sendMessage(String message){
		
			//int realneVelkost = fragmentSize - 1;
			int endPostion = 0;
			
			for (int i = 0; i < message.length(); i = i + fragmentSize) {
				StringBuilder sBuilder = new StringBuilder();
				endPostion = i + fragmentSize;

				if (endPostion < message.length()) {
					sBuilder.append("0" + message.substring(i, endPostion));
				} else {
					sBuilder.append("1"
							+ message.substring(i, message.length()));
				}

			  sendData(sBuilder.toString());
			}
		
	}
	
	/**
	 * Odoslanie spravy cez socket.
	 * @param dPacket
	 * @param buffer
	 */
	private void sendData(String message){
		DatagramPacket dPacket = null;
		byte[] buffer=new byte[fragmentSize];				//MAXIMUM je 65508~64kB
		
		buffer=message.getBytes();
		
		//System.out.println("Fragment spravy je: " + new String(buffer));
		
		try {
			dPacket=new DatagramPacket(buffer,buffer.length,receiverAddress);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			dSocket.send(dPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
