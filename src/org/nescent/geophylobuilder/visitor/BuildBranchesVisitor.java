package org.nescent.geophylobuilder.visitor;

import java.io.IOException;

import org.nescent.geophylobuilder.tree.TreeNode;

public class BuildBranchesVisitor extends BuilderVisitor {
    int count = 0;

    @Override
    public void visit(TreeNode node) {
	for (TreeNode chd : node.getChildren()) {
	    if (chd.getType() == TreeNode.TREE_NODE_TYPE_SAMPLE) {
		continue;
	    }
	    double toz, frz;

	    if (chd.getType() == TreeNode.TREE_NODE_TYPE_SAMPLE) {
		toz = 0;
	    } else {
		if (useDistance) {
		    toz = (maxDepth - chd.getDepthFromRoot()) * multipleZ;
		} else {
		    toz = (maxDepth - chd.getLevelFromRoot()) * multipleZ;
		}
	    }

	    if (node.getType() == TreeNode.TREE_NODE_TYPE_SAMPLE) {
		frz = 0;
	    } else {
		if (useDistance) {
		    frz = (maxDepth - node.getDepthFromRoot()) * multipleZ;
		} else {
		    frz = (maxDepth - node.getLevelFromRoot()) * multipleZ;
		}
	    }
	    try {
		writer.write("<Placemark>");
		writer.write("<name>" + (node.getName()).trim() + "-"
			+ (chd.getName()).trim() + "</name>");
		writer.write("<description>tree branch</description>");
		writer.write("<styleUrl>#treeBranch</styleUrl>");
		writer.write("<LineString>");
		writer.write("<tessellate>1</tessellate>");
		writer.write("<altitudeMode>absolute</altitudeMode>");
		writer.write("<coordinates>");
		writer.write(node.getX() + "," + node.getY() + "," + frz + " "
			+ chd.getX() + "," + chd.getY() + "," + toz
			+ "</coordinates>");
		writer.write("</LineString>");
		writer.write("</Placemark>");
	    } catch (IOException ioe) {
		throw new RuntimeException("failed to write the kml file.", ioe);
	    }
	}
    }
}
