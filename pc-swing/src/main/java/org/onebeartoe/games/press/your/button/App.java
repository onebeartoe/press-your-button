
package org.onebeartoe.games.press.your.button;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.onebeartoe.games.press.your.button.plugins.PluginConfigEntry;
import org.onebeartoe.games.press.your.button.preferences.GamePreferencesKeys;
import org.onebeartoe.games.press.your.button.preferences.JavaPreferencesService;
import org.onebeartoe.games.press.your.button.preferences.PreferencesService;
import org.onebeartoe.games.press.your.button.swing.GamePanel;
import org.onebeartoe.games.press.your.button.tabs.PressYourButton;

public class App extends WindowAdapter //extends IOIOSwingApp extends WindowAdapter
{    
    
    private final Logger logger;
    
    private PreferencesService preferenceService;
    
    private Timer searchTimer;
   
    private List<PluginConfigEntry> userPluginConfiguration;
    
    private JFrame frame;
    
    private JTabbedPane tabbedPane;
    
//    private JFileChooser pluginChooser;
    
    private JLabel statusLabel;
    
    public static final int DEFAULT_HEIGHT = 600;
    
    public static final int DEFAULT_WIDTH = 450;
    
    public App()
    {
	String className = App.class.getName();
	logger = Logger.getLogger(className);
	
//	pluginChooser = new JFileChooser();
//	pluginChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	
	preferenceService = new JavaPreferencesService();

        userPluginConfiguration = new ArrayList();
           
        try 
	{
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} 
	catch (Exception ex) 
	{
	    String message = "An error occured while setting the native look and feel.";
	    logger.log(Level.SEVERE, message, ex);	
	}
        
	// game tab
	String path2 = "/icons/tabs/game.png";
	URL url2 = getClass().getResource(path2);
	ImageIcon animationsTabIcon = new ImageIcon(url2);
	final GamePanel animationsPanel = new PressYourButton();
	
	frame = new JFrame("Press Your Butotn");
	
	JPanel statusPanel = new JPanel();
	statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
	statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
	statusLabel = new JLabel("Game Status: Running");
	statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
	statusPanel.add(statusLabel);
	
	JMenuBar menuBar = createMenuBar();
	
	tabbedPane = new JTabbedPane();
	tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener( new TabChangeListener() );
        tabbedPane.addTab("Pres Your Button", animationsTabIcon, animationsPanel, "Load built-in animations.");
	
	Dimension demension;
	try 
	{
	    demension = preferenceService.restoreWindowDimension();
	} 
	catch (Exception ex) 
	{
	    demension = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	Point location = null;
	try 
	{
	    location = preferenceService.restoreWindowLocation();
	} 
	catch (Exception ex) 
	{
	    logger.log(Level.INFO, ex.getMessage(), ex);
	}    
	
	frame.addWindowListener(this);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	frame.setLayout( new BorderLayout() );
	frame.setJMenuBar(menuBar);
	frame.add(tabbedPane, BorderLayout.CENTER);	
	frame.add(statusPanel, BorderLayout.SOUTH);
	frame.setSize(demension);		
		
	if(location == null)
	{
	    // center it
	    frame.setLocationRelativeTo(null); 		
	}
	else
	{
	    frame.setLocation(location);
	}
                
	frame.setVisible(true);
    }

    protected Window createMainWindow(String args[]) 
    {
	try 
	{
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} 
	catch (Exception ex) 
	{
	    String message = "An error occured while setting the native look and feel.";
	    logger.log(Level.SEVERE, message, ex);	
	}
	
	// user images tab
	String userIconPath = "/tab_icons/my_small.png";
	URL userUrl = getClass().getResource(userIconPath);
	ImageIcon userTabIcon = new ImageIcon(userUrl);	
	String key = GamePreferencesKeys.userImagesDirectory;	

	// animations tab
	String path2 = "/tab_icons/ship_small.png";
	URL url2 = getClass().getResource(path2);
	ImageIcon animationsTabIcon = new ImageIcon(url2);
	final GamePanel animationsPanel = new PressYourButton();
	
	frame = new JFrame("Press Your Button");
	
	JPanel statusPanel = new JPanel();
	statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
	statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
	statusLabel = new JLabel("Game Status: Searching...");
	statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
	statusPanel.add(statusLabel);
	
	JMenuBar menuBar = createMenuBar();
	
	tabbedPane = new JTabbedPane();
	tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.addChangeListener( new TabChangeListener() );
        tabbedPane.addTab("Animations", animationsTabIcon, animationsPanel, "Load built-in animations.");
	
	Dimension demension;
	try 
	{
	    demension = preferenceService.restoreWindowDimension();
	} 
	catch (Exception ex) 
	{
	    demension = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	Point location = null;
	try 
	{
	    location = preferenceService.restoreWindowLocation();
	} 
	catch (Exception ex) 
	{
	    logger.log(Level.INFO, ex.getMessage(), ex);
	}
        
        
	
	frame.addWindowListener(this);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	frame.setLayout( new BorderLayout() );
	frame.setJMenuBar(menuBar);
	frame.add(tabbedPane, BorderLayout.CENTER);	
	frame.add(statusPanel, BorderLayout.SOUTH);
	frame.setSize(demension);		
        frame.setTitle("onebeartoe Press Your Button");
		
	if(location == null)
	{
	    // center it
	    frame.setLocationRelativeTo(null); 		
	}
	else
	{
	    frame.setLocation(location);
	}
                
	frame.setVisible(true);
	
	return frame;
    }
    
    private JMenuBar createMenuBar()	    
    {
	JMenuItem menuItem;
	
	JMenu helpMenu = new JMenu("Help");
	helpMenu.setMnemonic(KeyEvent.VK_A);
	helpMenu.getAccessibleContext().setAccessibleDescription("update with accessible description");
	
	menuItem = new JMenuItem("Instructions");
	KeyStroke instructionsKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK);
	menuItem.setAccelerator(instructionsKeyStroke);
	menuItem.getAccessibleContext().setAccessibleDescription("update with accessible description");
	menuItem.addActionListener( new InstructionsListener() );
	helpMenu.add(menuItem);
	
	menuItem = new JMenuItem("About");
	KeyStroke aboutKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK);
	menuItem.setAccelerator(aboutKeyStroke);
	menuItem.getAccessibleContext().setAccessibleDescription("update with accessible description");
	menuItem.addActionListener( new AboutListener() );
	helpMenu.add(menuItem);
	
	menuItem = new JMenuItem("Exit");
	KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK);
	menuItem.setAccelerator(keyStroke);
	menuItem.addActionListener( new QuitListener() );
	menuItem.getAccessibleContext().setAccessibleDescription("update with accessible description");
	helpMenu.add(menuItem);

	menuItem = new JMenuItem();
	menuItem.setMnemonic(KeyEvent.VK_D);
	helpMenu.add(menuItem);
	
	JMenuItem loadPluginsOption = new JMenuItem("Load");
	JMenuItem clearPluginsOption = new JMenuItem("Clear");
	clearPluginsOption.addActionListener( new ClearPluginsListener() );
	JMenu pluginsMenu = new JMenu("Plugins");
	pluginsMenu.add(loadPluginsOption);
	pluginsMenu.add(clearPluginsOption);
	pluginsMenu.getAccessibleContext().setAccessibleDescription("default plugins menu message");
	
	JMenuBar menuBar = new JMenuBar();
	menuBar.add(helpMenu);
	menuBar.add(pluginsMenu);
	
	return menuBar;
    }
    
    private void displayPlugin(GamePanel panel)
    {
	ImageIcon icon = panel.getTabIcon();
	String title = panel.getTabTitle();
	tabbedPane.addTab(title, icon, panel, "A weather app for internal and external temps.");
	
    }
    
    private void exit()
    {	
	savePreferences();
	
	System.exit(0);
    }
    
    public byte[] extractBytes(BufferedImage image) throws IOException 
    {
	// get DataBufferBytes from Raster
	WritableRaster raster = image.getRaster();
	DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

	return (data.getData());
    }    
    
    public static void main(String[] args) throws Exception 
    {		
	App app = new App();
    }
    
    private void savePreferences()
    {
	preferenceService.saveWindowPreferences(frame);
    }
    
    private void startSearchTimer()
    {
	int delay = 1000;
	SearchTimer worker = new SearchTimer();
	searchTimer = new Timer(delay, worker);
	searchTimer.start();
    }
    
    @Override
    public void windowClosing(WindowEvent event)
    {
        exit();
    }

    private class AboutListener implements ActionListener
    {
	@Override
	public void actionPerformed(ActionEvent e) 
	{
	    String path = "/icons/tabs/";
	    String iconPath = path + "game.png";
	    URL resource = getClass().getResource(iconPath);
	    ImageIcon imageIcon = new ImageIcon(resource);
	    String message = "About Press Your Button";
	    AboutPanel about = new AboutPanel();
	    JOptionPane.showMessageDialog(frame, about, message, JOptionPane.INFORMATION_MESSAGE, imageIcon);
	}
    }
    
    private class ClearPluginsListener implements ActionListener
    {
	@Override
	public void actionPerformed(ActionEvent e) 
	{
            userPluginConfiguration.clear();
	    preferenceService.saveUserPluginPreferences(userPluginConfiguration);
	}
    }
    
    private class InstructionsListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e) 
	{
	    String path = "/images/";
	    String iconPath = path + "aaagumball.png";
	    URL resource = getClass().getResource(iconPath);
	    ImageIcon imageIcon = new ImageIcon(resource);
	    String message = "Game Instructions";
	    InstructionsPanel about = new InstructionsPanel();
	    JOptionPane.showMessageDialog(frame, about, message, JOptionPane.INFORMATION_MESSAGE, imageIcon);
	}
    }
    
    private class QuitListener implements ActionListener
    {
	@Override
	public void actionPerformed(ActionEvent e) 
	{
            exit();
	}
    }
    
    private class SearchTimer implements ActionListener 
    {
	final long searchPeriodLength = 45 * 1000;
	
	final long periodStart;
	
	final long periodEnd;
	
	private int dotCount = 0;
	
	String message = "Searching";
	
	StringBuilder label = new StringBuilder(message);
	
	public SearchTimer()
	{
	    label.insert(0, "<html><body><h2>");
	    
	    Date d = new Date();
	    periodStart = d.getTime();
	    periodEnd = periodStart + searchPeriodLength;
	}
	
	public void actionPerformed(ActionEvent e) 
	{	    	    	    
	    if(dotCount > 10)
	    {
		label = new StringBuilder(message);
		label.insert(0, "<html><body><h2>");
		
		dotCount = 0;
	    }
	    else
	    {
		label.append('.');
	    }
	    dotCount++;

	    App.this.statusLabel.setText( label.toString() );
	    
	    Date d = new Date();
	    long now = d.getTime();
	    if(now > periodEnd)
	    {
		searchTimer.stop();
		
	    }
	}
    }

    private class TabChangeListener implements ChangeListener
    {
	public void stateChanged(ChangeEvent e) 
	{


	    // start the selected panel/tab's activity
	    Object o = e.getSource();
	    JTabbedPane tabs = (JTabbedPane) o;
	    Component c = tabs.getSelectedComponent();		
	    GamePanel p = (GamePanel) c;
	    p.startTabActivity();
	}
    }
    
}
