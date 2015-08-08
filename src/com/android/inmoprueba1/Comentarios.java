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

import android.app.ProgressDialog;

import android.content.Intent;
import android.content.res.Configuration;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

//mostrar y enviar (si se esta logado) comentarios sobre un inmueble

public class Comentarios extends Resultado implements
		android.view.View.OnClickListener {

	// declaracion variables

	Button botoncomentar;
	EditText introcomentario;
	TextView comentarios, confirmacion, resumcoments;
	ViewGroup layout;

	String conec = "N";

	Menu mn;
	String langusado;
	Long id;// recoger id seleccionado
	String monedausada = "";// que moneda se usa para mostrar

	String nombreaux;
	String textocomentaux;
	String fechaaux;

	String apikey = "";

	int idc;

	ListView listacoment;
	ArrayList<ListarComents> arraycom;
	ListarComents resultadocom;

	// ////////////////////////

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comentarios);

		System.out.println("entrando en comentarios");

		VariableAtras.setDato("comentarios");// para controlar boton atras movil
		VariableAtras.setContador(4);

		botoncomentar = (Button) findViewById(R.id.botoncomentario);
		introcomentario = (EditText) findViewById(R.id.introcomentariosjson);
		comentarios = (TextView) findViewById(R.id.comentarios);
		listacoment = (ListView) findViewById(R.id.listacomentarios);
		arraycom = new ArrayList<ListarComents>();
		confirmacion = (TextView) findViewById(R.id.enviocoment);

		botoncomentar.setOnClickListener(this);
		confirmacion.setOnClickListener(this);

		// recoger id de inmueble
		reicieveParams = getIntent().getExtras();// recibir el id de la
													// propiedad seleccionada
		id = reicieveParams.getLong("id");
		langusado = reicieveParams.getString("langusado");

		System.out.println("langusado en buscar: " + langusado);
		// ////////////////////////

		mostrarcoment();// comprobar si se esta logueado para mostrar o no un
						// edittext

		recogercoment();// buscar los comentarios

		monedausada = reicieveParams.getString("moneda");

	}// end oncreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		try {

			getMenuInflater().inflate(R.menu.main, menu);

			mn = menu;

			loginout();

		} catch (Exception e) {

			System.out.println("Ha petado la creacion del menu: " + e);
		}

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		Intent intent;

		switch (item.getItemId()) {

		case R.id.buscarmn:

			intent = new Intent(Comentarios.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", langusado);
			startActivity(intent);

			return true;

			case R.id.registromn:

				intent = new Intent(Comentarios.this, Registro.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "comentarios");
				intent.putExtra("id", id);
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				//Login2.logout();
				intent = new Intent(Comentarios.this, MiPerfil.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "comentarios");
				intent.putExtra("id", id);
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			}// end if
			else {

				intent = new Intent(Comentarios.this, Login.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "comentarios");
				intent.putExtra("id", id);
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

		}// end switch

	}// end menu

	public void onClick(View v) {

		final ProgressDialog progress;

		switch (v.getId()) {

		case R.id.botoncomentario:// enviar un comentario nuevo

			final String comentariorealizado = introcomentario.getText()
					.toString();

			System.out
					.println("comentario introducido: " + comentariorealizado);
			progress = ProgressDialog.show(this, "", "Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					enivarcoment(apikey, comentariorealizado);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progress.dismiss();
						}
					});
				}
			}).start();

			// recargar comentarios
			introcomentario.setText("");
			comentarios.setVisibility(View.GONE);
			confirmacion.setVisibility(View.VISIBLE);

			break;// end case R.id.botoncomentario:

		case R.id.enviocoment:

			// volver a resultado

			progress = ProgressDialog.show(this, "", "Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Comentarios.this,
							Resultado.class);
					intent.putExtra("id", id);// enviarle a la pagina de
												// resultado el id seleccionado
					intent.putExtra("lang", langusado);
					intent.putExtra("moneda", monedausada);

					startActivity(intent);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progress.dismiss();
						}
					});
				}
			}).start();

			break;// end R.id.enviocoment:

		}// end switch

	}// end onclick

	public String cortarcomentarios(String coment) {// mostrar preview de los
													// comentarios

		System.out.println("entrando en cortarcomentarios");

		String cometcort = "";

		if (coment.length() > 40) {

			cometcort = coment.substring(0, 40);
			cometcort = cometcort + "...";

			return cometcort;

		}// end if
		else {

			return coment;
		}

	}// end cortarcomentarios

	public void mostrarcoment() {// mostrar edittext y boton si se esta logueado

		System.out.println("Dentro de mostrarcoment");

		// saber si esta conectado
		conec = Logado.comprobarlog(this);

		if (conec.equals("S")) {

			System.out.println("Esta logueado");

			introcomentario.setVisibility(View.VISIBLE);
			botoncomentar.setVisibility(View.VISIBLE);

		}// end if
		else {

			introcomentario.setVisibility(View.GONE);
			botoncomentar.setVisibility(View.GONE);

		}// end else

	}// end mostrarcoment

	public void recogercoment() {// recoger del json los comentarios que ya
									// existen

		String urlrecom = "http://www.arcanetsl.com/inmobiliaria/web/ver-comentarios/"
				+ id;

		Json resultado = new Json();
		JSONArray Array = new JSONArray();

		System.out.println("vamos a llamar al JSon");

		try {

			Array = resultado.readJSON(urlrecom);// devuelve un json con todos
													// los comentarios

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ha petado el retorno de readjson");
			e.printStackTrace();
		}

		try {

			System.out.println("Vamos a sacar los datos que necesitamos");

			final String nombre[] = new String[Array.size()];
			final String textocoment[] = new String[Array.size()];
			final String fecha[] = new String[Array.size()];

			for (int i = 0; i < Array.size(); i++) {// obtener los datos que
													// necesitamos

				JSONObject c = (JSONObject) Array.get(i);// crear object para
															// recuperar datos
															// concreto del
															// array

				nombre[i] = (String) c.get("nombre");
				nombreaux = nombre[i];
				System.out.println("nombre comentario: " + nombreaux);

				// ////////////////////
				textocoment[i] = (String) c.get("opinion");
				System.out.println("textocoment: " + textocoment[i]);

				// ////////////////////
				fecha[i] = (String) c.get("fecha");
				fechaaux = fecha[i];
				System.out.println("fecha: " + fechaaux);

				textocomentaux = cortarcomentarios(textocoment[i]);

				resultadocom = new ListarComents(textocomentaux, fechaaux);

				// crear array con todos los elementos
				arraycom.add(resultadocom);

			}// end for

			System.out.println("he salido del for");

			// Creo el adapter personalizado
			AdapterComents adapter = new AdapterComents(this, arraycom);

			System.out.println("creado adapter comentarios");

			// Lo aplico
			listacoment.setAdapter(adapter);

			// saber que elemetno de la lista se selecciona
			System.out.println("saber que elemento de la lista se selecciona");

			listacoment.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int posicion, long arg3) {

					// En posicion tienes el indice que se ha seleccionado
					System.out.println("posicion seleccionada: " + posicion);

					Intent intent = new Intent(Comentarios.this,
							VerComentario.class);
					intent.putExtra("id", id);// enviarle a la pagina de
												// resultado el id seleccionado
					intent.putExtra("fechac", fecha[posicion]);
					intent.putExtra("autorc", nombre[posicion]);
					intent.putExtra("textoc", textocoment[posicion]);
					intent.putExtra("moneda", monedausada);
					intent.putExtra("langusado", langusado);

					startActivity(intent);

				}

			});// end OnItemClickListener

		} catch (Exception e) {
			System.out
					.println("Resultados ha petao como un cabron-------------------------");
			e.printStackTrace();
			System.out
					.println("-------------------------------------------------------------------");
		}// end catch

	}// end recogercoment

	public void enivarcoment(String apikey, String comentario) {// enviar un
																// nuevo
																// comentario
																// con el apikey
																// del usuario

		String urlsend = "http://www.arcanetsl.com/inmobiliaria/web/movil/api/nuevo-comentario?apikey=";

		apikey = Variablecookie.getCookie();

		System.out.println("apikey recogida: " + apikey);

		urlsend = urlsend + apikey;// añadir el apikey a la url

		String idinmu = id.toString();// pasar a string el id para el post de
										// parametros

		try {

			HttpClient httpclient = new DefaultHttpClient();

			/*
			 * Creamos el objeto de HttpClient que nos permitira conectarnos
			 * mediante peticiones http
			 */

			HttpPost httppost = new HttpPost(urlsend);

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

			params.add(new BasicNameValuePair("idinmueble", idinmu));

			params.add(new BasicNameValuePair("comentario", comentario));

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

			HttpEntity ent = resp.getEntity();/* y obtenemos una respuesta */

			String text = EntityUtils.toString(ent);

			System.out.println("return post: " + text);

		} catch (Exception e) {

			// return "error";}
			System.out.println("Excepcion post");
			e.printStackTrace();

		}

	}// end enivarcoment

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
				final Intent intent = new Intent(Comentarios.this,
						Comentarios.class);
				intent.putExtra("id", id);
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

		} catch (Exception e) {

			System.out
					.println("Ha petado la llamada a guarda idioma desde main");
			System.out.println(e);
			System.out
					.println("fin excepcion guarda idioma main---------------------------");

		}// end try catch*/

	}// guardaselecidioma

	@Override
	public void onBackPressed() {
		// Write your code here

		String datoatras = VariableAtras.getDato();

		int cont = VariableAtras.getContador();

		System.out.println("Pulsado atras en comentarios: " + cont);

		if (datoatras.equals("comentarios") && cont == 4) {

			VariableAtras.setDato("resultado");

			System.out.println("Pulsado atras en comentarios");

			// ////
			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);// se muestre efecto cargando

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Comentarios.this,
							Resultado.class);
					intent.putExtra("id", id);
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

			// ////

		}// end if

		super.onBackPressed();

		VariableAtras.setContador(cont - 1);
		System.out.println("despues de reducir busqueda: " + cont);

	}

}// end class comentarios
