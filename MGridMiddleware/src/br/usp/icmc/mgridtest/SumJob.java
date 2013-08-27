package br.usp.icmc.mgridtest;

import br.usp.icmc.gridm.common.GridJob;

public class SumJob implements GridJob
{
	private Integer[] ints = null;
	
	
	public SumJob(Integer ...i)
	{
		ints = i;
	}
	
	@Override
	public Object execute() throws Exception {
		Integer sum = 0;
		for(int i=0; i<ints.length; i++)
		{
			sum+=ints[i];
		}
		return sum;
	}

}
