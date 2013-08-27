package br.usp.icmc.gridm.common;

public class GlobalConfiguration 
{
	//porta padrao para escutar requisições de jobs chegando
	private static int listenIncomingJobsPort = 12399;
	//porta padrao para enviar respostas dos jobs
	private static int sendReplyPort = 12400;
	//timeout padrao para read/write
	private static int rwTimeout = 10000;
	//timeout padrao para conectar a socket
	private static int connectTimeout = 10000;
	//threadpool size do pool de threads utilizado para conexões
	private static int threadPoolSize = 12;
	
	public static int getListenIncomingJobsPort() {
		return listenIncomingJobsPort;
	}
	public static void setListenIncomingJobsPort(int listenIncomingJobsPort) {
		GlobalConfiguration.listenIncomingJobsPort = listenIncomingJobsPort;
	}
	public static int getSendReplyPort() {
		return sendReplyPort;
	}
	public static void setSendReplyPort(int sendReplyPort) {
		GlobalConfiguration.sendReplyPort = sendReplyPort;
	}
	public static int getRwTimeout() {
		return rwTimeout;
	}
	public static void setRwTimeout(int rwTimeout) {
		GlobalConfiguration.rwTimeout = rwTimeout;
	}
	public static int getConnectTimeout() {
		return connectTimeout;
	}
	public static void setConnectTimeout(int connectTimeout) {
		GlobalConfiguration.connectTimeout = connectTimeout;
	}
	public static int getThreadPoolSize() {
		return threadPoolSize;
	}
	public static void setThreadPoolSize(int threadPoolSize) {
		GlobalConfiguration.threadPoolSize = threadPoolSize;
	}

	
}
