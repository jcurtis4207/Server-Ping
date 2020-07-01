import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class ServerPing {
	// setup frame
	public static void main(String[] args) {
		JFrame frame = new JFrame("IP PING");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new PingPanel());
		frame.pack();
		frame.setVisible(true);
	}

	// setup panel
	private static class PingPanel extends JPanel{
		private JLabel[] ipLabels;
		private JLabel[] statusLabels;
		private String[] ipStrings = {"192.168.1.50", "192.168.1.55", "192.168.1.66", "192.168.1.77", "192.168.1.88"};
		private boolean[] test;
		private JButton refreshButton;
		private JPanel leftPanel, rightPanel;
		
		public PingPanel() {
			// create sub panels
			leftPanel = new JPanel();
			leftPanel.setBackground(Color.BLACK);
			leftPanel.setPreferredSize(new Dimension(90, 110));
			rightPanel = new JPanel();
			rightPanel.setBackground(Color.BLACK);
			rightPanel.setPreferredSize(new Dimension(30, 110));
			
			// create arrays and fill left panel
			ipLabels = new JLabel[ipStrings.length];
			statusLabels = new JLabel[ipStrings.length];
			for(int i = 0; i < ipStrings.length; i++) {
				ipLabels[i] = new JLabel(ipStrings[i]);
				ipLabels[i].setForeground(Color.WHITE);
				leftPanel.add(ipLabels[i]);
			}
			test = new boolean[ipStrings.length];
			refreshButton = new JButton("Refresh");
			refreshButton.addActionListener(new ButtonListener());
						
			// fill right panel
			for(int i = 0; i < ipStrings.length; i++) {
				statusLabels[i] = new JLabel("OFF");
				statusLabels[i].setForeground(Color.RED);
				rightPanel.add(statusLabels[i]);
			}
			
			// fill main panel
			add(leftPanel);
			add(rightPanel);
			add(refreshButton);
			setBackground(Color.black);
			setPreferredSize(new Dimension(200,150));
			
			// ping ip's and update online entries
			new Thread(new Runnable() {
		    		public void run() {
		    			try {
		    				for(int i = 0; i < ipStrings.length; i++) {
		    					test[i] = sendPingRequest(ipStrings[i]);
		    					if(test[i]) {
		    						statusLabels[i].setText("ON");
		    						statusLabels[i].setForeground(Color.GREEN);
		    					}
		    				}
		    				revalidate();
		    			} catch (Exception e) {
		    			}
		    		}
	    		}).start();
		}
		
		// when refresh button pressed - ping and update page
		private class ButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent event) {
				for(int i = 0; i < ipStrings.length; i++) {
					test[i] = sendPingRequest(ipStrings[i]);
					if(test[i]) {
						statusLabels[i].setText("ON");
						statusLabels[i].setForeground(Color.GREEN);
					}
					else {
						statusLabels[i].setText("OFF");
						statusLabels[i].setForeground(Color.RED);
					}
					rightPanel.add(statusLabels[i]);
				}
				revalidate();
			}
		}
	}
	
	// ping ip address and return boolean
	public static boolean sendPingRequest(String ipAddress){
		try {
			InetAddress addr = InetAddress.getByName(ipAddress);
			try{
				if(addr.isReachable(1000))
					return true;
				else
					return false;
			}catch(IOException e) {
				return false;
			}
		} catch (UnknownHostException e) {
			return false;
		}
	}
}