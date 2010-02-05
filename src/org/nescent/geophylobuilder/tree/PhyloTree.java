package org.nescent.geophylobuilder.tree;

import java.util.List;

import org.nescent.geophylobuilder.visitor.DepthVisitor;
import org.nescent.geophylobuilder.visitor.Visitor;

public class PhyloTree {
    TreeNode root;
    String treeName;

    public TreeNode getRoot() {
	return root;
    }

    public void setRoot(TreeNode root) {
	this.root = root;
    }

    public String getTreeName() {
	return treeName;
    }

    public void setTreeName(String treeName) {
	this.treeName = treeName;
    }

    public void visitMe(Visitor visitor, int order) {
	if (root == null) {
	    return;
	} else {
	    root.visitMe(visitor, order);
	}
    }

    public TreeNode find(String key) {
	return find(key, false);
    }

    public TreeNode find(String key, boolean includeAttributes) {
	if (root == null) {
	    return null;
	}
	return root.find(key, includeAttributes);
    }

    public List<TreeNode> findAll(String key) {
	return findAll(key);
    }

    public List<TreeNode> findAll(String key, boolean includeAttributes) {
	if (root == null) {
	    return null;
	}
	return root.findAll(key, includeAttributes);
    }

    public double getDepth(boolean useDistance) {
	DepthVisitor visitor = new DepthVisitor();
	visitor.setUseDistance(useDistance);
	visitMe(visitor, TreeNode.VISITOR_TYPE_FORE);
	return visitor.getDepth();
    }

    public boolean isDistanced() {
	if (root == null) {
	    return false;
	}
	if (root.isWithDistance()) {
	    return true;
	}
	if (root.getChildren().size() > 0) {
	    TreeNode node = root.getChildren().get(0);
	    return node.isWithDistance();
	}
	return false;
    }
}
