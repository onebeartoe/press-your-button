
package org.onebeartoe.games.press.your.button;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author rmarquez
 */

// rename this to AboutPanel
public class AboutPanel extends JPanel
{
    
    private final String developmentUrl = "http://electronics.onebeartoe.org/";
    private final String projectUrl = "http://onebeartoe.net/";
    
    private JLabel developmentLabel;
    
    public AboutPanel()
    {
	String version = "0.5.1";
	String text = "Press Your Button PC Beta Version " + version;
	String html = "<html><body><h2>" + text + "</h2></body></html>";
	JLabel productLabel = new JLabel(html, JLabel.CENTER);
	
	String developmentHtml = "<html><body><h3>" + "Sofware Development<br/><br/>" + developmentUrl + "</h3></body></html>";
	developmentLabel = new JLabel(developmentHtml);
	developmentLabel.addMouseListener(new DevelopmentLabelListener() );
	
	String projectHtml = "<html><body><h3>Game Home Page<br/><br/>" + projectUrl + "</h3></body></html>";
	JLabel projectLabel = new JLabel(projectHtml);
	projectLabel.addMouseListener(new DevelopmentLabelListener() );
	
	GridLayout layout = new GridLayout(3, 1, 6, 6);
	setLayout(layout);
	
	add(productLabel);
	add(developmentLabel);
	add(projectLabel);
    }
    
    private class DevelopmentLabelListener extends MouseAdapter
    {
	@Override
	public void mouseClicked(MouseEvent e) 
	{
	    Desktop desktop = Desktop.getDesktop();
	    URI uri;
	    try 
	    {
		uri = new URI(developmentUrl);
		desktop.browse(uri);
	    } 
	    catch (Exception ex) 
	    {
		Logger.getLogger(AboutPanel.class.getName()).log(Level.SEVERE, null, ex);
		String message = "Please visit " + developmentUrl + "for more information";
		JOptionPane.showMessageDialog(AboutPanel.this, message);
	    }    
        }
	
	  
	
	@Override
	public void mouseEntered(MouseEvent e) 
	{
	    developmentLabel.setForeground(Color.BLUE);	    
	}
	
	@Override
	public void mouseExited(MouseEvent e) 
	{
	    developmentLabel.setForeground(Color.BLACK);
	}
    }
    
}
