/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.gridm.resource;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import br.usp.icmc.gridm.common.GlobalConfiguration;
import br.usp.icmc.gridm.common.GridJobId;
import br.usp.icmc.gridm.common.GridJobReply;

/**
 * 
 *
 * @author fred
 */
public class Resource 
{
    private ServerSocket socket;
    private Executor service;
    private volatile boolean _RUNNING = false;
    public Resource() 
    {
        if(GlobalConfiguration.getThreadPoolSize() <= 0)
            service = Executors.newCachedThreadPool();
        else
            service = Executors.newFixedThreadPool(GlobalConfiguration.getThreadPoolSize());
    }
    
    public void start() throws Exception
    {
    	Logger.getLogger("Log").info("R Iniciando thread de resource");
        if(socket != null)
            socket.close();
        socket = new ServerSocket(GlobalConfiguration.getListenIncomingJobsPort());
        Logger.getLogger("Log").info("R Criado socket de escuta");
        _RUNNING = true;
        Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
		        while(_RUNNING)
		        {
		            try
		            {
		            	Logger.getLogger("Log").info("R Iniciado nova thread");
		            	Logger.getLogger("Log").info("R Bloqueado em accept");
		            	Socket s = socket.accept();
		            	s.setSoTimeout(GlobalConfiguration.getRwTimeout());
		            	Logger.getLogger("Log").info("R Nova conexão, processando...");
		            	processAndReply(s);
		            }
		            catch(Exception ex)
		            {
		                ex.printStackTrace();
		            }
		        }
			}
		});
        t.start();
    }
    
    public void stop() throws Exception
    {
    	_RUNNING = false;
    	if(socket != null)
    		socket.close();
    }
    
    //processa o objeto e responde com os dados para o broker
    private void processAndReply(final Socket s) throws Exception
    {
    	service.execute(new Runnable() {
			
			@Override
			public void run() {
				try
				{
					Logger.getLogger("Log").info("R2 Nova thread para ler da socket a conexão");
					ObjectInputStream in = new ObjectInputStream(s.getInputStream());
					ArrayList<GridJobId> jobs = (ArrayList<GridJobId>) in.readObject();
					ArrayList<GridJobReply> resp = new ArrayList<GridJobReply>();
					Logger.getLogger("Log").info("R2 Recebido "+jobs.size()+" jobs");
					for(GridJobId job : jobs)
					{
						GridJobReply r = new GridJobReply();
						r.setJobId(job.getJobId());
						r.setReply(job.getGridJob().execute());
						Logger.getLogger("Log").info("R2 Executado um job");
						resp.add(r);
					}
					Logger.getLogger("Log").info("R2 Iniciando nova socket para enviar resposta");
					Socket rs = new Socket();
					rs.setSoTimeout(GlobalConfiguration.getRwTimeout());
					Logger.getLogger("Log").info("R2 Conectando ao Broker");
					rs.connect(new InetSocketAddress(s.getInetAddress().getHostAddress(), 
							GlobalConfiguration.getSendReplyPort()), GlobalConfiguration.getConnectTimeout());
					ObjectOutputStream out = new ObjectOutputStream(rs.getOutputStream());
					Logger.getLogger("Log").info("R2 Enviando resposta...");
					out.writeObject(resp);
					out.close();
					Logger.getLogger("Log").info("R2 Enviado resposta!");
					rs.close();
					Logger.getLogger("Log").info("R2 Finalizado conexão!");
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
    }
}
