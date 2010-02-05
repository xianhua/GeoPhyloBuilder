package org.nescent.geophylobuilder.visitor;

import org.nescent.geophylobuilder.tree.TreeNode;

public class DepthVisitor implements Visitor {
    boolean useDistance;
    double depth;

    public void visit(TreeNode node) {
	if (useDistance) {
	    if (node.getDepthFromRoot() > depth) {
		depth = node.getDepthFromRoot();
	    }
	} else {
	    if (node.getLevelFromRoot() > depth) {
		depth = node.getLevelFromRoot();
	    }
	}
    }

    public boolean isUseDistance() {
	return useDistance;
    }

    public void setUseDistance(boolean useDistance) {
	this.useDistance = useDistance;
    }

    public double getDepth() {
	return depth;
    }

    public void setDepth(double depth) {
	this.depth = depth;
    }

}
