package org.nescent.geophylobuilder.tree;

import java.util.ArrayList;
import java.util.List;

import org.nescent.geophylobuilder.visitor.Visitor;

public class TreeNode {
    public static int TREE_NODE_TYPE_ROOT = 0;
    public static int TREE_NODE_TYPE_INNER = 1;
    public static int TREE_NODE_TYPE_TERMINAL = 2;
    public static int TREE_NODE_TYPE_SAMPLE = 3;
    public static int VISITOR_TYPE_FORE = 0;
    public static int VISITOR_TYPE_BACK = 1;

    int id;
    String rankId;
    String name;
    int type;
    double distance;
    double depthFromRoot;
    int levelFromRoot;
    TreeNode parent;
    List<TreeNode> children;
    List<String> attributes;
    boolean withDistance;
    double x;
    double y;
    double z;
    boolean fixedLocation;

    public TreeNode() {
	children = new ArrayList<TreeNode>();
	attributes = new ArrayList<String>();
	withDistance = false;
	fixedLocation = false;
	type = TreeNode.TREE_NODE_TYPE_INNER;
    }

    public static int getTREE_NODE_TYPE_ROOT() {
	return TREE_NODE_TYPE_ROOT;
    }

    public static void setTREE_NODE_TYPE_ROOT(int tree_node_type_root) {
	TREE_NODE_TYPE_ROOT = tree_node_type_root;
    }

    public static int getTREE_NODE_TYPE_INNER() {
	return TREE_NODE_TYPE_INNER;
    }

    public static void setTREE_NODE_TYPE_INNER(int tree_node_type_inner) {
	TREE_NODE_TYPE_INNER = tree_node_type_inner;
    }

    public static int getTREE_NODE_TYPE_TERMINAL() {
	return TREE_NODE_TYPE_TERMINAL;
    }

    public static void setTREE_NODE_TYPE_TERMINAL(int tree_node_type_terminal) {
	TREE_NODE_TYPE_TERMINAL = tree_node_type_terminal;
    }

    public static int getTREE_NODE_TYPE_SAMPLE() {
	return TREE_NODE_TYPE_SAMPLE;
    }

    public static void setTREE_NODE_TYPE_SAMPLE(int tree_node_type_sample) {
	TREE_NODE_TYPE_SAMPLE = tree_node_type_sample;
    }

    public static int getVISITOR_TYPE_FORE() {
	return VISITOR_TYPE_FORE;
    }

    public static void setVISITOR_TYPE_FORE(int visitor_type_fore) {
	VISITOR_TYPE_FORE = visitor_type_fore;
    }

    public static int getVISITOR_TYPE_BACK() {
	return VISITOR_TYPE_BACK;
    }

    public static void setVISITOR_TYPE_BACK(int visitor_type_back) {
	VISITOR_TYPE_BACK = visitor_type_back;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getRankId() {
	return rankId;
    }

    public void setRankId(String rankId) {
	this.rankId = rankId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }

    public double getDistance() {
	return distance;
    }

    public void setDistance(double distance) {
	this.distance = distance;
    }

    public double getDepthFromRoot() {
	return depthFromRoot;
    }

    public void setDepthFromRoot(double depthFromRoot) {
	this.depthFromRoot = depthFromRoot;
    }

    public int getLevelFromRoot() {
	return levelFromRoot;
    }

    public void setLevelFromRoot(int levelFromRoot) {
	this.levelFromRoot = levelFromRoot;
    }

    public TreeNode getParent() {
	return parent;
    }

    public void setParent(TreeNode parent) {
	this.parent = parent;
    }

    public List<TreeNode> getChildren() {
	return children;
    }

    public void setChildren(List<TreeNode> children) {
	this.children = children;
    }

    public List<String> getAttributes() {
	return attributes;
    }

    public void setAttributes(List<String> attributes) {
	this.attributes = attributes;
    }

    public boolean isWithDistance() {
	return withDistance;
    }

    public void setWithDistance(boolean withDistance) {
	this.withDistance = withDistance;
    }

    public double getX() {
	return x;
    }

    public void setX(double x) {
	this.x = x;
    }

    public double getY() {
	return y;
    }

    public void setY(double y) {
	this.y = y;
    }

    public double getZ() {
	return z;
    }

    public void setZ(double z) {
	this.z = z;
    }

    public boolean isFixedLocation() {
	return fixedLocation;
    }

    public void setFixedLocation(boolean fixedLocation) {
	this.fixedLocation = fixedLocation;
    }

    public void visitMe(Visitor visitor, int order) {
	if (order == TreeNode.VISITOR_TYPE_FORE) {
	    visitor.visit(this);

	    for (TreeNode child : children) {
		child.visitMe(visitor, order);
	    }
	} else if (order == TreeNode.VISITOR_TYPE_BACK) {
	    for (TreeNode child : children) {
		child.visitMe(visitor, order);
	    }
	    visitor.visit(this);
	}
    }

    public void addChild(TreeNode node) {
	children.add(node);
	node.setParent(this);
    }

    public void removeChild(TreeNode node) {
	children.remove(node);
    }

    public TreeNode find(String key) {
	return find(key, false);
    }

    public TreeNode find(String key, boolean includeAttributes) {
	if (key == null) {
	    return null;

	}

	if (key.equals(name)) {
	    return this;
	} else {
	    if (includeAttributes) {
		if (attributes.contains(key)) {
		    return this;
		}
	    }
	}

	for (TreeNode child : children) {
	    TreeNode node = child.find(key, includeAttributes);
	    if (node != null) {
		return node;
	    }

	}
	return null;
    }

    public List<TreeNode> findAll(String key) {
	return findAll(key);
    }

    public List<TreeNode> findAll(String key, boolean includeAttributes) {
	if (key == null) {
	    return null;
	}
	List<TreeNode> list = new ArrayList<TreeNode>();
	if (key.equals(name)) {
	    list.add(this);
	} else {
	    if (includeAttributes) {
		if (attributes.contains(key)) {
		    list.add(this);
		}
	    }
	}
	for (TreeNode child : children) {
	    List<TreeNode> l = child.findAll(key, includeAttributes);
	    if (l != null)
		list.addAll(l);
	}

	return list;
    }
}
