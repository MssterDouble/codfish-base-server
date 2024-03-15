package org.com.codfish.servlet;

public class ErrReturnObj {
	private String RETCODE = null;
	private String RETMSG = null;
    public void setRetcode(String retcode) {
        this.RETCODE = retcode;
    }
    public String getRetcode() {
        return RETCODE;
    }
    public void setRetmsg(String retmsg) {
        this.RETMSG = retmsg;
    }
    public String getRetmsg() {
        return RETMSG;
    }
}
