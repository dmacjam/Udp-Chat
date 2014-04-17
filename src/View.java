import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


/**
 * GUI-graficke rozhranie View
 * @author Maci ThinkPad
 *
 */
@SuppressWarnings("serial")
public class View extends JFrame{
	private static View INSTANCE;
	
	private JTextPane chatArea;
	private JTextField messageToSend;
	private JButton sendBtn;
	private JTextField myPort;
	private JTextField receiverIp;
	private JTextField receiverPort;
	private JTextField fragmentSize;
	private JButton changeSettingsBtn;
	private JButton changeMyPort;
	private JButton changeFragmentBtn;
	private JScrollPane chatScrollPane;
	//private JScrollPane messageScrollPane;
	
	private StyledDocument doc;
	private Style style;
	
	private View() {		
			//MY SETTINGS
		JPanel myPortPanel=new JPanel(new GridLayout(1,3,20,15));
		myPortPanel.setBorder(BorderFactory.createTitledBorder("My setting"));
		myPort=new JTextField();
		changeMyPort=new JButton("Start port");
		
		myPortPanel.add(new JLabel("My port:"));
		myPortPanel.add(myPort);
		myPortPanel.add(changeMyPort);
		
		
			//SETTINGS PANEL
		JPanel settingsPanel=new JPanel(new GridLayout(3,2,20,15));
		settingsPanel.setBorder(BorderFactory.createTitledBorder("Receiver settings"));

		receiverIp=new JTextField();
		receiverIp.setToolTipText("Receiver IP address, blank for localhost");
		receiverIp.setText("localhost");
		receiverPort=new JTextField();
		
		changeSettingsBtn=new JButton("Apply");
		changeSettingsBtn.setEnabled(false);
		

		settingsPanel.add(new JLabel("IP adress:"));
		settingsPanel.add(receiverIp);
		settingsPanel.add(new JLabel("Port:"));
		settingsPanel.add(receiverPort);
		settingsPanel.add(changeSettingsBtn);
		
		
			//FRAGMENT PANEL
		JPanel fragmentPanel=new JPanel(new GridLayout(1,3,20,15));
		fragmentPanel.setBorder(BorderFactory.createTitledBorder("Fragment settings"));
		
		fragmentSize=new JTextField();
		fragmentSize.setText("MAX");
		changeFragmentBtn=new JButton("Set fragment");
		changeFragmentBtn.setEnabled(false);
		
		fragmentPanel.add(new JLabel("Fragment size"));
		fragmentPanel.add(fragmentSize);
		fragmentPanel.add(changeFragmentBtn);		
		
		
		//NORTH PANEL
		JPanel northPanel=new JPanel(new BorderLayout(5,5));
		northPanel.add(myPortPanel,BorderLayout.NORTH);
		northPanel.add(settingsPanel,BorderLayout.CENTER);
		northPanel.add(fragmentPanel,BorderLayout.SOUTH);
		
			//MESSAGE BORDER
		JPanel nextPanel=new JPanel(new BorderLayout(5,5));
		nextPanel.setBorder(BorderFactory.createTitledBorder("Your message"));
		
		messageToSend=new JTextField(10);			//(4,10);
		//messageToSend.setLineWrap(true);
		//messageScrollPane=new JScrollPane(messageToSend);
		//messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		sendBtn=new JButton("Send");
		sendBtn.setToolTipText("Odosli spravu kliknutim");
		sendBtn.setEnabled(false);
		
			//CHAT SCROLL PANE
		chatArea=new JTextPane();
		chatArea.setEditable(false);
		
		doc=chatArea.getStyledDocument();
		style=chatArea.addStyle("Moj styl", null);
		
		//chatArea.setLineWrap(true);
		chatScrollPane=new JScrollPane(chatArea);
		chatScrollPane.setBorder(BorderFactory.createTitledBorder("Chat"));
		chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		nextPanel.add(messageToSend,BorderLayout.CENTER);
		nextPanel.add(sendBtn,BorderLayout.SOUTH);
		
		
		//CENTER PANEL
		JPanel centerPanel=new JPanel(new BorderLayout(5,5));
		centerPanel.add(nextPanel,BorderLayout.SOUTH);
		centerPanel.add(chatScrollPane,BorderLayout.CENTER);
		
		
		//CONTENT PANE SETTINGS
		this.getContentPane().setLayout(new BorderLayout(5, 5));
		this.getContentPane().add(northPanel,BorderLayout.NORTH);
		this.getContentPane().add(centerPanel,BorderLayout.CENTER);
		this.setSize(550, Toolkit.getDefaultToolkit().getScreenSize().height-50);
		this.setTitle("Udp Chat Messenger");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(0, 0);
		
	}
	
	/**
	 * Singleton.
	 * @return
	 */
	public static View getInstance(){
		if (INSTANCE == null){
			INSTANCE = new View();
		}
	  return INSTANCE;	
	}
	
	/**
	 * Pridaj listener pre changeMyPort button.
	 * @param l
	 */
	public void addChangeMyPortListener(ActionListener l){
		changeMyPort.addActionListener(l);
	}
	
	
	/**
	 * Pridaj listener pre send button.
	 * @param l
	 */
	public void addSendMessageListener(ActionListener l){
		sendBtn.addActionListener(l);
	}
	
	/**
	 * Pridaj listener pre changeSettingsButton.
	 * @param l
	 */
	public void addChangeSettingsListener(ActionListener l){
		changeSettingsBtn.addActionListener(l);
	}
	
	/**
	 * Pridaj listener pre changeFragmentBtn
	 * @param l
	 */
	public void addChangeFragmentListner(ActionListener l){
		changeFragmentBtn.addActionListener(l);
	}
	
	
	/**
	 * Vrati cislo z MyPort textfieldu.
	 * @return
	 */
	public int getMyPort(){
		int port=0;
		try{
			port=Integer.parseInt(myPort.getText());
		}
		catch(NumberFormatException e){
			errorMessage("Wrong port");
		}
	  return port;
	}
	
	
	/**
	 * Vrati zadanu velkost fragmentu.
	 * @return
	 */
	public int getFragmentSize(){
		int fragment=0;
		try{
			fragment=Integer.parseInt(fragmentSize.getText());
			if (fragment < 1){
				errorMessage("Fragment must be greater than zero");
				return 0;
			}
		}
		catch(NumberFormatException e){
			errorMessage("Wrong fragment size");
		}


	 return fragment;
	}
	
	
	
	/**
	 * Vracia ip adresu z receiverIp textfieldu.
	 * @return
	 */
	public String getreceiverIp(){
		return receiverIp.getText();
	}
	
	/**
	 * Vracia hodnotu z receiverPort textfieldu.
	 * @return
	 */
	public int getreceiverPort(){
		int port=0;
		try{
			port=Integer.parseInt(receiverPort.getText());
		}
		catch(NumberFormatException e){
			errorMessage("Wrong receiver port");
		}
	  return port;
	}
	
	/**
	 * Vrati string z messageToSend textfieldu.
	 * @return
	 */
	public String getMessage(){
		String s=messageToSend.getText();
		messageToSend.setText("");
		return s;
	}
	
	/**
	 * Vypise prijatu spravu do chatu.
	 * @param s
	 */
	public void ouputMessage(String s){
		//chatArea.append(s+"\n");
		
		StyleConstants.setForeground(style, new Color(0x009900));
		try{ doc.insertString(doc.getLength(), s+"\n" , style);}
		catch (BadLocationException e){
			e.printStackTrace();
		}
		
		chatArea.setCaretPosition(doc.getLength());
	}
	
	/**
	 * Nastavi receiver ip textfield.
	 * @param s
	 */
	public void setIpReceiver(String s){
		receiverIp.setText(s);
	}
	
	/**
	 * Metoda na zobrazenie mojej spravy v inej farbe.
	 * @param s
	 */
	public void showMyMessage(String s){
		StyleConstants.setForeground(style, Color.black);
		//chatArea.append("JA: "+s+"\n");
		
		try{ doc.insertString(doc.getLength(), "JA:"+s+"\n", style);}
		catch (BadLocationException e){
			e.printStackTrace();
		}
		
		chatArea.setCaretPosition(doc.getLength());
	}
	
	/**
	 * Vypis chyb vo formularoch.
	 * @param s
	 */
	public void errorMessage(String s){
		StyleConstants.setForeground(style, Color.red);
		
		try{ doc.insertString(doc.getLength(), s+"\n", style);}
		catch (BadLocationException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Vypis info.
	 * @param s
	 */
	public void infoMessage(String s){
		StyleConstants.setForeground(style, Color.blue);
		
		try{ doc.insertString(doc.getLength(), s+"\n", style);}
		catch (BadLocationException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Odomkne button na potvrdenie info o receiverovi.
	 */
	public void enableReceiverSettings(){
		changeSettingsBtn.setEnabled(true);
	}
	
	/**
	 * Odomke button na zadanie velkosti fragmentu.
	 */
	public void enableFragmentSettings(){
		changeFragmentBtn.setEnabled(true);
	}
	
	/**
	 * Odomke button na odoslanie spravy.
	 */
	public void enableSend(){
		sendBtn.setEnabled(true);
	}
	
	
	public void addEnterMessageSend(ActionListener l){
		messageToSend.addActionListener(l);
	}

}
