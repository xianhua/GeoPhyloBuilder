package org.nescent.geophylobuilder.parser;

public class NwkTokenizer {
    String nwkstring;
    int curPos = 0;
    NwkToken prevToken = null;

    public NwkTokenizer() {
	super();
    }

    public NwkTokenizer(String nwkstring) {
	super();
	this.nwkstring = nwkstring;
    }

    public String getNwkstring() {
	return nwkstring;
    }

    public void setNwkstring(String nwkstring) {
	this.nwkstring = nwkstring;
    }

    public int getCurPos() {
	return curPos;
    }

    public void setCurPos(int curPos) {
	this.curPos = curPos;
    }

    public NwkToken getPrevToken() {
	return prevToken;
    }

    public void setPrevToken(NwkToken prevToken) {
	this.prevToken = prevToken;
    }

    public NwkToken nextToken() {

	char myChar;
	NwkToken tk;

	if (curPos >= nwkstring.length()) {
	    tk = new NwkToken();
	    tk.setType(NwkToken.TOKEN_TYPE_NULL);
	    return tk;
	}

	myChar = nwkstring.charAt(curPos);
	if ((myChar == ' ')) {
	    curPos = curPos + 1;
	    myChar = nwkstring.charAt(curPos);
	    while (myChar == ' ') {
		curPos = curPos + 1;
		if (curPos >= nwkstring.length()) {
		    tk = new NwkToken();
		    tk.setType(NwkToken.TOKEN_TYPE_NULL);
		    return tk;
		}
		myChar = nwkstring.charAt(curPos);
	    }
	}

	if (myChar == '(') {
	    tk = new NwkToken();
	    tk.setType(NwkToken.TOKEN_TYPE_LEFT_PARENTHESIS);
	    curPos = curPos + 1;
	    prevToken = tk;
	    return tk;
	} else if (myChar == ')') {
	    tk = new NwkToken();
	    tk.setType(NwkToken.TOKEN_TYPE_RIGHT_PARENTHESIS);
	    curPos = curPos + 1;
	    prevToken = tk;
	    return tk;
	} else if (myChar == ':') {
	    tk = new NwkToken();
	    tk.setType(NwkToken.TOKEN_TYPE_COLON);
	    curPos = curPos + 1;
	    prevToken = tk;
	    return tk;
	} else if (myChar == ',') {
	    tk = new NwkToken();
	    tk.setType(NwkToken.TOKEN_TYPE_COMMA);
	    curPos = curPos + 1;
	    prevToken = tk;
	    return tk;
	} else if (myChar == ';') {
	    tk = new NwkToken();
	    tk.setType(NwkToken.TOKEN_TYPE_END);
	    curPos = curPos + 1;
	    prevToken = tk;
	    return tk;
	} else {
	    String s = String.valueOf(myChar);
	    curPos = curPos + 1;
	    myChar = nwkstring.charAt(curPos);
	    while (myChar != '(' && myChar != ')' && myChar != ':'
		    && myChar != ',' && curPos < nwkstring.length()) {
		s = s + myChar;
		curPos = curPos + 1;
		if (curPos < nwkstring.length()) {
		    myChar = nwkstring.charAt(curPos);
		}
	    }

	    s = s.trim();
	    if (!s.equals("")) {
		tk = new NwkToken();
		if (prevToken.getType() == NwkToken.TOKEN_TYPE_COLON) {
		    tk.setType(NwkToken.TOKEN_TYPE_NUMBER);
		} else {
		    tk.setType(NwkToken.TOKEN_TYPE_STRING);
		}
		tk.setValue(s);
		prevToken = tk;
		return tk;
	    } else {
		tk = new NwkToken();
		tk.setType(NwkToken.TOKEN_TYPE_NULL);
		prevToken = tk;
		return tk;
	    }
	}
    }

}
