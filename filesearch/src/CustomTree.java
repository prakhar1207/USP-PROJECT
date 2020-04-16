import javax.swing.tree.DefaultMutableTreeNode;

public class CustomTree {
	
	CustomNode root;
	CustomNode commonRoot;
	
	public CustomTree(CustomNode root ) {
		
		this.root = root;
		commonRoot = null;
		
	}
	
	public void addElement( String elementValue ) {
		
		elementValue = elementValue.replaceAll("\\\\", "/");
		String[] list = elementValue.split("/");
		// latest element of the list is the filename.extension
		root.addElement(root.incrementalPath, list);
		
	}
	
	public void createTree(DefaultMutableTreeNode node, int globalCounter, DefaultMutableTreeNode[] array) {
		
		getCommonRoot();
		commonRoot.createNode(0, node, globalCounter, array);
		
	}
	
	public CustomNode getCommonRoot() {
		
		if ( commonRoot != null)
			return commonRoot;
		else {
			CustomNode current = root;
			commonRoot = current;
			return commonRoot;
		}
		
	}
	
}