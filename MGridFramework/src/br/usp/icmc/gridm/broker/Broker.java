package br.usp.icmc.gridm.broker;

import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;

import br.usp.icmc.gridm.common.GlobalConfiguration;
import br.usp.icmc.gridm.common.GridJob;
import br.usp.icmc.gridm.common.GridJobId;
import br.usp.icmc.gridm.common.GridJobReply;

public class Broker 
{
	//Utilizado por padrão
	private Scheduler scheduler = new FifoScheduler();
	//Utilizado por padrão
	private DiscoveryService discoveryService = new HardcodedDiscoveryService();
	//Jobs a serem dispachados
	private ArrayList<GridJob> jobsToDispatch;
	//Respostas dos jobs
	private ArrayList<GridJobReply> replies = new ArrayList<GridJobReply>();
	//Programação sincrona
	private Object sinc = new Object();
	ServerSocket recieveReplySocket;
	
	public void dispatchJobs(int timeout, GridJob ...jobs) throws Exception
	{
		ArrayList<GridJob> l = new ArrayList<GridJob>();
		l.addAll(Arrays.asList(jobs));
		dispatchJobs(l, timeout);
	}
	
	public void dispatchJobs(ArrayList<GridJob> jobs, int timeout) throws Exception
	{
		synchronized(sinc) {
			Logger.getLogger("Log").info("Bind na socket reply");
			recieveReplySocket = new ServerSocket(GlobalConfiguration.getSendReplyPort());
			Logger.getLogger("Log").info("Dispatch jobs iniciado...");
			this.jobsToDispatch = jobs;
			if(discoveryService.getAvailableNodesAddresses() == null || 
					discoveryService.getAvailableNodesAddresses().length == 0)
				throw new IllegalArgumentException("Nenhum nó localizado pelo serviço de descoberta de nós");
			startListenningToReplyThread();
			int i=0;
			GridJobId[] ga = new GridJobId[jobsToDispatch.size()];
			Logger.getLogger("Log").info(ga.length+" jobs para serem enviados.");
			for(GridJob j : jobsToDispatch)
			{
				GridJobId g = new GridJobId();
				g.setGridJob(j);
				g.setJobId(i);
				ga[i] = g;
				i++;
			}
			String[] sa = discoveryService.getAvailableNodesAddresses();
			InetSocketAddress[] addrs = new InetSocketAddress[sa.length];
			int jj=0;
			for(String s : sa)
			{
				addrs[jj] = new InetSocketAddress(s, GlobalConfiguration.getListenIncomingJobsPort());
				jj++;
			}
			Logger.getLogger("Log").info("Chamando escalonador para enviar "+ga.length+" jobs");
			scheduler.schedule(ga, addrs);
			Logger.getLogger("Log").info("Bloqueado em sinc");
			sinc.wait(timeout);
			Logger.getLogger("Log").info("Desbloqueado em sinc");
		}
	}

	private void startListenningToReplyThread() 
	{
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				try
				{
					Logger.getLogger("Log").info("T Iniciado thread que escuta as requisições de resposta....");
					while(true)
					{
						Logger.getLogger("Log").info("T Bloqueado em Accept...");
						Socket s = recieveReplySocket.accept();
						Logger.getLogger("Log").info("T Recebido uma conexão...");
						ObjectInputStream i = new ObjectInputStream(s.getInputStream());
						ArrayList<GridJobReply> gi = (ArrayList<GridJobReply>) i.readObject();
						Logger.getLogger("Log").info("T Adicionando objeto na lista de resposta...");
						replies.addAll(gi);
						//Se ja recebeu a resposta de todos os jobs para
						Logger.getLogger("Log").info("T Jobs esperados: "+jobsToDispatch.size());
						Logger.getLogger("Log").info("T Jobs recebidos ate agora (incluindo esse): "+replies.size());
						if(replies.size() >= jobsToDispatch.size())
						{
							Logger.getLogger("Log").info("T Todos os jobs esperados ja foram recebidos");
							//Ordena as replies
							Collections.sort(replies, new Comparator<GridJobReply>(){

								@Override
								public int compare(GridJobReply o1,
										GridJobReply o2) {
									if( o1.getJobId() < o2.getJobId() )
										return -1;
									else if( o1.getJobId() == o2.getJobId() )
										return 0;
									return 1;
								}
								
							});
							//Desbloqueia a thread que chamou o método dispatchJobs()
							Logger.getLogger("Log").info("T Notificando thread de espera");
							synchronized(sinc) {
								sinc.notifyAll();
							}
							Logger.getLogger("Log").info("T Pronto!");
							break;
						}
					}
					Logger.getLogger("Log").info("T Saindo da thread de escuta...");
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			
		});
		t.start();
	}
	
	
	public ArrayList<GridJobReply> getReplies() {
		return replies;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public DiscoveryService getDiscoveryService() {
		return discoveryService;
	}

	public void setDiscoveryService(DiscoveryService discoveryService) {
		this.discoveryService = discoveryService;
	}
}
