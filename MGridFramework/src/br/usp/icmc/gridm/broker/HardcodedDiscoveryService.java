package br.usp.icmc.gridm.broker;

public class HardcodedDiscoveryService implements DiscoveryService
{
	private String[] nodes;
	
	
	public void setNodes(String[] nodes) 
	{
		this.nodes = nodes;
	}


	@Override
	public String[] getAvailableNodesAddresses() throws Exception 
	{
		return nodes;
	}

}
