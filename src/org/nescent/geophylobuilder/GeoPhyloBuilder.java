package org.nescent.geophylobuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.nescent.geophylobuilder.parser.NwkTreeParser;
import org.nescent.geophylobuilder.tree.PhyloTree;
import org.nescent.geophylobuilder.tree.TreeNode;
import org.nescent.geophylobuilder.visitor.BuildBranchesVisitor;
import org.nescent.geophylobuilder.visitor.BuildDroplinesVisitor;
import org.nescent.geophylobuilder.visitor.BuildNodeLabelsVisitor;
import org.nescent.geophylobuilder.visitor.BuildNodesVisitor;
import org.nescent.geophylobuilder.visitor.DepthCalculator;
import org.nescent.geophylobuilder.visitor.RangeVisitor;
import org.nescent.geophylobuilder.visitor.XYCalculator;

public class GeoPhyloBuilder {
    PhyloTree tree;
    double maxDepth;
    double multipleZ = 30000;

    public void readTree(String treestring) throws Exception {
	tree = new PhyloTree();
	tree.setRoot(NwkTreeParser.parse(treestring));
	tree.setTreeName("tree");
    }

    public void readTreeFromFile(String file) throws Exception {
	BufferedReader r = new BufferedReader(new FileReader(new File(file)));
	String str = "";
	String s = r.readLine();
	while (s != null) {
	    str += s;
	    s = r.readLine();
	}
	r.close();
	TreeNode root = NwkTreeParser.parse(str);
	tree = new PhyloTree();
	tree.setRoot(root);
	tree.setTreeName("tree");
    }

    public void readPositionFile(String file) throws Exception {
	BufferedReader r = new BufferedReader(new FileReader(new File(file)));

	String s = r.readLine();
	while (s != null) {
	    readPosition(s);
	    s = r.readLine();
	}
	r.close();

    }

    public void readPosition(String position) throws Exception {
	String ss[] = position.split(",");
	if (ss.length != 3)
	    throw new Exception("invalid row: " + position);
	String name = ss[0];
	double lat = Double.parseDouble(ss[1]);
	double lon = Double.parseDouble(ss[2]);
	TreeNode node = tree.find(name);
	if (node == null) {
	    throw new Exception("failed to find the node: " + name);
	}

	TreeNode newnode = new TreeNode();
	newnode.setName(node.getName() + ":sample");
	newnode.setType(TreeNode.TREE_NODE_TYPE_SAMPLE);
	newnode.setX(lon);
	newnode.setY(lat);
	node.addChild(newnode);

    }

    public void buildTree(String file) throws Exception {
	BufferedWriter w = new BufferedWriter(new FileWriter(new File(file)));
	getZ();
	getXY();
	writeBegin(w);
	buildNodes(w);
	buildBranches(w);
	buildDroplines(w);
	writeEnd(w);
	w.flush();
	w.close();
    }

    public void getXY() {
	XYCalculator xyzCal = new XYCalculator();
	tree.visitMe(xyzCal, TreeNode.VISITOR_TYPE_BACK);
    }

    public void getZ() {
	DepthCalculator visitor = new DepthCalculator();
	tree.visitMe(visitor, TreeNode.VISITOR_TYPE_FORE);
	maxDepth = tree.getDepth(tree.isDistanced());
    }

    public void buildNodesLabel(Writer writer) {
	try {
	    writer.write("<Folder>");
	    writer.write("<name>Labels</name>");
	    writer.write("<description>Labels of Tree Nodes</description>");

	    BuildNodeLabelsVisitor buildNodeLabelsVisitor = new BuildNodeLabelsVisitor();
	    buildNodeLabelsVisitor.setWriter(writer);
	    buildNodeLabelsVisitor.setUseNewOrigin(false);

	    buildNodeLabelsVisitor.setUseDistance(tree.isDistanced());
	    buildNodeLabelsVisitor.setMultipleZ(multipleZ);

	    buildNodeLabelsVisitor.setMaxDepth(maxDepth);

	    tree.visitMe(buildNodeLabelsVisitor, TreeNode.VISITOR_TYPE_FORE);
	    writer.write("</Folder>");

	} catch (IOException ioe) {
	    throw new RuntimeException("failed to write kml file.", ioe);
	}
    }

    public void buildNodes(Writer writer) {

	try {
	    writer.write("<Folder>");
	    writer.write("<name>Tree Nodes</name>");
	    writer.write("<description>Tree Nodes</description>");
	    buildNodesLabel(writer);

	    BuildNodesVisitor buildNodeVisitor = new BuildNodesVisitor();
	    buildNodeVisitor.setWriter(writer);
	    buildNodeVisitor.setUseNewOrigin(false);
	    if (tree.isDistanced()) {
		buildNodeVisitor.setUseDistance(true);
	    }

	    buildNodeVisitor.setMultipleZ(multipleZ);
	    buildNodeVisitor.setMaxDepth(maxDepth);

	    tree.visitMe(buildNodeVisitor, TreeNode.VISITOR_TYPE_FORE);
	    writer.write("</Folder>");
	} catch (IOException ioe) {
	    throw new RuntimeException("failed to write kml file.", ioe);
	}
    }

    public void buildDroplines(Writer w) {
	try {
	    w.write("<Folder>");
	    w.write("<name>Drop Lines</name>");
	    w
		    .write("<description>Lines linking from tree nodes to their drop nodes</description>");
	} catch (IOException ioe) {
	    throw new RuntimeException("failed to write the kml file.", ioe);
	}
	BuildDroplinesVisitor buildDropLinesVisitor = new BuildDroplinesVisitor();
	buildDropLinesVisitor.setWriter(w);
	buildDropLinesVisitor.setUseNewOrigin(false);
	buildDropLinesVisitor.setUseDistance(tree.isDistanced());
	buildDropLinesVisitor.setMultipleZ(multipleZ);
	buildDropLinesVisitor.setMaxDepth(maxDepth);

	tree.visitMe(buildDropLinesVisitor, TreeNode.VISITOR_TYPE_FORE);
	try {
	    w.write("</Folder>");
	} catch (IOException ioe) {
	    throw new RuntimeException("failed to write the kml file.", ioe);
	}
    }

    public void buildBranches(Writer w) {

	try {
	    w.write("<Folder>");
	    w.write("<name>Tree Braches</name>");
	    w.write("<description>Tree Nodes</description>");

	    BuildBranchesVisitor buildLineVisitor = new BuildBranchesVisitor();
	    buildLineVisitor.setWriter(w);
	    buildLineVisitor.setUseNewOrigin(false);
	    if (tree.isDistanced()) {
		buildLineVisitor.setUseDistance(true);
	    } else {
		buildLineVisitor.setUseDistance(false);
	    }

	    buildLineVisitor.setMultipleZ(multipleZ);
	    buildLineVisitor.setMaxDepth(maxDepth);
	    tree.visitMe(buildLineVisitor, TreeNode.VISITOR_TYPE_FORE);
	    w.write("</Folder>");
	} catch (IOException ioe) {
	    throw new RuntimeException("failed to write the kml file.", ioe);
	}

    }

    public void writeBegin(Writer w) throws IOException {

	RangeVisitor vst = new RangeVisitor();
	tree.visitMe(vst, TreeNode.VISITOR_TYPE_FORE);
	double width = 100000 * (vst.getMaxx() - vst.getMinx());

	String rootImage = "http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png";
	String nodeImage = "http://maps.google.com/mapfiles/kml/shapes/open-diamond.png";
	String sampleImage = "http://maps.google.com/mapfiles/kml/pal2/icon13.png";
	String dropNodeImage = "http://maps.google.com/mapfiles/kml/paddle/ylw-circle.png";

	w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	w.write("<kml xmlns=\"http://earth.google.com/kml/2.1\">");
	w.write("<Document>");
	w.write("<name>" + tree.getTreeName() + "</name>");
	w.write("<description>Generated by GeoPhyloBuilder 1.0</description>");
	w.write("<LookAt>");
	w.write("<longitude>" + tree.getRoot().getX() + "</longitude>");
	w.write("<latitude>" + tree.getRoot().getY() + "</latitude>");
	w.write("<altitude>0</altitude>");
	w.write("<range>" + width + "</range>");
	w.write("<tilt>65</tilt>");
	w.write("<heading>-148.4122922628044</heading>");
	w.write("</LookAt>");
	w.write("<StyleMap id=\"treeNode\">");
	w.write("<Pair>");
	w.write("	<key>normal</key>");
	w.write("	<styleUrl>#treeNode_normal</styleUrl>");
	w.write("</Pair>");
	w.write("<Pair>");
	w.write("	<key>highlight</key>");
	w.write("	<styleUrl>#treeNode_highlight</styleUrl>");
	w.write("</Pair>");
	w.write("</StyleMap>");
	w.write("<Style id=\"treeNode_normal\">");
	w.write("<IconStyle>");
	w.write("	<scale>0.5</scale>");
	w.write("	<Icon>");
	w.write("		<href>" + nodeImage + "</href>");
	w.write("	</Icon>");
	w.write("</IconStyle>");
	w.write("<LabelStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>0.6</scale>");
	w.write("</LabelStyle>");
	w.write("<LineStyle>");
	w.write("	<width>2</width>");
	w.write("</LineStyle>");
	w.write("</Style>");
	w.write("<Style id=\"treeNode_highlight\">");
	w.write("<IconStyle>");
	w.write("	<scale>0.5</scale>");
	w.write("	<Icon>");
	w.write("		<href>" + nodeImage + "</href>");
	w.write("	</Icon>");
	w.write("</IconStyle>");
	w.write("<LabelStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>0.6</scale>");
	w.write("</LabelStyle>");
	w.write("<LineStyle>");
	w.write("	<width>2</width>");
	w.write("</LineStyle>");
	w.write("</Style>");

	w.write("<StyleMap id=\"treeNodeDrop\">");
	w.write("<Pair>");
	w.write("	<key>normal</key>");
	w.write("	<styleUrl>#treeNodeDrop_normal</styleUrl>");
	w.write("</Pair>");
	w.write("<Pair>");
	w.write("	<key>highlight</key>");
	w.write("	<styleUrl>#treeNodeDrop_highlight</styleUrl>");
	w.write("</Pair>");
	w.write("</StyleMap>");
	w.write("<Style id=\"treeNodeDrop_normal\">");
	w.write("<IconStyle>");
	w.write("	<scale>0.5</scale>");
	w.write("	<Icon>");
	w.write("		<href>" + dropNodeImage + "</href>");
	w.write("	</Icon>");
	w.write("</IconStyle>");
	w.write("<LabelStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>0.6</scale>");
	w.write("</LabelStyle>");
	w.write("<LineStyle>");
	w.write("	<width>2</width>");
	w.write("</LineStyle>");
	w.write("</Style>");
	w.write("<Style id=\"treeNodeDrop_highlight\">");
	w.write("<IconStyle>");
	w.write("	<scale>0.5</scale>");
	w.write("	<Icon>");
	w.write("		<href>" + dropNodeImage + "</href>");
	w.write("	</Icon>");
	w.write("</IconStyle>");
	w.write("<LabelStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>0.6</scale>");
	w.write("</LabelStyle>");
	w.write("<LineStyle>");
	w.write("	<width>2</width>");
	w.write("</LineStyle>");
	w.write("</Style>");

	w.write("<StyleMap id=\"treeNodeRoot\">");
	w.write("<Pair>");
	w.write("	<key>normal</key>");
	w.write("	<styleUrl>#treeNodeRoot_normal</styleUrl>");
	w.write("</Pair>");
	w.write("<Pair>");
	w.write("	<key>highlight</key>");
	w.write("	<styleUrl>#treeNodeRoot_highlight</styleUrl>");
	w.write("</Pair>");
	w.write("</StyleMap>");
	w.write("<Style id=\"treeNodeRoot_normal\">");
	w.write("<IconStyle>");
	w.write("	<scale>0.5</scale>");
	w.write("	<Icon>");
	w.write("		<href>" + rootImage + "</href>");
	w.write("	</Icon>");
	w.write("</IconStyle>");
	w.write("<LabelStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>0.6</scale>");
	w.write("</LabelStyle>");
	w.write("<LineStyle>");
	w.write("	<width>2</width>");
	w.write("</LineStyle>");
	w.write("</Style>");
	w.write("<Style id=\"treeNodeRoot_highlight\">");
	w.write("<IconStyle>");
	w.write("	<scale>0.5</scale>");
	w.write("	<Icon>");
	w.write("		<href>" + rootImage + "</href>");
	w.write("	</Icon>");
	w.write("</IconStyle>");
	w.write("<LabelStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>0.6</scale>");
	w.write("</LabelStyle>");
	w.write("<LineStyle>");
	w.write("	<width>2</width>");
	w.write("</LineStyle>");
	w.write("</Style>");

	w.write("<StyleMap id=\"treeNodeSample\">");
	w.write("<Pair>");
	w.write("	<key>normal</key>");
	w.write("	<styleUrl>#treeNodeSample_normal</styleUrl>");
	w.write("</Pair>");
	w.write("<Pair>");
	w.write("	<key>highlight</key>");
	w.write("	<styleUrl>#treeNodeSample_highlight</styleUrl>");
	w.write("</Pair>");
	w.write("</StyleMap>");
	w.write("<Style id=\"treeNodeSample_normal\">");
	w.write("<IconStyle>");
	w.write("	<scale>0.5</scale>");
	w.write("	<Icon>");
	w.write("		<href>" + sampleImage + "</href>");
	w.write("	</Icon>");
	w.write("</IconStyle>");
	w.write("<LabelStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>0.6</scale>");
	w.write("</LabelStyle>");
	w.write("<LineStyle>");
	w.write("	<width>2</width>");
	w.write("</LineStyle>");
	w.write("</Style>");
	w.write("<Style id=\"treeNodeSample_highlight\">");
	w.write("<IconStyle>");
	w.write("	<scale>0.5</scale>");
	w.write("	<Icon>");
	w.write("		<href>" + sampleImage + "</href>");
	w.write("	</Icon>");
	w.write("</IconStyle>");
	w.write("<LabelStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>0.6</scale>");
	w.write("</LabelStyle>");
	w.write("<LineStyle>");
	w.write("	<width>2</width>");
	w.write("</LineStyle>");
	w.write("</Style>");

	w.write("<StyleMap id=\"treeBranch\">");
	w.write("<Pair>");
	w.write("<key>normal</key>");
	w.write("<styleUrl>#treeBranch_normal</styleUrl>");
	w.write("</Pair>");
	w.write("<Pair>");
	w.write("<key>highlight</key>");
	w.write("<styleUrl>#treeBranch_highlight</styleUrl>");
	w.write("</Pair>");
	w.write("</StyleMap>");
	w.write("<Style id=\"treeBranch_normal\">");
	w.write("<LineStyle>");
	w.write("<color>ff7fffff</color>");
	w.write("<width>1.5</width>");
	w.write("</LineStyle>");
	w.write("<PolyStyle>");
	w.write("<color>7f00ff00</color>");
	w.write("</PolyStyle>");
	w.write("</Style>");
	w.write("<Style id=\"treeBranch_highlight\">");
	w.write("<LineStyle>");
	w.write("<color>ff7fffff</color>");
	w.write("<width>1.5</width>");
	w.write("</LineStyle>");
	w.write("<PolyStyle>");
	w.write("<color>7f00ff00</color>");
	w.write("</PolyStyle>");
	w.write("</Style>");

	w.write("<StyleMap id=\"treeBranchDrop\">");
	w.write("<Pair>");
	w.write("<key>normal</key>");
	w.write("<styleUrl>#treeBranchDrop_normal</styleUrl>");
	w.write("</Pair>");
	w.write("<Pair>");
	w.write("<key>highlight</key>");
	w.write("<styleUrl>#treeBranchDrop_highlight</styleUrl>");
	w.write("</Pair>");
	w.write("</StyleMap>");
	w.write("<Style id=\"treeBranchDrop_normal\">");
	w.write("<LineStyle>");
	w.write("<color>7777ffff</color>");
	w.write("<width>1.0</width>");
	w.write("</LineStyle>");
	w.write("<PolyStyle>");
	w.write("<color>7f00ff00</color>");
	w.write("</PolyStyle>");
	w.write("</Style>");
	w.write("<Style id=\"treeBranch_highlight\">");
	w.write("<LineStyle>");
	w.write("<color>ff7fffff</color>");
	w.write("<width>1.5</width>");
	w.write("</LineStyle>");
	w.write("<PolyStyle>");
	w.write("<color>7f00ff00</color>");
	w.write("</PolyStyle>");
	w.write("</Style>");

	w.write("<StyleMap id=\"label\">");
	w.write("<Pair>");
	w.write("	<key>normal</key>");
	w.write("	<styleUrl>#label_normal</styleUrl>");
	w.write("</Pair>");
	w.write("<Pair>");
	w.write("	<key>highlight</key>");
	w.write("	<styleUrl>#label_highlight</styleUrl>");
	w.write("</Pair>");
	w.write("</StyleMap>");
	w.write("<Style id=\"label_normal\">");
	w.write("<IconStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>0.1</scale>");
	w.write("	<Icon>");
	w
		.write("		<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>");
	w.write("	</Icon>");
	w
		.write("	<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>");
	w.write("</IconStyle>");
	w.write("<LabelStyle>");
	w.write("	<scale>0.6</scale>");
	w.write("</LabelStyle>");
	w.write("<ListStyle>");
	w.write("</ListStyle>");
	w.write("</Style>");
	w.write("<Style id=\"label_highlight\">");
	w.write("<IconStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>0.1</scale>");
	w.write("	<Icon>");
	w
		.write("		<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>");
	w.write("	</Icon>");
	w
		.write("	<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>");
	w.write("	</IconStyle>");
	w.write("<LabelStyle>");
	w.write("	<scale>0.6</scale>");
	w.write("</LabelStyle>");
	w.write("<ListStyle>");
	w.write("</ListStyle>");
	w.write("</Style>");

	w.write("<StyleMap id=\"sampleLink\">");
	w.write("<Pair>");
	w.write("	<key>normal</key>");
	w.write("	<styleUrl>#sampleLink_normal</styleUrl>");
	w.write("</Pair>");
	w.write("<Pair>");
	w.write("	<key>highlight</key>");
	w.write("	<styleUrl>#sampleLink_highlight</styleUrl>");
	w.write("	</Pair>");
	w.write("</StyleMap>");
	w.write("<Style id=\"sampleLink_highlight\">");
	w.write("<IconStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>1.3</scale>");
	w.write("	<Icon>");
	w
		.write("	<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>");
	w.write("	</Icon>");
	w
		.write("	<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>");
	w.write("</IconStyle>");
	w.write("<ListStyle>");
	w.write("</ListStyle>");
	w.write("<LineStyle>");
	w.write("	<color>ff00ffaa</color>");
	w.write("</LineStyle>");
	w.write("</Style>");
	w.write("<Style id=\"sampleLink_normal\">");
	w.write("	<IconStyle>");
	w.write("	<color>00ffffff</color>");
	w.write("	<scale>1.1</scale>");
	w.write("	<Icon>");
	w
		.write("	<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>");
	w.write("	</Icon>");
	w
		.write("	<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>");
	w.write("	</IconStyle>");
	w.write("<ListStyle>");
	w.write("</ListStyle>");
	w.write("		<LineStyle>");
	w.write("	<color>ff00ffaa</color>");
	w.write("	</LineStyle>");
	w.write("</Style>");

    }

    public void writeEnd(Writer w) throws IOException {
	w.write("</Document>");
	w.write("</kml>");
    }

    public static void main(String agrs[]) {
	String nwk = "((((((((917:1,1011:1):2,981:5):2,983:2):4,(988:1,974:1):3):4,((893:3,1035:0):1,1101:7):6):31,1033:22):31,(((941:4,977:0):0,976:1):1,(((((346:0,919:1):0,407:0):0,1098:0):0,1102:1):1,1031:9):3):32):47,439:31);";
	// String nwk = "(A,B);";
	String file = "c:\\temp\\positions.txt";

	GeoPhyloBuilder builder = new GeoPhyloBuilder();
	try {
	    builder.readTree(nwk);
	    builder.readPositionFile(file);
	    builder.buildTree("c:\\temp\\tree.kml");
	} catch (Exception e) {
	    System.out.println(e);
	    System.exit(0);
	}
    }
}
