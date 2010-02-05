package org.nescent.geophylobuilder.visitor;

import org.nescent.geophylobuilder.tree.TreeNode;

public class XYCalculator implements Visitor {
    boolean useNewOrigin = false, geocoordinate = true;
    double originX, originY;

    public boolean isUseNewOrigin() {
	return useNewOrigin;
    }

    public void setUseNewOrigin(boolean useNewOrigin) {
	this.useNewOrigin = useNewOrigin;
    }

    public boolean isGeocoordinate() {
	return geocoordinate;
    }

    public void setGeocoordinate(boolean geocoordinate) {
	this.geocoordinate = geocoordinate;
    }

    public double getOriginX() {
	return originX;
    }

    public void setOriginX(double originX) {
	this.originX = originX;
    }

    public double getOriginY() {
	return originY;
    }

    public void setOriginY(double originY) {
	this.originY = originY;
    }

    public void visit(TreeNode node) {

	if (node.isFixedLocation()) {
	    return;
	}

	int i = 0;
	double x, y;

	double[] xArray = new double[node.getChildren().size()];

	x = 0;
	y = 0;
	double tmp = 0;

	if (node.getChildren().size() > 0) {
	    for (TreeNode child : node.getChildren()) {
		tmp = child.getX();
		if (child.getX() < 0) {
		    tmp = 360 + tmp;
		}
		xArray[i++] = tmp;
		y = y + child.getY();
	    }

	    double maxx = 0;
	    int startx, j;

	    for (i = 0; i < node.getChildren().size() - 1; i++) {
		for (j = i + 1; j < node.getChildren().size(); j++) {
		    if (xArray[i] > xArray[j]) {
			double dtmp = xArray[i];
			xArray[i] = xArray[j];
			xArray[j] = dtmp;
		    }
		}
	    }
	    startx = 0;
	    for (i = 0; i < node.getChildren().size() - 1; i++) {
		tmp = xArray[i + 1] - xArray[i];
		if (tmp > maxx) {
		    maxx = tmp;
		    startx = i;
		}
	    }

	    tmp = 360 + xArray[0] - xArray[node.getChildren().size() - 1];

	    if (tmp > maxx) {
		maxx = tmp;
		startx = node.getChildren().size() - 1;
	    }

	    for (i = 0; i < node.getChildren().size(); i++) {
		tmp = xArray[startx] - xArray[i];
		if (tmp < 0) {
		    tmp = 360 + tmp;
		}
		x = x + tmp;
	    }
	    x = x / node.getChildren().size();
	    x = xArray[startx] - x;

	    if (x > 180) {
		x = x - 360;
	    }

	    if (useNewOrigin) {
		node.setX((x + originX) / 2);
		node.setY((y + originY) / (node.getChildren().size() + 1));
	    } else {
		node.setX(x);
		node.setY(y / node.getChildren().size());
	    }
	} else {
	    if (node.getType() != TreeNode.TREE_NODE_TYPE_SAMPLE) {
		throw new RuntimeException("No samples found for "
			+ node.getName());
	    }
	}
    }

}
