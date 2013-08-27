package br.usp.icmc.gridm.common;

import java.io.Serializable;

public class GridJobReply implements Serializable
{
	private int jobId = -1;
	private Object reply = null;
	
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public Object getReply() {
		return reply;
	}
	public void setReply(Object reply) {
		this.reply = reply;
	}
	
	
}
