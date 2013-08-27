package br.usp.icmc.mgridtest;

import java.util.ArrayList;
import java.util.logging.Logger;

import br.usp.icmc.gridm.broker.Broker;
import br.usp.icmc.gridm.broker.DiscoveryService;
import br.usp.icmc.gridm.broker.HardcodedDiscoveryService;
import br.usp.icmc.gridm.common.GridJob;
import br.usp.icmc.gridm.common.GridJobReply;
import br.usp.icmc.gridm.resource.Resource;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	final Broker bro = new Broker();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Resource r = new Resource();
		try
		{
			r.start();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner s = (Spinner) findViewById(R.id.s1);
		s.setAdapter(spinnerAdapter);
		//spinnerAdapter.add("10.62.8.205");
		spinnerAdapter.add("127.0.0.1");
		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				EditText e = (EditText) findViewById(R.id.e1);
				spinnerAdapter.add(e.getText().toString());
			}
		});
		
		
		Button d = (Button) findViewById(R.id.dispatch);
		d.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				String[] st = new String[spinnerAdapter.getCount()];
				for(int i=0; i<spinnerAdapter.getCount(); i++)
				{
					st[i] = spinnerAdapter.getItem(i);
				}
				HardcodedDiscoveryService hds = new HardcodedDiscoveryService();
				hds.setNodes(st);
				bro.setDiscoveryService(hds);
				AsyncTask<Void, Void, Object> task = new AsyncTask<Void, Void, Object>(){

					@Override
					protected Object doInBackground(Void... params)
					{						
						ArrayList<GridJob> js = new ArrayList<GridJob>();
						//Matrix multiplication
						
						Integer [][][] matrix = new Integer[][][]{{{100,1,1},{1,1,1},{1,1,1}}, {{1,1,1},{1,1,1},{1,1,1}}, {{1,1,1},{1,1,1},{1,1,1}}, {{1,1,1},{1,1,1},{1,1,1}}};
						//Integer [][][] matrix2 = new Integer[][][]{{{100,100,100},{1,1,1},{1,1,1}}, {{1,1,1},{1,1,1},{1,1,1}}, {{1,1,1},{1,1,1},{1,1,1}}, {{1,1,1},{1,1,1},{1,1,1}}};
						
						MatrixMultiplication2 m = new MatrixMultiplication2(matrix);
						//MatrixMultiplication2 m2 = new MatrixMultiplication2(matrix2);
							
						js.add(m);
					    //js.add(m2);
						try
						{
							System.out.println("-Chamando dipatch jobs");
							bro.dispatchJobs(js, 50000);
							System.out.println("Retornado!");
							return bro;
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
							return null;
						}
					}
					
					protected void onPostExecute(Object object)
					{
						try
						{
							if(object != null)
							{
								ArrayList<GridJobReply> r = bro.getReplies();
								//Toast.makeText(getApplicationContext(), r.toArray().toString(), Toast.LENGTH_LONG).show();
								
								System.out.println("--------- RESULTADO  ---------"+r.size());
								int i=0;
								for(GridJobReply re : r)
								{
									Integer[][][] m = (Integer[][][]) re.getReply();
									for (Integer[][] mm : m) 
									{
										for(int ii=0; ii< mm.length; ii++)
										{
											for(int j=0; j< mm[ii].length; j++)
											{
												System.out.print(mm[ii][j]+" ");
											}
											System.out.println("");
										}
										System.out.println("");System.out.println("");
									}
								}
								System.out.println("--------- RESULTADO  ---------"+r.size());
								
							}
							else
							{
								Toast.makeText(getApplicationContext(), "ERRO", Toast.LENGTH_LONG).show();
							}
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
					
				};
				task.execute();
				System.out.println("Invocado task.execute()");
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
