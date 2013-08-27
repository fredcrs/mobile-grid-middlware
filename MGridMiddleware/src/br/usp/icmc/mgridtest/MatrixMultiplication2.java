package br.usp.icmc.mgridtest;

import br.usp.icmc.gridm.common.GridJob;

public class MatrixMultiplication2 implements GridJob
{
	private Integer[][][] matrix;
	
	public MatrixMultiplication2(Integer[][][] m)
	{
		matrix = m;
	}
	
	@Override
	public Object execute() throws Exception 
	{
		Integer[][][] newMatrix = new Integer[this.matrix.length][3][3];
		
		int countMatrix = 0;
		for (Integer[][] m : this.matrix) {
			Integer[][] newM = new Integer[3][3];
			
			int v = 0;
			for(int r = 0; r < m.length; r++){
				for(int c = 0; c < m[r].length; c++){
					v = m[r][0] * m[0][c] + m[r][1]*m[1][c] + m[r][2] *m[2][c];
					newM[r][c] = v;
				}
			}
			newMatrix[countMatrix++] = newM;	
		}
		
		return newMatrix;
		
	}
	
	

}
