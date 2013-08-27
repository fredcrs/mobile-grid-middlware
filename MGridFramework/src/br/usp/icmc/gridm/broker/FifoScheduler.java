package br.usp.icmc.gridm.broker;

import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import br.usp.icmc.gridm.common.GridJobId;

public class FifoScheduler implements Scheduler
{

	@Override
	public void schedule(GridJobId[] jobs, InetSocketAddress[] hosts) throws Exception
	{
		Logger.getLogger("Log").info("S Escalonador enviando jobs para "+hosts.length+" hosts");
		int cjob = 0;
		int j = jobs.length / hosts.length;
		for(InetSocketAddress i : hosts)
		{
			if(cjob >= jobs.length)
				break;
			Socket s = new Socket();
			s.setSoTimeout(10000);
			Logger.getLogger("Log").info("S Conectando-se a "+i);
			s.connect(i);
			Logger.getLogger("Log").info("S Conectado!");
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ArrayList<GridJobId> suba = new ArrayList<GridJobId>();
			for(int c=0; c<j; c++)
			{
				Logger.getLogger("Log").info("S Adicionado 1 job para o array de: "+i);
				suba.add(jobs[cjob++]);
			}
			out.writeObject(suba);
			out.close();
			s.close();
			Logger.getLogger("Log").info("S Finalizado conexão com "+i);
		}
	}
	
}
