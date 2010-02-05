package org.nescent.geophylobuilder.visitor;

import java.io.Writer;

import org.nescent.geophylobuilder.tree.TreeNode;

public class BuilderVisitor implements Visitor {
    boolean useNewOrigin;
    double originX;
    double originY;
    double multipleZ;
    double maxDepth;
    boolean useDistance;
    double dropToZ;
    Writer writer;

    public boolean isUseNewOrigin() {
	return useNewOrigin;
    }

    public void setUseNewOrigin(boolean useNewOrigin) {
	this.useNewOrigin = useNewOrigin;
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

    public double getMultipleZ() {
	return multipleZ;
    }

    public void setMultipleZ(double multipleZ) {
	this.multipleZ = multipleZ;
    }

    public double getMaxDepth() {
	return maxDepth;
    }

    public void setMaxDepth(double maxDepth) {
	this.maxDepth = maxDepth;
    }

    public boolean isUseDistance() {
	return useDistance;
    }

    public void setUseDistance(boolean useDistance) {
	this.useDistance = useDistance;
    }

    public double getDropToZ() {
	return dropToZ;
    }

    public void setDropToZ(double dropToZ) {
	this.dropToZ = dropToZ;
    }

    public Writer getWriter() {
	return writer;
    }

    public void setWriter(Writer writer) {
	this.writer = writer;
    }

    public void visit(TreeNode node) {
	// TODO Auto-generated method stub

    }

}
