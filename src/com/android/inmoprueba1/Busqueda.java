package com.android.inmoprueba1;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.android.inmoprueba1.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu; 
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import android.text.Editable;
import android.text.TextWatcher;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;

public class Busqueda extends Activity implements
		android.view.View.OnClickListener {

	AutoCompleteTextView ciudad_buscar;
	Button buscarbtn, busqavan;

	String seleccion, langusado;

	Bundle reicieveParams;

	Menu mn;

	Double tipoChange;// saber el tipo de cambio al cambiar de moneda

	String monedausada = "";// que moneda se usa para mostrar

	String conec = "N";

	String datoatras;// controlar accion cuando se pulsa el boton atras del
						// telefono

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.busqueda);

		System.out.println("Entrando en busqueda.java");

		// actionbar
		ActionBar actionBar = getActionBar();
		actionBar.show();
		actionBar.setBackgroundDrawable(new ColorDrawable(0x666699));
		// //////// 

		// controlar accion cuando se pulsa el boton atras del telefono
		VariableAtras.setDato("busqueda");
		VariableAtras.setContador(1);
		//
		// recibiendo paramentros de busqueda
		reicieveParams = getIntent().getExtras();
		monedausada = reicieveParams.getString("moneda");
		langusado = reicieveParams.getString("langusado");
		System.out.println("monedausada en buscar: " + monedausada);
		System.out.println("langusado en buscar: " + langusado);
		//

		ciudad_buscar = (AutoCompleteTextView) findViewById(R.id.ciudad_buscar);
		buscarbtn = (Button) findViewById(R.id.aceptar);
		busqavan = (Button) findViewById(R.id.filtrosbus);

		buscarbtn.setOnClickListener(this);
		busqavan.setOnClickListener(this);

		conec = Logado.comprobarlog(this);// saber si esta logueado para mostrar
											// unas opciones u otras

		// funcionamiento autocomplete
		try {

			ciudad_buscar.setThreshold(1);

			ciudad_buscar.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

					System.out.println("probando ontextchanged");
					System.out.println("s: " + s);

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							Busqueda.this,
							android.R.layout.simple_dropdown_item_1line,
							autocomplete(s.toString()));

					// Se establece el Adapter
					ciudad_buscar.setAdapter(adapter);

					System.out.println("deberia funcionar");

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
				}
			});

			ciudad_buscar.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					//Lineas para ocultar el teclado virtual (Hide keyboard)
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(ciudad_buscar.getWindowToken(), 0);

					seleccion = arg0.getItemAtPosition(arg2).toString();// guardar
																		// en
																		// seleccion
																		// la
																		// ciudad
																		// que
																		// se
																		// elije
					/*int coma = seleccion.indexOf(",");
					System.out.println("coma: " + coma);
					seleccion = seleccion.substring(0, coma);*/

					System.out.println("Ciudad seleccionada: " + seleccion);
										
				}
			});

		} catch (Exception e) {
			System.out
					.println("el textviewadapter ha petao como un cabron-------------------------");
			e.printStackTrace();
			System.out
					.println("-------------------------------------------------------------------");
		}
		// ////

	}// end onCreate

	// //////////////google autocomplete

	private static final String LOG_TAG = "ExampleApp";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyCUf6NnKVFz3clBpTSBFPZ-jKvQoSfSrBI";

	private ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;

		String paislocal = Locale.getDefault().getDisplayLanguage();
		System.out.println("paislocal: " + paislocal);

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {

			System.out.println("entrando en autocomplete google: " + input);

			StringBuilder sb = new StringBuilder(PLACES_API_BASE
					+ TYPE_AUTOCOMPLETE + OUT_JSON);
			// sb.append("?sensor=false&key=" + API_KEY);// sensor a true si usa
			// el
			// gps
			sb.append("?types=(cities)&key=" + API_KEY);// sensor a true si usa
														// el
			// sb.append("&language=" + paislocal);langusado
			sb.append("&language=" + langusado);
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			System.out.println("creado stringbuffer");

			URL url = new URL(sb.toString());
			System.out.println("creado url: " + url);
			conn = (HttpURLConnection) url.openConnection();
			System.out.println("creado conexion");
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			System.out.println("creado inputstreamreader");

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
			System.out.println("finalizado while read: " + jsonResults);
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {

			JSONObject jsonObj = (JSONObject) JSONValue.parse(jsonResults
					.toString());
			JSONArray predsJsonArray = (JSONArray) jsonObj.get("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<String>(predsJsonArray.size());
			System.out.println("creado resultlist");

			for (int i = 0; i < predsJsonArray.size(); i++) {
				JSONObject c = (JSONObject) predsJsonArray.get(i);

				System.out.println("dentro del for autocomplete: " + i);
				resultList.add((String) c.get("description"));

			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}
		System.out.println("devolviendo resultlist");
		return resultList;
	}

	// ///////////////////////////////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {// crear menu de opciones
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		mn = menu;
		loginout();// si esta logado el menu es el de logout
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {// acciones a realizar
															// cuando se pulsa
															// alguna opcion del
															// menu
		// TODO Auto-generated method stub

		Intent intent;

		System.out.println("menu seleccionado: " + item);
		System.out.println("menu getitem seleccionado: " + item.getItemId());

		switch (item.getItemId()) {

		case R.id.buscarmn:

			intent = new Intent(Busqueda.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", langusado);
			startActivity(intent);

			return true;

			case R.id.registromn:

				intent = new Intent(Busqueda.this, Registro.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "busqueda");
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				//Login2.logout();// realiza la opcion de logout
				intent = new Intent(Busqueda.this, MiPerfil.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "busqueda");
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			}// end if
			else {

				intent = new Intent(Busqueda.this, Login.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "busqueda");
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

		case R.id.espmn:// cambio de idioma

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

			monedausada = "EUR";
			System.out.println("monedausada: " + monedausada);

			return true;

		case R.id.usdmn:

			monedausada = "USD";
			System.out.println("monedausada: " + monedausada);

			return true;

		case R.id.gbpmn:

			monedausada = "GBP";
			System.out.println("monedausada: " + monedausada);

			return true;

		default:

			return true;

		}

	}// end menu

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.aceptar:

			System.out
					.println("antes de String ciudadb = ciudad_buscar.getText().toString();");

			String ciudadb = ciudad_buscar.getText().toString();

			System.out.println("ciudadb: " + ciudadb);
			System.out.println("seleccion en onclick: " + seleccion);

			if (ciudadb.equals("")) {// si no se ha introducido ninguna ciudad
										// se toma la ubicacion acutal del gps o
										// de la conexion a internet

				try {

					LocationManager locMgr;
					LocationListener locLstnr;

					System.out
							.println("antes de locMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);");

					locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

					System.out
							.println("antes de ocLstnr = new MyLocationListener();");

					locLstnr = new MyLocationListener();

					System.out
							.println("antes de locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locLstnr);");

					if (locMgr.getAllProviders().contains(
							LocationManager.NETWORK_PROVIDER)) {

						System.out.println("dentro if network");

						locMgr.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER, 0, 0,
								locLstnr);
						Double latitude = locMgr.getLastKnownLocation(
								LocationManager.NETWORK_PROVIDER).getLatitude();
						Double longitude = locMgr.getLastKnownLocation(
								LocationManager.NETWORK_PROVIDER)
								.getLongitude();

						System.out.println("latitude" + latitude + "longitude"
								+ longitude);

						try {

							Geocoder geocoder = new Geocoder(this);

							List<Address> direccion = geocoder.getFromLocation(
									latitude, longitude, 1);

							/*Address direc = direccion.get(0);
							seleccion = String
									.format("%s", direc.getLocality());*/
							
							seleccion = direccion.get(0).getLocality();
							String pais =  direccion.get(0).getCountryName();							
							
							seleccion = seleccion + ", " + pais;

							System.out.println("direccion recuperada: "
									+ seleccion);

						} catch (Exception e) {

							System.out.println("ha petado el geocoder: " + e);

						}// end try

					}// end if network

					if (locMgr.getAllProviders().contains(
							LocationManager.GPS_PROVIDER)) {

						System.out.println("dentro if gps");

						locMgr.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, 0, 0, locLstnr);
						Double latitude = locMgr.getLastKnownLocation(
								LocationManager.GPS_PROVIDER).getLatitude();
						Double longitude = locMgr.getLastKnownLocation(
								LocationManager.GPS_PROVIDER).getLongitude();

						System.out.println("latitude" + latitude + "longitude"
								+ longitude);

						try {

							Geocoder geocoder = new Geocoder(this);

							List<Address> direccion = geocoder.getFromLocation(
									latitude, longitude, 1);

							/*Address direc = direccion.get(0);
							seleccion = String
									.format("%s", direc.getLocality());*/
							
							seleccion = direccion.get(0).getLocality();
							String pais =  direccion.get(0).getCountryName();
							
							seleccion = seleccion + ", " + pais;

							System.out.println("direccion recuperada: "
									+ seleccion);

						} catch (Exception e) {

							System.out.println("ha petado el geocoder: " + e);

						}// end try

					}// end if gps

				} catch (Exception e) {

					System.out.println("ha petado al llamar al location: " + e);
				}

			} else {

				if (seleccion == null) {// si el valor introducido no es valido
										// y no funciona la geolocalizacion se
										// toma por defecto valladolid

					if (ciudadb.equals("")) {

						ciudadb = "VALLADOLID";

					} else {// si se ha introducido un destino valido, se quita
							// la parte del pais

						seleccion = ciudadb;
						/*int coma = seleccion.indexOf(",");

						System.out.println("coma: " + coma);

						if (coma > 0) {

							seleccion = seleccion.substring(0, coma);
						}*/

					}// end if

				}// end if

			}// end if ciudadb.equals("")

			System.out.println("Vamos a llamar a resultados con: " + seleccion);
			
			//primero comprobar si la ciudad esta en la bd
			Json resultado = new Json();
			JSONArray Array = new JSONArray();

			//String urlsend = "http://www.arcanetsl.com/inmobiliaria/web/buscar-provincia2/";// url
																							// para
																							// buscar
																							// por
																							// provicia
																							// seleccionada
			
			String urlsend = "http://www.arcanetsl.com/inmobiliaria/web/buscar-movil/"; //{cadena}/{idioma}"

			// ///////////////

			System.out.println("vamos a llamar al JSon");

			try {// llamar a json para que devuelva los resultados en funcion de la
					// busqueda

				//urlsend = urlsend + seleccion;// añadir a la url la ciudad seleccionada
				
				
				seleccion = seleccion.replaceAll(" ","%20");
				System.out.println("seleccion: " + seleccion);
				
				urlsend = urlsend + seleccion + "/" + langusado;// añadir a la url la ciudad seleccionada

				System.out.println("vamos a llamar al JSon con la url: " + urlsend);

				Array = resultado.readJSON(urlsend);// devuelve un json con los
													// datos de la ciudad
													// seleccionada

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("ha petado el retorno de readjson");

				e.printStackTrace();
			}// end catch


			if(!Array.isEmpty()){//si existe

			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);// se muestre efecto cargando

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Busqueda.this,
							Resultados.class);

					intent.putExtra("ciudad", seleccion);
					intent.putExtra("moneda", monedausada);
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
			
			}//end if
			else{//si la ciudad no tiene resultados
				
				final String err = this.getString(R.string.errorbusq);

	            final CharSequence[] options = { err };
	            
	            AlertDialog.Builder builder = new AlertDialog.Builder(Busqueda.this);
	            
	            builder.setTitle(this.getString(R.string.avisobusq));

	            builder.setItems(options, new DialogInterface.OnClickListener() {

	                @Override

	                public void onClick(DialogInterface dialog, int item) {

	                    if (options[item].equals(err))//confirmar registro e ir a busqueda

	                    {

	                    	dialog.dismiss();

	                    } 		                 

	                }

	            });

	            builder.show();
				
			}//end else

			break;// end case R.id.aceptar
			
		case R.id.filtrosbus:
			
			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time

					final Intent intent = new Intent(Busqueda.this,
							Filtros.class);
					intent.putExtra("ciudad", ""); 
					intent.putExtra("moneda", monedausada);
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
			
			break;//end case R.id.filtrosbus:

		}// end switch

	} // end onclick

	public void languagechange(String lang) {

		Locale locale = new Locale(lang);
		Locale.setDefault(locale);

		System.out.println("locale: " + locale);

		Configuration config = new Configuration();
		config.locale = locale;

		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

		System.out.println("fin languajechange");

		updatetext();// actualizar idioma pantalla

		guardaselecidioma(lang);// guardar la seleccion del idioma para la
								// proxima vez que abra la app

	}// end languagechange

	public void updatetext() {// cambio textos de idioma

		System.out.println("mn: " + mn);

		/*
		 * MenuItem txtregistro = mn.findItem(R.id.registromn); MenuItem
		 * txtanuncio = mn.findItem(R.id.anunciomn);
		 */
		// MenuItem txtidioma = mn.findItem(R.id.idiomamn);
		// MenuItem txtmoneda = mn.findItem(R.id.monedamn);

		/*
		 * try { // System.out.println("txtregistro1: " + txtregistro);
		 * 
		 * buscarbtn.setText(R.string.buscar); //
		 * txtregistro.setTitle(R.string.registro); loginout();
		 * 
		 * // txtanuncio.setTitle(R.string.publicaanuncio);
		 * txtidioma.setTitle(R.string.idioma);
		 * txtmoneda.setTitle(R.string.moneda);
		 * 
		 * } catch (Exception e) {
		 * 
		 * System.out.println("ha petao txtregistro: " + e); }
		 */

		final ProgressDialog progress = ProgressDialog.show(this, "",
				"Procesing...", true);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// do the thing that takes a long time
				final Intent intent = new Intent(Busqueda.this, Busqueda.class);
				intent.putExtra("moneda", monedausada);
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

	public void guardaselecidioma(String lang) {// guardar la selec del nuevo
												// idioma

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

	public void loginout() {// hacer operacion de deslogado

		MenuItem txtperfil = mn.findItem(R.id.perfilmn);

		if (conec.equals("S")) {

			txtperfil.setTitle(R.string.perfil);

		}// end if
		else {

			txtperfil.setTitle(R.string.miperfil);

		}// end else

	}// end loginout

	@SuppressLint("NewApi")
	public void onBackPressed() {// si se pulsa el boton atras del tlf
		// Write your code here

		datoatras = VariableAtras.getDato();

		int cont = VariableAtras.getContador();

		System.out.println("Pulsado atras en busqueda: " + cont);

		if (datoatras.equals("busqueda") && cont == 1) {

			System.out.println("Pulsado atras en busqueda");

			VariableAtras.setContador(99);

			Intent intent = new Intent(Busqueda.this, MainActivity.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", langusado);

			startActivity(intent);
			this.finishAffinity();// finalizar todas las activity anteriores

		}// end if

		super.onBackPressed();

		System.out.println("despues de reducir busqueda: " + cont);

	}// end onBackPressed

}// end class busqueda
