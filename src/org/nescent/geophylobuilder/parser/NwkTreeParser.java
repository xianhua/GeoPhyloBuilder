package org.nescent.geophylobuilder.parser;

import java.util.Stack;

import org.nescent.geophylobuilder.tree.TreeNode;

public class NwkTreeParser {
    public static TreeNode parse(String str) throws Exception {

	Stack<TreeNode> st = new Stack<TreeNode>();

	TreeNode treeNode;
	TreeNode root = null;
	int MyID;
	int internalNodeID;
	TreeNode parent = null, parent1 = null;

	MyID = 0;
	internalNodeID = 0;

	NwkToken tk;
	NwkTokenizer nwktokenizer = new NwkTokenizer(str);
	tk = nwktokenizer.nextToken();
	while (tk.getType() != NwkToken.TOKEN_TYPE_END) {
	    switch (tk.getType()) {
	    case NwkToken.TOKEN_TYPE_LEFT_PARENTHESIS: {
		if (!st.isEmpty()) {
		    parent = st.peek();
		}

		MyID = MyID + 1;
		treeNode = new TreeNode();

		if (parent == null) {
		    treeNode.setId(MyID);
		    treeNode.setName("root");
		    treeNode.setType(TreeNode.TREE_NODE_TYPE_ROOT);
		    root = treeNode;
		    st.push(treeNode);
		} else {

		    treeNode.setId(MyID);
		    internalNodeID = internalNodeID + 1;
		    treeNode.setName("node_" + internalNodeID);
		    treeNode.setType(TreeNode.TREE_NODE_TYPE_INNER);
		    parent.addChild(treeNode);
		    st.push(treeNode);
		}
		break;
	    }
	    case NwkToken.TOKEN_TYPE_RIGHT_PARENTHESIS: {
		if (st.isEmpty()) {
		    throw new Exception(
			    "NWK Tree Syntex error: no left parenthesis accompanying the right parenthesis.");
		}

		parent1 = st.pop();
		if (parent1 == null) {
		    throw new Exception(
			    "NWK Tree Syntex error: no left parenthesis accompanying the right parenthesis.");
		}
		break;
	    }
	    case NwkToken.TOKEN_TYPE_COLON: {
		if (parent1 == null) {
		    throw new Exception(
			    "NWK Tree Syntex error: no tree node specified for the colon");
		}
		NwkToken tktmp;
		tktmp = nwktokenizer.nextToken();
		if (tktmp.getType() != NwkToken.TOKEN_TYPE_NUMBER) {
		    throw new Exception(
			    "NWK Tree Syntex error: no distance specified after the colon");
		}

		if (Double.parseDouble(tktmp.getValue()) > 0.0) {
		    parent1.setDistance(Double.parseDouble(tktmp.getValue()));
		} else {
		    parent1.setDistance(0.0d);
		}

		parent1.setWithDistance(true);
		break;
	    }
	    case NwkToken.TOKEN_TYPE_COMMA: {
		break;
	    }
	    case NwkToken.TOKEN_TYPE_STRING: {
		if (st.isEmpty()) {
		    throw new Exception(
			    "NWK Tree Syntex error: no beginning left parenthesis.");
		}
		parent = st.peek();
		if (parent == null) {
		    throw new Exception(
			    "NWK Tree Syntex error: no beginning left parenthesis.");
		}
		treeNode = new TreeNode();
		treeNode.setName(tk.getValue());
		treeNode.setType(TreeNode.TREE_NODE_TYPE_TERMINAL);
		MyID = MyID + 1;
		treeNode.setId(MyID);
		parent1 = treeNode;
		parent.addChild(treeNode);
		break;
	    }
	    }
	    tk = nwktokenizer.nextToken();
	}
	st.clear();
	return root;
    }
}
