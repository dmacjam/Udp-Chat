import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;


public class ReceiveThread extends Thread{						//implements Runnable{
	private DatagramSocket socket;
	private boolean alive=true;
	private static final Logger LOG = Logger.getLogger(ReceiveThread.class.getName());
	private StringBuilder message;
	byte[] buffer;
	int pocetFragmentov;
	private int port;
	
	
	public ReceiveThread(DatagramSocket socket) {
		this.socket=socket;
		this.port=socket.getLocalPort();
		this.message=new StringBuilder();
		this.buffer=new byte[65508];
	}
	
	/**
	 * Metoda ktorou nastavime zvonku alive na false a tak killneme toto vlakno.
	 */
	public void kill(){
		alive=false;
	}

	/**
	 * Funkcia na prijimanie sprav,ktora sa stale toci v slucke. Kedze receive je blokujuci tak je
	 * nastaveny casovy limit kedy vyhodi exception a ak sa medzitym zmenil moj port tak alive bude
	 * mat hodnotu false a teda vlakno sa ukonci.
	 */
	public void prijimaj(){	
		try {
			socket.setSoTimeout(20000); //2 min=120 000
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		

		while (alive) {
			DatagramPacket packet=new DatagramPacket(buffer, buffer.length);
			//System.out.println("Stojim na receive");
			try {
				socket.receive(packet);
				joinMessages(packet);
			} catch(SocketTimeoutException e){
				LOG.info("Vyprsal socket limit-port: "+port);			
			} catch (SocketException e) {
				LOG.info("Socket Exception-vyziadany close-port: "+port);
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Metoda ktora spaja spravy do Stringbuildera a na konci ich vypise.
	 * @param packet
	 */
	public void joinMessages(DatagramPacket packet){
		//View.getInstance().ouputMessage(packet.getSocketAddress()+"["+packet.getLength()+"]: "+" "+new String(buffer).substring(0, packet.getLength()));
		
		String fragmentMessage=new String(buffer).substring(0,packet.getLength());
		
		if (message.length() == 0){
			message.append(packet.getSocketAddress()+": ");
		}
		
		message.append(fragmentMessage.substring(1,packet.getLength()));
		pocetFragmentov++;
		
		if (fragmentMessage.charAt(0) == '1'){
			View.getInstance().ouputMessage(message.toString()+" |"+pocetFragmentov+"|");
			message=new StringBuilder();
			pocetFragmentov=0;
		}
		
	}
	
	
	/**
	 * Metoda run.
	 */
	@Override
	public void run() {
		prijimaj();	
		LOG.info("Vlakno na porte "+port+" zomiera...");
	}
	
	
	
}
