package org.nescent.geophylobuilder.parser;

public class NwkToken {
    int type;
    String value;
    public final static int TOKEN_TYPE_RIGHT_PARENTHESIS = 0;
    public final static int TOKEN_TYPE_LEFT_PARENTHESIS = 1;
    public final static int TOKEN_TYPE_COMMA = 2;
    public final static int TOKEN_TYPE_COLON = 3;
    public final static int TOKEN_TYPE_STRING = 4;
    public final static int TOKEN_TYPE_NUMBER = 5;
    public final static int TOKEN_TYPE_END = 6;
    public final static int TOKEN_TYPE_NULL = 7;

    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
