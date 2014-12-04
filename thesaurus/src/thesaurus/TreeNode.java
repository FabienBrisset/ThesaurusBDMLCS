package thesaurus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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

	/**
	 * @return un JTree pour afficher this graphiquement sous forme d'arbre
	 */
	public JTree toJTree(){
		ArrayList<TreeNode<T>> aTraiterTreeNode = new ArrayList<TreeNode<T>>();
		ArrayList<DefaultMutableTreeNode> aTraiterDefaultMutableTreeNode = new ArrayList<DefaultMutableTreeNode>();
		aTraiterTreeNode.add(this);
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(((Mot) this.data).getLibelleMot());
		aTraiterDefaultMutableTreeNode.add(top);
		DefaultMutableTreeNode currentNode = top;
		
		while(aTraiterTreeNode.size() > 0){
			for(int i=0; i<aTraiterTreeNode.get(0).children.size(); i++){
				DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(((Mot) aTraiterTreeNode.get(0).children.get(i).data).getLibelleMot());
				currentNode.add(newChild);
				aTraiterTreeNode.add(aTraiterTreeNode.get(0).children.get(i));
				aTraiterDefaultMutableTreeNode.add(newChild);
			}
			aTraiterDefaultMutableTreeNode.remove(0);
			aTraiterTreeNode.remove(0);
			if(aTraiterDefaultMutableTreeNode.size() > 0)
				currentNode = aTraiterDefaultMutableTreeNode.get(0);
		}
		
		DefaultTreeModel modelArbre = new DefaultTreeModel(top);
		
		return new JTree(modelArbre);
	}
}
