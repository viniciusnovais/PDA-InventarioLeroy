package com.pda.inventario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.pda_inventario.R;
import com.pda.inventario.businessComponent.ColetaBC;
import com.pda.inventario.businessComponent.DepartamentoBC;
import com.pda.inventario.businessComponent.EnderecoBC;
import com.pda.inventario.businessComponent.SetorBC;
import com.pda.inventario.businessComponent.UsuarioColetorBC;
import com.pda.inventario.entityObject.ColetaEO;
import com.pda.inventario.entityObject.ContagemColetorEO;
import com.pda.inventario.entityObject.DepartamentoColetorEO;
import com.pda.inventario.entityObject.EnderecoColetorEO;
import com.pda.inventario.entityObject.InventarioColetorEO;
import com.pda.inventario.entityObject.InventarioEO;
import com.pda.inventario.entityObject.SetorColetorEO;
import com.pda.inventario.entityObject.UsuarioColetorEO;
import com.pda.inventario.entityObject.UsuarioEO;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PrincipalActivity extends Activity {	
	final int ATIVIDADE_CONTAGEM = 2;
	final int ATIVIDADE_AUDITORIA = 3;
	final int ATIVIDADE_DIVERGENCIA = 5;
	final int ATIVIDADE_FINAL = 4;
	
	UsuarioEO objUsuarioLogado = new UsuarioEO();
	InventarioEO objInventario = new InventarioEO();
	
	Button btnContagem, btnExportacao, btnImportacao, btnAuditoria, btnDivergencia, btnFinal;
	TextView tvPendentes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.principal_novo);
	    
	    Intent intent = getIntent();
		objUsuarioLogado = (UsuarioEO)intent.getSerializableExtra("UsuarioEO");
		objInventario = (InventarioEO)intent.getSerializableExtra("InventarioEO");
		
		tvPendentes = (TextView)findViewById(R.id.tvPendentes);
		
		btnContagem = (Button)findViewById(R.id.btnContagem);
		btnAuditoria = (Button)findViewById(R.id.btnAuditoria);
		btnDivergencia = (Button)findViewById(R.id.btnDivergencia);
		btnFinal = (Button)findViewById(R.id.btnContFinal);
		btnExportacao = (Button)findViewById(R.id.btnExportacao);
		btnImportacao = (Button)findViewById(R.id.btnImportacao);
		
		btnContagem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					
					Intent intent = new Intent(PrincipalActivity.this, ContagemActivity.class);
					intent.putExtra("UsuarioEO",objUsuarioLogado);
					getIntent().getSerializableExtra("UsuarioEO");
					
					intent.putExtra("InventarioEO",objInventario);
					getIntent().getSerializableExtra("InventarioEO");	
					
					intent.putExtra("TIPO_ATIVIDADE",ATIVIDADE_CONTAGEM);
					getIntent().getSerializableExtra("TIPO_ATIVIDADE");		
					
					startActivity(intent);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnExportacao.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					
					AsyncCallWS task = new AsyncCallWS();
					task.execute();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnImportacao.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					AsyncCallWSImport task = new AsyncCallWSImport();
					task.execute();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnFinal.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					
					Intent intent = new Intent(PrincipalActivity.this, ContagemActivity.class);
					intent.putExtra("UsuarioEO",objUsuarioLogado);
					getIntent().getSerializableExtra("UsuarioEO");
					
					intent.putExtra("InventarioEO",objInventario);
					getIntent().getSerializableExtra("InventarioEO");	
					
					intent.putExtra("TIPO_ATIVIDADE",ATIVIDADE_FINAL);
					getIntent().getSerializableExtra("TIPO_ATIVIDADE");		
					
					startActivity(intent);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnAuditoria.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					
					Intent intent = new Intent(PrincipalActivity.this, ContagemActivity.class);
					intent.putExtra("UsuarioEO",objUsuarioLogado);
					getIntent().getSerializableExtra("UsuarioEO");
					
					intent.putExtra("InventarioEO",objInventario);
					getIntent().getSerializableExtra("InventarioEO");	
					
					intent.putExtra("TIPO_ATIVIDADE",ATIVIDADE_AUDITORIA);
					getIntent().getSerializableExtra("TIPO_ATIVIDADE");		
					
					startActivity(intent);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnDivergencia.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					
					Intent intent = new Intent(PrincipalActivity.this, ContagemActivity.class);
					intent.putExtra("UsuarioEO",objUsuarioLogado);
					getIntent().getSerializableExtra("UsuarioEO");
					
					intent.putExtra("InventarioEO",objInventario);
					getIntent().getSerializableExtra("InventarioEO");	
					
					intent.putExtra("TIPO_ATIVIDADE",ATIVIDADE_DIVERGENCIA);
					getIntent().getSerializableExtra("TIPO_ATIVIDADE");		
					
					startActivity(intent);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		this.verificaPendentes();		
	}
	
	@Override
	public void onResume() {
	 super.onResume();
	 this.verificaPendentes();
	}
	
	private class AsyncCallWS extends AsyncTask<Void, Integer, Void> {
		private final ProgressDialog dialog = new ProgressDialog(PrincipalActivity.this);

		@Override
		protected void onPreExecute() {
			Log.i("Response", "onPreExecute");
			this.dialog.setMessage(StringUtils.EXPORT);
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.i("Response", "doInBackground");
			setContagem();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i("Response", "onPostExecute");
			Toast.makeText(getApplicationContext(), StringUtils.EXPORT_OK, Toast.LENGTH_LONG).show();
			verificaPendentes();
			this.dialog.dismiss();
		}
	}	
	
	public void setContagem() {

		String SOAP_ACTION = "http://tempuri.org/SetColeta";
		String METHOD_NAME = "SetColeta";
		String NAMESPACE = "http://tempuri.org/";
		//String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
		String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/inventario.asmx";
		
		try {
			ColetaBC repository = new ColetaBC(this);
			SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
			List<ContagemColetorEO> obj = new ArrayList<ContagemColetorEO>();

			obj = repository.GetContagemColetorPendente(String.valueOf(objInventario.getIdInventario()));

			SoapObject soapEntity = new SoapObject(NAMESPACE, "entity");

			for (ContagemColetorEO i : obj){
				soapEntity.addProperty("ContagemColetorEO", i);
			}
			Request.addSoapObject(soapEntity);

			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			soapEnvelope.dotNet = true;
			soapEnvelope.setOutputSoapObject(Request);

			MarshalDate md = new MarshalDate();
			md.register(soapEnvelope);

			soapEnvelope.addMapping(NAMESPACE, "ContagemColetorEO",new ContagemColetorEO().getClass());

			HttpTransportSE transport = new HttpTransportSE(URL);

			transport.call(SOAP_ACTION, soapEnvelope);

			SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

			repository.UpdateColetaExportInventario(String.valueOf(objInventario.getIdInventario()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void verificaPendentes(){
		try {
			ColetaBC repository = new ColetaBC(this);
			List<ColetaEO> objColetaList = new ArrayList<ColetaEO>();
			
			objColetaList = repository.GetColetaPendente(String.valueOf(objInventario.getIdInventario()));
			
			if (!objColetaList.isEmpty()){
				tvPendentes.setText(StringUtils.COLETA_PENDENTES + String.valueOf(objColetaList.size()));
				tvPendentes.setTextColor(Color.parseColor("#FF0000"));
			}else{
				tvPendentes.setText(StringUtils.COLETA_PENDENTES);
				tvPendentes.setTextColor(Color.parseColor("#FFFFFF"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class AsyncCallWSImport extends AsyncTask<Void, Integer, Void> {
		private final ProgressDialog dialog = new ProgressDialog(PrincipalActivity.this);

		@Override
		protected void onPreExecute() {
			Log.i("Response", "onPreExecute");
			this.dialog.setMessage(StringUtils.IMPORT);
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.i("Response", "doInBackground");
			getImport();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i("Response", "onPostExecute");
			Toast.makeText(getApplicationContext(), StringUtils.IMPORT_OK, Toast.LENGTH_LONG).show();
			this.dialog.dismiss();
		}
	}
	
	private void getImport(){
		try {
			getUsers();
			getDepto();
			getSetor();
			getEndereco();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getUsers() {

		String SOAP_ACTION = "http://tempuri.org/GetUsuarioColetor";
		String METHOD_NAME = "GetUsuarioColetor";
		String NAMESPACE = "http://tempuri.org/";
		//String URL = "http://179.184.159.52/wsandroid/autenticacao.asmx";
		String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/autenticacao.asmx";
		
		try {
			SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
			InventarioColetorEO obj = new InventarioColetorEO();

			PropertyInfo pi = new PropertyInfo();
			pi.setName("obj");
			pi.setValue(obj);
			pi.setType(obj.getClass());

			Request.addProperty(pi);

			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			soapEnvelope.dotNet = true;
			soapEnvelope.setOutputSoapObject(Request);

			MarshalDate md = new MarshalDate();
			md.register(soapEnvelope);

			soapEnvelope.addMapping(NAMESPACE, "InventarioColetorEO",new InventarioColetorEO().getClass());

			HttpTransportSE transport = new HttpTransportSE(URL);

			transport.call(SOAP_ACTION, soapEnvelope);

			SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

			if (objSoapList != null) {
				List<UsuarioColetorEO> objUsuarioList = new ArrayList<UsuarioColetorEO>();
				for (int i = 0; i < objSoapList.getPropertyCount(); i++)
				{
					SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
					UsuarioColetorEO objUsuario = new UsuarioColetorEO();

					objUsuario.setCodigo(Integer.parseInt(objSoap.getPropertyAsString("Codigo")));
					objUsuario.setLogin(objSoap.getPropertyAsString("Login"));
					objUsuario.setLider(Integer.parseInt(objSoap.getPropertyAsString("Lider")));
					objUsuario.setSenha(objSoap.getPropertyAsString("Senha").getBytes());

					objUsuarioList.add(objUsuario);					
				}

				if (!objUsuarioList.isEmpty()){
					UsuarioColetorBC repository = new UsuarioColetorBC(this);
					repository.CreateUsuarioList(objUsuarioList);
				}
			} else
				Log.i("WS Call", "Return null");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getDepto() {

		String SOAP_ACTION = "http://tempuri.org/GetDepartamento";
		String METHOD_NAME = "GetDepartamento";
		String NAMESPACE = "http://tempuri.org/";
		//String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";		
		String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsinventario.asmx";
		
		try {
			SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
			DepartamentoColetorEO obj = new DepartamentoColetorEO();
			obj.IdInventario = objInventario.getIdInventario();

			PropertyInfo pi = new PropertyInfo();
			pi.setName("entity");
			pi.setValue(obj);
			pi.setType(obj.getClass());		

			Request.addProperty(pi);

			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			soapEnvelope.dotNet = true;
			soapEnvelope.setOutputSoapObject(Request);

			MarshalDate md = new MarshalDate();
			md.register(soapEnvelope);

			soapEnvelope.addMapping(NAMESPACE, "DepartamentoColetorEO",new DepartamentoColetorEO().getClass());

			HttpTransportSE transport = new HttpTransportSE(URL);

			transport.call(SOAP_ACTION, soapEnvelope);

			SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

			if (objSoapList != null) {
				List<DepartamentoColetorEO> objDepartamentoList = new ArrayList<DepartamentoColetorEO>();
				for (int i = 0; i < objSoapList.getPropertyCount(); i++)
				{
					SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
					DepartamentoColetorEO objDepartamento = new DepartamentoColetorEO();

					objDepartamento.setProperty(0, Integer.parseInt(objSoap.getPropertyAsString("IdInventario")));
					objDepartamento.setProperty(1, Integer.parseInt(objSoap.getPropertyAsString("IdDepartamento")));
					objDepartamento.setProperty(2, Integer.parseInt(objSoap.getPropertyAsString("IdMetodoContagem")));
					objDepartamento.setProperty(3, Integer.parseInt(objSoap.getPropertyAsString("IdMetodoAuditoria")));
					objDepartamento.setProperty(4, Integer.parseInt(objSoap.getPropertyAsString("IdMetodoLeitura")));
					objDepartamento.setProperty(5, objSoap.getPropertyAsString("Departamento"));
					objDepartamento.setProperty(6, objSoap.getPropertyAsString("MetodoContagem"));
					objDepartamento.setProperty(7, objSoap.getPropertyAsString("MetodoAuditoria"));
					objDepartamento.setProperty(8, Integer.parseInt(objSoap.getPropertyAsString("Quantidade")));

					objDepartamentoList.add(objDepartamento);					
				}

				if (!objDepartamentoList.isEmpty()){
					DepartamentoBC repository = new DepartamentoBC(this);
					repository.CreateDepartamentoColetorList(objDepartamentoList);
				}
			} else
				Log.i("WS Call", "Return null");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getSetor() {

		String SOAP_ACTION = "http://tempuri.org/GetSetor";
		String METHOD_NAME = "GetSetor";
		String NAMESPACE = "http://tempuri.org/";
		//String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
		String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsinventario.asmx";
		
		try {
			SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
			SetorColetorEO obj = new SetorColetorEO();
			obj.IdInventario = objInventario.getIdInventario();

			PropertyInfo pi = new PropertyInfo();
			pi.setName("entity");
			pi.setValue(obj);
			pi.setType(obj.getClass());		

			Request.addProperty(pi);

			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			soapEnvelope.dotNet = true;
			soapEnvelope.setOutputSoapObject(Request);

			MarshalDate md = new MarshalDate();
			md.register(soapEnvelope);

			soapEnvelope.addMapping(NAMESPACE, "SetorColetorEO",new SetorColetorEO().getClass());

			HttpTransportSE transport = new HttpTransportSE(URL);

			transport.call(SOAP_ACTION, soapEnvelope);

			SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

			if (objSoapList != null) {
				List<SetorColetorEO> objSetorList = new ArrayList<SetorColetorEO>();
				for (int i = 0; i < objSoapList.getPropertyCount(); i++)
				{
					SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
					SetorColetorEO objSetor = new SetorColetorEO();

					objSetor.IdInventario = Integer.parseInt(objSoap.getPropertyAsString("IdInventario").toString());
					objSetor.IdDepartamento = Integer.parseInt(objSoap.getPropertyAsString("IdDepartamento").toString());
					objSetor.IdSetor = Integer.parseInt(objSoap.getPropertyAsString("IdSetor").toString());
					objSetor.IdMetodoContagem = Integer.parseInt(objSoap.getPropertyAsString("IdMetodoContagem").toString());
					objSetor.IdMetodoAuditoria = Integer.parseInt(objSoap.getPropertyAsString("IdMetodoAuditoria").toString());
					objSetor.IdMetodoLeitura = Integer.parseInt(objSoap.getPropertyAsString("IdMetodoLeitura").toString());
					objSetor.Setor = objSoap.getPropertyAsString("Setor").toString();
					objSetor.MetodoContagem = objSoap.getPropertyAsString("MetodoContagem").toString();
					objSetor.MetodoAuditoria = objSoap.getPropertyAsString("MetodoAuditoria").toString();
					objSetor.Quantidade = Integer.parseInt(objSoap.getPropertyAsString("Quantidade").toString());

					objSetorList.add(objSetor);					
				}

				if (!objSetorList.isEmpty()){
					SetorBC repository = new SetorBC(this);
					repository.CreateSetorColetorList(objSetorList);
				}
			} else
				Log.i("WS Call", "Return null");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getEndereco() {

		String SOAP_ACTION = "http://tempuri.org/GetEndereco";
		String METHOD_NAME = "GetEndereco";
		String NAMESPACE = "http://tempuri.org/";
		//String URL = "http://179.184.159.52/wsandroid/wsinventario.asmx";
		String URL = "http://" + StringUtils.SERVIDOR + "/" + StringUtils.DIRETORIO_VIRTUAL + "/wsinventario.asmx";
		
		try {
			SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
			EnderecoColetorEO obj = new EnderecoColetorEO();
			obj.IdInventario = objInventario.getIdInventario();

			PropertyInfo pi = new PropertyInfo();
			pi.setName("entity");
			pi.setValue(obj);
			pi.setType(obj.getClass());		

			Request.addProperty(pi);

			SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			soapEnvelope.implicitTypes = true;
			soapEnvelope.dotNet = true;
			soapEnvelope.setOutputSoapObject(Request);

			MarshalDate md = new MarshalDate();
			md.register(soapEnvelope);

			soapEnvelope.addMapping(NAMESPACE, "EnderecoColetorEO",new EnderecoColetorEO().getClass());

			HttpTransportSE transport = new HttpTransportSE(URL);

			transport.call(SOAP_ACTION, soapEnvelope);

			SoapObject objSoapList = (SoapObject) soapEnvelope.getResponse();

			if (objSoapList != null) {
				List<EnderecoColetorEO> objEnderecoList = new ArrayList<EnderecoColetorEO>();
				for (int i = 0; i < objSoapList.getPropertyCount(); i++)
				{
					SoapObject objSoap = (SoapObject) objSoapList.getProperty(i);
					EnderecoColetorEO objEndereco = new EnderecoColetorEO();

					objEndereco.IdInventario = Integer.parseInt(objSoap.getPropertyAsString("IdInventario").toString());
					objEndereco.IdEndereco = Integer.parseInt(objSoap.getPropertyAsString("IdEndereco").toString());
					objEndereco.IdDepartamento = Integer.parseInt(objSoap.getPropertyAsString("IdDepartamento").toString());
					objEndereco.IdSetor = Integer.parseInt(objSoap.getPropertyAsString("IdSetor").toString());
					objEndereco.IdMetodoContagem = Integer.parseInt(objSoap.getPropertyAsString("IdMetodoContagem").toString());
					objEndereco.IdMetodoAuditoria = Integer.parseInt(objSoap.getPropertyAsString("IdMetodoAuditoria").toString());
					objEndereco.IdMetodoLeitura = Integer.parseInt(objSoap.getPropertyAsString("IdMetodoLeitura").toString());
					objEndereco.Endereco = objSoap.getPropertyAsString("Endereco").toString();
					objEndereco.Departamento = objSoap.getPropertyAsString("Departamento").toString();
					objEndereco.Setor = objSoap.getPropertyAsString("Setor").toString();
					objEndereco.MetodoContagem = objSoap.getPropertyAsString("MetodoContagem").toString();
					objEndereco.MetodoAuditoria = objSoap.getPropertyAsString("MetodoAuditoria").toString();
					objEndereco.Quantidade = Integer.parseInt(objSoap.getPropertyAsString("Quantidade").toString());					
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					try {
						objEndereco.DataHora = format.parse(objSoap.getPropertyAsString("DataHora").toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}

					objEnderecoList.add(objEndereco);					
				}

				if (!objEnderecoList.isEmpty()){
					EnderecoBC repository = new EnderecoBC(this);
					repository.CreateEnderecoList(objEnderecoList);
				}
			} else
				Log.i("WS Call", "Return null");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
