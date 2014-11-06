package thesaurus;

import java.awt.List;
import java.util.Iterator;
import java.util.LinkedList;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

    T data;
    TreeNode<T> parent;
    LinkedList<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<T>>();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
    
    public TreeNode<T> getNode(T object) {
    	if(this.data.equals(object))
    		return this;
    	
    	for(int i=0; i<this.children.size(); i++){
    		if(this.children.get(i).equals(object))
    			return this.children.get(i);
    		
    		return this.children.get(i).getNode(object);
    	}
    	
    	return null;
    }
    
    public TreeNode<T> getChild(T child) {
    	for(int i=0; i<this.children.size(); i++){
    		if(this.children.get(i).equals(child))
    			return this.children.get(i);
    		
    		return this.children.get(i).getChild(child);
    	}
    	
    	return null;
    }
    
    public LinkedList<TreeNode<T>> getChildren(){
    	return this.children;
    }

	@Override
	public Iterator<TreeNode<T>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

    // other features ...

}
