package org.nescent.geophylobuilder.visitor;

import java.io.IOException;

import org.nescent.geophylobuilder.tree.TreeNode;

public class BuildNodeLabelsVisitor extends BuilderVisitor {

    @Override
    public void visit(TreeNode node) {

	if (node.getType() == TreeNode.TREE_NODE_TYPE_SAMPLE) {
	    return;
	}

	try {
	    writer.write("<Placemark>");
	    writer.write("<name>" + node.getName() + "</name>");
	    writer.write("<visibility>1</visibility>");
	    writer.write("<description>label of tree node</description>");
	    writer.write("<styleUrl>#label</styleUrl>");
	    writer.write("<Point>");
	    writer.write("<altitudeMode>absolute</altitudeMode>");
	    if (node.getType() == TreeNode.TREE_NODE_TYPE_SAMPLE) {
		node.setZ(0);
	    } else {
		if (useDistance) {
		    node.setZ((maxDepth - node.getDepthFromRoot()) * multipleZ);
		} else {
		    node.setZ((maxDepth - node.getLevelFromRoot()) * multipleZ);
		}
	    }
	    writer.write("<coordinates>" + node.getX() + "," + node.getY()
		    + "," + node.getZ() + "</coordinates>");
	    writer.write("</Point>");
	    writer.write("</Placemark>");
	} catch (IOException ioe) {
	    throw new RuntimeException("failed to write the file.", ioe);
	}

    }
}
