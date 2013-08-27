package br.usp.icmc.gridm.common;

import java.io.Serializable;

public interface GridJob extends Serializable
{
	public Object execute() throws Exception;
}
