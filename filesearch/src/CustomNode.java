import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CustomNode {
	
	List<CustomNode> childs;
	List<CustomNode> leafs;
	String data;
	String incrementalPath;
	
	public CustomNode(String nodeValue, String incrementalPath ) {
		
		childs = new ArrayList<>();
		leafs = new ArrayList<>();
		data = nodeValue;
		this. incrementalPath = incrementalPath;
		
	}
	
	public boolean isLeaf() {
		
		return childs.isEmpty() && leafs.isEmpty();
		
	}
	
	public void addElement(String currentPath, String[] list) {
		
		while( list[0] == null || list[0].equals("") )
			
			list = Arrays.copyOfRange(list, 1, list.length);
		
		CustomNode currentChild = new CustomNode(list[0], currentPath+"\\"+list[0]);
		
		if ( list.length == 1 ) {
			leafs.add( currentChild );
			return;
		} else {
			int index = childs.indexOf( currentChild );
			if ( index == -1 ) {
				childs.add( currentChild );
				currentChild.addElement(currentChild.incrementalPath, Arrays.copyOfRange(list, 1, list.length));
			} else {
				CustomNode nextChild = childs.get(index);
				nextChild.addElement(currentChild.incrementalPath, Arrays.copyOfRange(list, 1, list.length));
			}
		}
		
	}
	
	
	
	public void createNode(int increment, DefaultMutableTreeNode node,
	                       int globalCounter, DefaultMutableTreeNode[] array) {
		
		globalCounter += 1;
		
		for( CustomNode n: childs) {
			globalCounter += 1;
			array[globalCounter] = new DefaultMutableTreeNode(n.data);
			node.add(array[globalCounter]);
			n.createNode(increment + 1, array[globalCounter], globalCounter, array);
		}
		for( CustomNode n: leafs) {
			globalCounter += 1;
			array[globalCounter] = new DefaultMutableTreeNode(n.data);
			node.add(array[globalCounter]);
		}
		
	}
	
	@Override
	public String toString() {
		
		return data;
		
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		CustomNode that = (CustomNode) o;
		
		if (!childs.equals(that.childs)) return false;
		if (!leafs.equals(that.leafs)) return false;
		if (!data.equals(that.data)) return false;
		return incrementalPath.equals(that.incrementalPath);
	}
	
	@Override
	public int hashCode() {
		int result = childs.hashCode();
		result = 31 * result + leafs.hashCode();
		result = 31 * result + data.hashCode();
		result = 31 * result + incrementalPath.hashCode();
		return result;
	}
}