import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Controller {
	private Model model;
	private View view;
	
	public Controller(View view,Model model) {
		this.view=view;
		this.model=model;

		this.view.addChangeSettingsListener(new EditSettingsListener());

		this.view.addChangeMyPortListener(new ChangeMyPortListener());
		this.view.addChangeFragmentListner(new ChangeFragmentListener());

	}
	
	/**
	 * ActionListener class na settings button.
	 * @author Maci ThinkPad
	 *
	 */
	public class EditSettingsListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int port=view.getreceiverPort();
			String ip=view.getreceiverIp();
			if ( ip.isEmpty() ) {
				ip=new String("localhost");
				view.setIpReceiver(ip);
			}
			if (port != 0) {
				model.setReceiverValues(ip , port);
				view.enableFragmentSettings();
				view.enableSend();
				ActionListener l=new SendMessageListener();
				view.addSendMessageListener(l);
				view.addEnterMessageSend(l);
			}
		}
		
	}
	
	/**
	 * ActionListener class na send button.
	 * @author Maci ThinkPad
	 *
	 */
	public class SendMessageListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String message=view.getMessage();
			model.sendMessage(message);
			view.showMyMessage(message);
		}
		
	}
	
	/**
	 * ActionListener class na my settings change button.
	 * @author Maci ThinkPad
	 *
	 */
	public class ChangeMyPortListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int port=view.getMyPort();
			if ( port != 0 ){
				model.createSocketOnPort(port);
				view.enableReceiverSettings();
			}
		}
		
	}
	
	/**
	 * ActionListener class na zmenu velkosti fragmentu.
	 * @author Maci ThinkPad
	 *
	 */
	public class ChangeFragmentListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int fragment=view.getFragmentSize();
			if ( fragment != 0 ){
				model.setFragmentSize(fragment);
			}
		}
		
	}
	


}
