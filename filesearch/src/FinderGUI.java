import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;


public class FinderGUI extends JFrame {
	
	private JPanel panel = new JPanel();
	private JPanel panelBottom = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JLabel chooseDirLabel = new JLabel("---");
	private JTextField wordToFindField = new JTextField("example");
	private JTextField extensionField = new JTextField(".log");
	
	private String directory = "";
	private String textToFind = "";
	
	private ArrayList<JTextPane> textPanes = new ArrayList<>();
	private ArrayList<JScrollPane> scrollPanes = new ArrayList<>();
	private int scrollPaneCounter = 0;
	private ArrayList<JTree> jTrees = new ArrayList<>();
	private ArrayList<JScrollPane> jScrollPanes = new ArrayList<>();
	private int treesAndPanesCounter = 0;
	
	private TreeSelectionListener createTreeSelectionListener() {
		
		return treeSelectionEvent -> {
			TreePath path = treeSelectionEvent.getPath();
			String s = path.toString().substring(1,path.toString()
					.length()-1).replaceAll(", ", "\\\\");
			File file = new File(s);
			if (file.isFile()){
				addTab(s);
			}
		};
		
	}
	
	private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
		
		for(int i=startingIndex;i<rowCount;++i){
			tree.expandRow(i);
		}
		if(tree.getRowCount()!=rowCount){
			expandAllNodes(tree, rowCount, tree.getRowCount());
		}
		
	}
	
	private void addTab(String path){
		
		textPanes.add(new JTextPane());
		scrollPanes.add(new JScrollPane(textPanes.get(scrollPaneCounter), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		scrollPanes.get(scrollPaneCounter).setLocation(400,10);
		
		try {
			ArrayList<ArrayList<Integer>> indList = SearchMethods.finder(path, wordToFindField.getText());

			SearchMethods.selectText(new File(path), indList, wordToFindField.getText(),
					textPanes.get(scrollPaneCounter));
			
			tabbedPane.addTab(path.split("\\\\")[path.split("\\\\").length-1],
					scrollPanes.get(scrollPaneCounter));
			tabbedPane.setLocation(panelBottom.getWidth()*13/50, panelBottom.getHeight()/50);
			tabbedPane.setSize(panelBottom.getWidth()*12/25,panelBottom.getHeight()*8/10);
			textPanes.get(scrollPaneCounter).setCaretPosition(0);
			textPanes.get(scrollPaneCounter).getCaret().setVisible(true);
			scrollPaneCounter += 1;
			panelBottom.add(tabbedPane);
			setContentPane(panel);
		}
		catch(Exception ee) {
			ee.printStackTrace();
			String message = "Something went wrong...";
			JOptionPane.showMessageDialog(null,
					message,
					"Output",
					JOptionPane.PLAIN_MESSAGE);
		}
		
	}
	
	public FinderGUI() {
		super("Finder");
		
		final JPanel panelTop = new JPanel();
		final JButton chooseDirButton = new JButton("Choose Directory");
		final JLabel wordToFindLabel = new JLabel("Find What:");
		final JLabel extensionLabel = new JLabel("Extension:");
		final JButton searchButton = new JButton("Search");
		final JButton upButton = new JButton("Up");
		final JButton downButton = new JButton("Down");
		final JButton delTabButton = new JButton("Delete Tab");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // get 2/3 of the height, and 2/3 of the width
		int height = screenSize.height * 2 / 3;
		int width = screenSize.width * 2 / 3;
		this.setSize(width, height);
		
		// Windows design
		try {
			UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		panel.setLayout(null);
		panel.setSize(width,height);
		panel.add(panelTop);
		panel.add(panelBottom);
		
		panelTop.setLayout(null);
		panelTop.setSize(width, height/5);
		panelTop.add(chooseDirButton);
		panelTop.add(chooseDirLabel);
		panelTop.add(wordToFindField);
		panelTop.add(wordToFindLabel);
		panelTop.add(extensionField);
		panelTop.add(extensionLabel);
		panelTop.add(searchButton);
		
		panelBottom.setLayout(null);
		panelBottom.setLocation(0, height/5);
		panelBottom.setSize(width, height*4/5);
		panelBottom.add(delTabButton);
		panelBottom.add(downButton);
		panelBottom.add(upButton);
		
		chooseDirButton.setLocation(10,100);
		chooseDirButton.setSize(50,50);
		chooseDirButton.setLocation(width/50,height/25);
		chooseDirButton.setSize(width*8/50,height/25);
		chooseDirButton.addActionListener(new ButtonChooserEventListener());
		
		chooseDirLabel.setLocation(width/50, height*3/25);
		chooseDirLabel.setSize(width*8/50,height/25);
		chooseDirLabel.setVerticalAlignment(JLabel.CENTER);
		chooseDirLabel.setHorizontalAlignment(JLabel.CENTER);
		
		searchButton.setSize(width*8/50, height*8/50);
		searchButton.setLocation(width*41/50, height/50);
		searchButton.addActionListener(new ButtonSearchEventListener());
		
		wordToFindLabel.setLocation(width*43/200, height/25);
		wordToFindLabel.setSize(width*24/200, height/25);
		
		extensionLabel.setSize(width*24/200, height/25);
		extensionLabel.setLocation(width*43/200,height*3/25);
		
		wordToFindField.setLocation(width*64/200,height/25);
		wordToFindField.setSize(width*72/200,height/25);
		wordToFindField.setHorizontalAlignment(JTextField.CENTER);
		
		extensionField.setHorizontalAlignment(JTextField.CENTER);
		extensionField.setSize(width*72/200,height/25);
		extensionField.setLocation(width*64/200,height*3/25);
		
		delTabButton.setLocation(width*17/20, panelBottom.getHeight()/9);
		delTabButton.setSize(width/10, height*4/45);
		delTabButton.addActionListener((ActionEvent e) -> {
			
			int selected = tabbedPane.getSelectedIndex();
			if (selected >= 0) {
				textPanes.remove(selected);
				scrollPanes.remove(selected);
				scrollPaneCounter -= 1;
				tabbedPane.removeTabAt(selected);
			}
			
		});
		
		upButton.setSize(width/10, panelBottom.getHeight()*2/24);
		upButton.setLocation(width*17/20, panelBottom.getHeight()*22/48);
		upButton.addActionListener((ActionEvent e) -> {
			
			int select = tabbedPane.getSelectedIndex();
			StyledDocument doc = textPanes.get(select).getStyledDocument();
			int i = textPanes.get(select).getCaretPosition();
			int it = 0;
			while (i >= 0){
				try{
					if (doc.getText(i, textToFind.length()).equals(textToFind)){
						textPanes.get(select).setCaretPosition(i);
						if (it != 0){break;}
					}}
				catch (Exception ee){ee.printStackTrace();}
				it += 1;
				i-=1;
			}
			
		});
		
		downButton.setSize(width/10,panelBottom.getHeight()*2/24);
		downButton.setLocation(width*17/20, panelBottom.getHeight()*34/48);
		downButton.addActionListener((ActionEvent e) -> {
			
				int select = tabbedPane.getSelectedIndex();
				StyledDocument doc = textPanes.get(select).getStyledDocument();
				int i = textPanes.get(select).getCaretPosition();
				int it = 0;
				while (i < doc.getLength()-textToFind.length()){
					try{
						if (doc.getText(i, textToFind.length()).equals(textToFind)){
							textPanes.get(select).setCaretPosition(i);
							if (it != 0){break;}
						}}
					catch (Exception ee){ee.printStackTrace();}
					it += 1;
					i+=1;
				}
				
		});
		
		setContentPane(panel);
		
	}
	
	class ButtonChooserEventListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int ret = jFileChooser.showDialog(null, "Choose folder");
			if (ret == JFileChooser.APPROVE_OPTION) {
				chooseDirLabel.setText(jFileChooser.getSelectedFile().toString());
				directory = jFileChooser.getSelectedFile().toString();
			}
		}
		
	}
	
	class ButtonSearchEventListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			final String extension;
			
			if (treesAndPanesCounter != 0) {
				panelBottom.remove(jScrollPanes.get(treesAndPanesCounter-1));
			}
			
			if (wordToFindField.getText().length() != 0) {
					textToFind = wordToFindField.getText();
			}
			else{
				JOptionPane.showMessageDialog(null,
						"Enter What Do You Want To Search",
						"Output",
						JOptionPane.PLAIN_MESSAGE);
				return;
			}
			
			if (extensionField.getText().length() > 1 && extensionField.getText().startsWith(".")) {
				extension = extensionField.getText();
			}
			else if(extensionField.getText().length() > 0 && !extensionField.getText().startsWith(".")){
				extension = "." + extensionField.getText();
			}
			else{
				JOptionPane.showMessageDialog(null,
						"Enter Extension Name",
						"Output",
						JOptionPane.PLAIN_MESSAGE);
				return;
			}
			
			File file = new File(directory);
			if (!file.isDirectory()){
				JOptionPane.showMessageDialog(null,
						"Choose Directory",
						"Output",
						JOptionPane.PLAIN_MESSAGE);
				return;
			}
			
			ArrayList<String> files = new ArrayList<>();
			files =  SearchMethods.searchGoodFiles(directory, files, textToFind, extension);

			if (files.size() == 0){
				String message = "No Files Found";
				JOptionPane.showMessageDialog(null,
							message,
							"Output",
							JOptionPane.PLAIN_MESSAGE);
				return;
				}
			
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(directory);
			CustomTree tree = new CustomTree(new CustomNode(directory, directory));
			
			jTrees.add(new JTree(root));
			
			int size = 0;
				
			for (String i : files){
				tree.addElement(i.substring(directory.length()+1, i.length()));
				size += i.replaceAll("\\\\", "/").split("/").length;
			}
				
			int counter;
			counter = -1;
			DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[size+1];
				
			tree.createTree(root, counter, nodes);
			jTrees.get(treesAndPanesCounter).addTreeSelectionListener(createTreeSelectionListener());
			expandAllNodes(jTrees.get(treesAndPanesCounter),0,
					jTrees.get(treesAndPanesCounter).getRowCount());
			jScrollPanes.add(new JScrollPane(jTrees.get(treesAndPanesCounter)));
			jScrollPanes.get(treesAndPanesCounter)
					.setLocation(panelBottom.getWidth()/50,panelBottom.getWidth()/50);
			jScrollPanes.get(treesAndPanesCounter)
					.setSize(panelBottom.getWidth()/5,panelBottom.getHeight()*8/10);
			panelBottom.add(jScrollPanes.get(treesAndPanesCounter));
			setContentPane(panelBottom);
			panel.add(panelBottom);
			setContentPane(panel);
			treesAndPanesCounter += 1;
			
		}
	}
	
}


