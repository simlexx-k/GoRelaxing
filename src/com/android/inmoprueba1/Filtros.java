package com.android.inmoprueba1;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.android.inmoprueba1.SeekBarWithTwoThumb.OnSeekBarWithTwoThumbChangeListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Filtros extends Resultados implements
		android.view.View.OnClickListener {

	Bundle reicieveParams;

	String ciudad;

	TextView filtrotipo, filtrocapacidad, filtrodormitorios, filtroprecio;
	Spinner filtrospinner;
	private List<String> listasp;
	EditText filtrointroprovincia;
	Button casa, piso, local, apartamento, chalet, capacidadmenos,
			capacidadmas, dormitoriomas, dormitoriomenos, aplifiltro;

	int huespedes = 1;
	int dormitorios = 1;

	int minVal = 0;
	int maxVal = 95;

	// datos a enviar para la busqueda
	String paisb = "";
	String provinciab = "";
	String tipob = "";
	String capacidadb = "";
	String dormitoriosb = "";
	String preciominb = "";
	String preciomaxb = "";

	// ////////////////////////////

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filtros);

		VariableAtras.setDato("filtros");// controlar boton atras telefono
		VariableAtras.setContador(3);

		System.out.println("Entrando en filtros");

		reicieveParams = getIntent().getExtras();// recibiendo paramentros de
													// busqueda

		langusado = reicieveParams.getString("langusado");

		ciudad = reicieveParams.getString("ciudad");// recibir que ciudad ha
													// sido seleccionada en
													// busqueda

		// saber si esta conectado
		conec = Logado.comprobarlog(this);

		/***************************
		 * PROGRAMACION CLASE
		 */
		filtrotipo = (TextView) findViewById(R.id.filtrotipo);
		filtrocapacidad = (TextView) findViewById(R.id.filtrocapacidad);
		filtrocapacidad.setText(huespedes + " "
				+ this.getString(R.string.huesped1));
		filtrodormitorios = (TextView) findViewById(R.id.filtrodormitorios);
		filtrodormitorios.setText(dormitorios + " "
				+ this.getString(R.string.dormi1));
		filtroprecio = (TextView) findViewById(R.id.filtroprecio);

		filtrospinner = (Spinner) findViewById(R.id.filtrospinner);

		filtrointroprovincia = (EditText) findViewById(R.id.filtrointroprovincia);

		casa = (Button) findViewById(R.id.casa);
		casa.setOnClickListener(this);
		piso = (Button) findViewById(R.id.piso);
		piso.setOnClickListener(this);
		local = (Button) findViewById(R.id.local);
		local.setOnClickListener(this);
		apartamento = (Button) findViewById(R.id.apartamento);
		apartamento.setOnClickListener(this);
		chalet = (Button) findViewById(R.id.chalet);
		chalet.setOnClickListener(this);

		capacidadmenos = (Button) findViewById(R.id.menoscapacidad);
		capacidadmenos.setOnClickListener(this);
		capacidadmas = (Button) findViewById(R.id.mascapacidad);
		capacidadmas.setOnClickListener(this);
		capacidadmenos.setEnabled(false);

		dormitoriomenos = (Button) findViewById(R.id.menosdormitorio);
		dormitoriomenos.setOnClickListener(this);
		dormitoriomas = (Button) findViewById(R.id.masdormitorio);
		dormitoriomas.setOnClickListener(this);
		dormitoriomenos.setEnabled(false);

		aplifiltro = (Button) findViewById(R.id.aplifiltro);
		aplifiltro.setOnClickListener(this);
		
		System.out.println("ciudad filtros: " + ciudad);
		
		try{
		
		if(!ciudad.equals("") && ciudad != null){//si viene de resultados rellenar el campo ciudad y pais
			
			int coma = ciudad.indexOf(",");
			System.out.println("coma: " + coma);
			
			if(coma > 0){//si se pasa poblacion y pais
				
				String city = ciudad.substring(0, coma);//extraer solo el nombre de la ciudad/provincia
				
				filtrointroprovincia.setText(city);
				
				int espacio = ciudad.indexOf("%20");
				System.out.println("espacio: " + espacio);
				String pa = ciudad.substring(espacio + 3, ciudad.length());//extraer solo el nombre del pais
				
				System.out.println("pais: " + pa);
				
				DatosSpinner(pa);
				
			}//end if
			else{//si solo se pasa poblacion
				
				filtrointroprovincia.setText(ciudad);
				DatosSpinner("");
				
			}//end else
			
			
		}//end if
		else{
			
			DatosSpinner("");
			
		}//end else
		}//end try
		catch(Exception e){
			
			
			System.out.println("petao sacar ciudad filtro: " + e);
		}//end catch		
		

		// create SeekBarWithTwoThumb as Integer range between 20 and 75
		filtroprecio.setText(this.getString(R.string.precio1) + minVal + " €,"
				+ this.getString(R.string.precio2) + maxVal + " €");

		SeekBarWithTwoThumb<Integer> seekBar = new SeekBarWithTwoThumb<Integer>(
				minVal, maxVal, this);
		seekBar.setOnSeekBarWithTwoThumbChangeListener(new OnSeekBarWithTwoThumbChangeListener<Integer>() {
			public void onSeekBarWithTwoThumbValuesChanged(
					SeekBarWithTwoThumb<?> bar, Integer minValue,
					Integer maxValue) {
				// handle changed range values
				minVal = minValue;
				maxVal = maxValue;
				actseeker();

			}
		});

		// add SeekBarWithTwoThumb to pre-defined layout
		ViewGroup layout = (ViewGroup) findViewById(R.id.seekbar);
		layout.addView(seekBar);

	}// end onCreate

	public void actseeker() {
		filtroprecio.setText(this.getString(R.string.precio1) + minVal + " €,"
				+ this.getString(R.string.precio2) + maxVal + " €");

	}// end actseeker

	public void DatosSpinner(String co) {// cargar datos en el spinner		

		Json resultado = new Json();
		JSONObject c = new JSONObject();
		listasp = new ArrayList<String>();
		listasp.add(this.getString(R.string.pais));//llenar el primero con la palabra pais

		String urlsend = "http://www.arcanetsl.com/inmobiliaria/web/cargar-buscador-avanzado-movil/"
				+ langusado; // {cadena}/{idioma}"

		System.out.println("vamos a llamar al JSon");

		try {// llamar a json para que devuelva los resultados en funcion de la
				// busqueda

			System.out.println("vamos a llamar al JSon con la url: " + urlsend);

			c = resultado.readJSONOBJECT(urlsend);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ha petado el retorno de readjson");

			e.printStackTrace();
		}// end catch

		System.out.println("array creado y devuelto");

		try {// extraer los datos necesarios del JSON

			System.out.println("Vamos a sacar los datos que necesitamos");

			JSONArray paises = (JSONArray) c.get("paises");
			String id = ""; // id del pais
			String pais = "";// pais sacado del array

			JSONObject cp = new JSONObject();

			for (int j = 0; j < paises.size(); j++) {

				cp = (JSONObject) paises.get(j);// crear object para
				// recuperar datos
				// concreto del
				// array

				Long idaux = (Long) cp.get("id");
				id = (Long.toString(idaux));
				System.out.println("id: " + id);

				pais = (String) cp.get("nombre");
				System.out.println("pais: " + pais);

				listasp.add(pais);

			}// end for
			System.out.println("salida del for");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ha petado al sacar los datos del json");
			e.printStackTrace();
		}// end catch

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listasp);
		adaptador
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		filtrospinner.setAdapter(adaptador);
		
		//seleccionar pais 
		if(!co.equals("")){
			
			filtrospinner.setSelection(adaptador.getPosition(co));
			
			System.out.println("filtrospinner: " + co);
			
			paisb = co;//guardar en la variable el pais que se ha enviado
			
		}//end if		

		filtrospinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				System.out.println("Seleccionado en spinner: "
						+ arg0.getItemAtPosition(arg2).toString());
				
				String paisaux = arg0.getItemAtPosition(arg2).toString();
				
				
				System.out.println("arg2 spinner: " + arg2);

				if(!paisaux.equals("PAIS")){
					paisb = Integer.toString(arg2);;
				}//end if
				else{
					paisb = "";
					
				}//end else
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}// end DatosSpinner

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		mn = menu;

		loginout();// si esta logado el menu es el de logout

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		Intent intent;

		AdapterResultados adapter;

		switch (item.getItemId()) {

		case R.id.buscarmn:

			intent = new Intent(Filtros.this, Busqueda.class);
			intent.putExtra("moneda", monedausadar);
			intent.putExtra("langusado", langusado);

			startActivity(intent);

			return true;

		case R.id.registromn:

			intent = new Intent(Filtros.this, Registro.class);
			intent.putExtra("moneda", monedausadar);
			intent.putExtra("llamado", "resultados");
			intent.putExtra("ciudadrec", ciudadrec);
			intent.putExtra("langusado", langusado);

			startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				// Login2.logout();
				intent = new Intent(Filtros.this, MiPerfil.class);
				intent.putExtra("moneda", monedausadar);
				intent.putExtra("llamado", "resultados");
				intent.putExtra("ciudadrec", ciudadrec);
				intent.putExtra("langusado", langusado);

				startActivity(intent);

			}// end if
			else {

				intent = new Intent(Filtros.this, Login.class);
				intent.putExtra("moneda", monedausadar);
				intent.putExtra("llamado", "resultados");
				intent.putExtra("ciudadrec", ciudadrec);
				intent.putExtra("langusado", langusado);

				startActivity(intent);

			}// end else

			return true;

			// case R.id.anunciomn:

			/*
			 * intent.setAction(Intent.ACTION_VIEW);
			 * intent.addCategory(Intent.CATEGORY_BROWSABLE);
			 * intent.setData(Uri.parse("http://jcolinas.wordpress.com"));
			 */

			// return true;

		case R.id.espmn:

			System.out.println("Pulsado español");

			langusado = "es";

			languagechange("es");

			return true;

		case R.id.engmn:

			System.out.println("Pulsado ingles");

			langusado = "en";

			languagechange("en");

			return true;
		case R.id.demn:

			System.out.println("Pulsado aleman");

			langusado = "de";

			languagechange("de");

			return true;

		case R.id.frmn:

			System.out.println("Pulsado frances");

			langusado = "fr";

			languagechange("fr");

			return true;

		case R.id.eurmn:

			monedausadar = "EUR";
			System.out.println("monedausadar: " + monedausadar);

			return true;

		case R.id.usdmn:

			monedausadar = "USD";
			System.out.println("monedausadar: " + monedausadar);

			// Creo el adapter personalizado

			adapter = new AdapterResultados(this, arrayres, s, monedausadar);
			System.out.println("creado adapter resuoltados con dolar");

			// Lo aplico
			lista.setAdapter(adapter);

			return true;

		case R.id.gbpmn:

			monedausadar = "GBP";
			System.out.println("monedausadar: " + monedausadar);

			adapter = new AdapterResultados(this, arrayres, s, monedausadar);
			System.out.println("creado adapter resuoltados con libra");

			// Lo aplico
			lista.setAdapter(adapter);

			return true;

		default:

			return super.onOptionsItemSelected(item);

		}

	}// end menu

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.aplifiltro:

			final ProgressDialog progress = ProgressDialog.show(this, "", "Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// aplicar filtros
					enviardatos();

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progress.dismiss();
						}
					});
				}
			}).start();			

			break;

		case R.id.casa:

			tipob = "1";

			casa.setEnabled(true);
			piso.setEnabled(false);
			local.setEnabled(false);
			apartamento.setEnabled(false);
			chalet.setEnabled(false);

			break;// end case

		case R.id.piso:

			tipob = "2";

			casa.setEnabled(false);
			piso.setEnabled(true);
			local.setEnabled(false);
			apartamento.setEnabled(false);
			chalet.setEnabled(false);

			break;// end case

		case R.id.local:

			tipob = "3";

			casa.setEnabled(false);
			piso.setEnabled(false);
			local.setEnabled(true);
			apartamento.setEnabled(false);
			chalet.setEnabled(false);

			break;// end case

		case R.id.apartamento:

			tipob = "4";

			casa.setEnabled(false);
			piso.setEnabled(false);
			local.setEnabled(false);
			apartamento.setEnabled(true);
			chalet.setEnabled(false);

			break;// end case

		case R.id.chalet:

			tipob = "5";

			casa.setEnabled(false);
			piso.setEnabled(false);
			local.setEnabled(false);
			apartamento.setEnabled(false);
			chalet.setEnabled(true);

			break;// end case

		case R.id.mascapacidad:

			huespedes = huespedes + 1;

			filtrocapacidad.setText(huespedes + " Huespedes");

			if (huespedes > 1) {

				capacidadmenos.setEnabled(true);
			}

			break;// end case

		case R.id.menoscapacidad:

			huespedes = huespedes - 1;

			if (huespedes == 1) {

				capacidadmenos.setEnabled(false);
				filtrocapacidad.setText(huespedes + " Huesped");
			} else {

				filtrocapacidad.setText(huespedes + " Huespedes");
			}

			break;// end case

		case R.id.masdormitorio:

			dormitorios = dormitorios + 1;

			filtrodormitorios.setText(dormitorios + " Dormitorios");

			if (dormitorios > 1) {

				dormitoriomenos.setEnabled(true);
			}

			break;// end case

		case R.id.menosdormitorio:

			dormitorios = dormitorios - 1;

			if (dormitorios == 1) {

				dormitoriomenos.setEnabled(false);
				filtrodormitorios.setText(dormitorios + " Dormitorio");
			} else {

				filtrodormitorios.setText(dormitorios + " Dormitorios");
			}

			break;// end case

		}// end switch

	}// end onclick

	public void enviardatos() {

		// pais se rellena en spinner
		provinciab = filtrointroprovincia.getText().toString();
		// tipo se rellena en la gestion de botones
		capacidadb = Integer.toString(huespedes);
		dormitoriosb = Integer.toString(dormitorios);
		preciominb = Integer.toString(minVal);
		preciomaxb = Integer.toString(maxVal);

		System.out.println("Datos que se van a enviar: pais: " + paisb
				+ ", provinciab: " + provinciab + ", tipo: " + tipob
				+ ", capacidad: " + capacidadb + ", dormitorios: "
				+ dormitoriosb + ", preciodesde: " + preciominb
				+ ", preciohasta: " + preciomaxb);

		String urlbusqueda = "http://www.arcanetsl.com/inmobiliaria/web/buscar-avanzada-movil/"
				+ langusado;

		try {

			HttpClient httpclient = new DefaultHttpClient();

			/*
			 * Creamos el objeto de HttpClient que nos permitira conectarnos
			 * mediante peticiones http
			 */

			HttpPost httppost = new HttpPost(urlbusqueda);

			/*
			 * El objeto HttpPost permite que enviemos una peticion de tipo POST
			 * a una URL especificada
			 */

			// AÑADIR PARAMETROS

			List<NameValuePair> params = new ArrayList<NameValuePair>();// se
																		// pasa
																		// como
																		// parametros
																		// el id
																		// del
																		// inmueble
																		// sobre
																		// el
																		// que
																		// se
																		// comenta
																		// y el
																		// comentario

			params.add(new BasicNameValuePair("pais", paisb));
			params.add(new BasicNameValuePair("provincia", provinciab));
			params.add(new BasicNameValuePair("tipo", tipob));
			params.add(new BasicNameValuePair("capacidad", capacidadb));
			params.add(new BasicNameValuePair("dormitorios", dormitoriosb));
			params.add(new BasicNameValuePair("preciodesde", preciominb));
			params.add(new BasicNameValuePair("preciohasta", preciomaxb));

			System.out.println("params enviarcoment: " + params);
			/*
			 * Una vez añadidos los parametros actualizamos la entidad de
			 * httppost, esto quiere decir en pocas palabras anexamos los
			 * parametros al objeto para que al enviarse al servidor envien los
			 * datos que hemos añadido
			 */

			httppost.setEntity(new UrlEncodedFormEntity(params));

			/* Finalmente ejecutamos enviando la info al server */

			HttpResponse resp = httpclient.execute(httppost);
			
			System.out.println("resp post: " + resp);

			HttpEntity ent = resp.getEntity();/* y obtenemos una respuesta */
			System.out.println("ent post: " + resp.getEntity().getContentLength());

			String text = EntityUtils.toString(ent);

			System.out.println("return post: " + text);
			
			//volver a resultados
			// ////////						
								Intent intent = new Intent(Filtros.this,
										Resultados.class);
								intent.putExtra("moneda", monedausadar);
								intent.putExtra("langusado", langusado);								
								intent.putExtra("filtro", text);
								intent.putExtra("ciudadaux", "filtros");
								intent.putExtra("ciudad", filtrointroprovincia.getText().toString());
								startActivity(intent);
			//

		} catch (Exception e) {

			// return "error";}
			System.out.println("Excepcion post");
			e.printStackTrace();

		}
	}// end enviardatos

	public void languagechange(String lang) {

		Locale locale = new Locale(lang);
		Locale.setDefault(locale);

		System.out.println("locale: " + locale);

		Configuration config = new Configuration();
		config.locale = locale;

		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

		System.out.println("fin languajechange");

		updatetext();

		guardaselecidioma(lang);// guardar la seleccion del idioma para la
								// proxima vez que abra la app

	}// end languagechange

	public void updatetext() {

		System.out.println("mn: " + mn);

		final ProgressDialog progress = ProgressDialog.show(this, "",
				"Procesing...", true);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// do the thing that takes a long time

				final Intent intent = new Intent(Filtros.this, Filtros.class);
				intent.putExtra("ciudad", ciudad);
				intent.putExtra("langusado", langusado);

				startActivity(intent);

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progress.dismiss();
					}
				});
			}
		}).start();

	}// end updatetext

	public void loginout() {

		MenuItem txtperfil = mn.findItem(R.id.perfilmn);

		if (conec.equals("S")) {

			txtperfil.setTitle(R.string.perfil);

		}// end if
		else {

			txtperfil.setTitle(R.string.miperfil);

		}// end else

	}// end loginout

	public void guardaselecidioma(String lang) {

		try {

			System.out.println("vamos a llamar a guardaidioma");

			GuardaIdioma.onClickGuardar(lang, this);

			System.out.println("despues de guardaidioma");

		} catch (Exception e) {

			System.out
					.println("Ha petado la llamada a guarda idioma desde main");
			System.out.println(e);
			System.out
					.println("fin excepcion guarda idioma main---------------------------");

		}// end try catch*/

	}// guardaselecidioma

	public void onBackPressed() {// si se pulsa el boton atras del tlf
		// Write your code here

		String datoatras = VariableAtras.getDato();

		int cont = VariableAtras.getContador();

		System.out.println("Pulsado atras en resultados: " + cont);

		if (datoatras.equals("filtros") && cont == 3) {

			System.out.println("Pulsado atras en filtros");

			// ////////
			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);// se muestre efecto cargando

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Filtros.this,
							Resultados.class);
					intent.putExtra("moneda", monedausadar);
					intent.putExtra("langusado", langusado);
					intent.putExtra("ciudad", ciudad);

					startActivity(intent);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progress.dismiss();
						}
					});
				}
			}).start();

			// /////

		}// end if

		super.onBackPressed();

		VariableAtras.setContador(cont - 1);
		System.out.println("despues de reducir filtros: " + cont);

	}// end onBackPressed

}// end class
