package br.usp.icmc.gridm.common;

import java.io.Serializable;

public class GridJobId implements Serializable
{
	private int jobId = -1;
	private GridJob gridJob = null;
	
	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public GridJob getGridJob() {
		return gridJob;
	}

	public void setGridJob(GridJob gridJob) {
		this.gridJob = gridJob;
	}
	
	
}
