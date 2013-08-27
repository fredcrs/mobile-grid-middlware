package br.usp.icmc.gridm.broker;

import java.net.InetSocketAddress;

import br.usp.icmc.gridm.common.GridJobId;

public interface Scheduler 
{
	public void schedule(GridJobId[] jobs, InetSocketAddress[] hosts) throws Exception;
}
