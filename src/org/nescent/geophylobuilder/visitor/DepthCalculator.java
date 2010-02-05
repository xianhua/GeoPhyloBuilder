package org.nescent.geophylobuilder.visitor;

import org.nescent.geophylobuilder.tree.TreeNode;

public class DepthCalculator implements Visitor {

    public void visit(TreeNode node) {
	if (node.getType() == TreeNode.TREE_NODE_TYPE_ROOT) {
	    node.setDepthFromRoot(0);
	    node.setLevelFromRoot(0);
	} else {
	    node.setDepthFromRoot(node.getParent().getDepthFromRoot()
		    + node.getDistance());
	    node.setLevelFromRoot(node.getParent().getLevelFromRoot() + 1);
	}
    }

}
