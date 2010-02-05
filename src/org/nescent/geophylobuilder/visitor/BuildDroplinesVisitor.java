package org.nescent.geophylobuilder.visitor;

import java.io.IOException;

import org.nescent.geophylobuilder.tree.TreeNode;

public class BuildDroplinesVisitor extends BuilderVisitor {
    public void visit(TreeNode node) {
	if (node.getType() == TreeNode.TREE_NODE_TYPE_SAMPLE) {
	    return;
	}
	double z;

	if (useDistance) {
	    z = (maxDepth - node.getDepthFromRoot()) * multipleZ;
	} else {
	    z = (maxDepth - node.getLevelFromRoot()) * multipleZ;

	}
	try {
	    writer.write("<Placemark>");
	    writer
		    .write("<name>" + node.getName().trim()
			    + ":Drop Line</name>");
	    writer.write("<description>Drop Line form node "
		    + node.getName().trim() + "</description>");
	    writer.write("<styleUrl>#treeBranchDrop</styleUrl>");
	    writer.write("<LineString>");
	    writer.write("<tessellate>1</tessellate>");
	    writer.write("<altitudeMode>absolute</altitudeMode>");
	    writer.write("<coordinates>");
	    writer.write(node.getX() + "," + node.getY() + "," + z + " "
		    + node.getX() + "," + node.getY() + "," + dropToZ
		    + "</coordinates>");
	    writer.write("</LineString>");
	    writer.write("</Placemark>");
	} catch (IOException ioe) {
	    throw new RuntimeException("failed to write the file.", ioe);
	}
    }
}
