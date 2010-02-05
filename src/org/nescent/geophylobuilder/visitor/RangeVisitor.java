package org.nescent.geophylobuilder.visitor;

import org.nescent.geophylobuilder.tree.TreeNode;

public class RangeVisitor implements Visitor {
    double minx = 0, miny = 0, maxx = 0, maxy = 0;

    public double getMinx() {
	return minx;
    }

    public void setMinx(double minx) {
	this.minx = minx;
    }

    public double getMiny() {
	return miny;
    }

    public void setMiny(double miny) {
	this.miny = miny;
    }

    public double getMaxx() {
	return maxx;
    }

    public void setMaxx(double maxx) {
	this.maxx = maxx;
    }

    public double getMaxy() {
	return maxy;
    }

    public void setMaxy(double maxy) {
	this.maxy = maxy;
    }

    public void visit(TreeNode node) {
	if (minx == 0) {
	    minx = node.getX();
	} else {
	    if (minx > node.getX()) {
		minx = node.getX();
	    }
	}

	if (miny == 0) {
	    miny = node.getY();
	} else {
	    if (miny > node.getY()) {
		miny = node.getY();
	    }
	}

	if (maxx == 0) {
	    maxx = node.getX();
	} else {
	    if (maxx < node.getX()) {
		maxx = node.getX();
	    }
	}

	if (maxy == 0) {
	    maxy = node.getY();
	} else {
	    if (maxy < node.getY()) {
		maxy = node.getY();
	    }
	}

    }

}
