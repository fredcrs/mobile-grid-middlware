package br.usp.icmc.mgridtest;

import br.usp.icmc.gridm.common.GridJob;

public class MatrixMultiplicationJob implements GridJob
{
	private Double[] row;
	private Double[] column;
	
	public MatrixMultiplicationJob(Double[] row, Double[] column)
	{
		if(row.length != column.length)
			throw new RuntimeException("Linha de dimensão diferente de coluna");
		this.row = row;
		this.column = column;
	}

	@Override
	public Object execute() throws Exception 
	{
		Double s = 0d;
		for(int i=0; i< row.length; i++)
		{
			s += row[i]*column[i];
		}
		return s;
	}
}
