package com.android.inmoprueba1;

import java.util.ArrayList;
import java.util.Locale;

import com.android.inmoprueba1.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;

import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import android.widget.AdapterView.OnItemClickListener;

public class Resultados extends Busqueda {

	ImageView fotoprincipalMA;
	ImageView gridMA, listaMA;
	ImageView fotores;

	Bundle reicieveParams;

	int s = 1;// q seleccion de vista se hace: 1 imagen grande (grid), 2 imagen
				// peque (lista)

	ListView lista;
	ArrayList<Directivo> arrayres;
	Directivo resultadodir;

	Double tipoChange;// saber el tipo de cambio al cambiar de moneda

	String monedausadar = "";// que moneda se usa para mostrar
	String ciudadrec, metros, titulo, precio;

	String conec = "N";

	Long capacidad, dormitorios;

	Drawable dw;
	
	Long id[] = new Long[0];

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultados);

		VariableAtras.setDato("resultados");// controlar accion boton atras tlf
		VariableAtras.setContador(2);

		System.out.println("Entrando en resultados");

		reicieveParams = getIntent().getExtras();// recibiendo paramentros de
													// busqueda

		monedausadar = reicieveParams.getString("moneda");
		langusado = reicieveParams.getString("langusado");

		// languagechange(langusado);

		System.out.println("langusado en resultados: " + langusado);

		System.out.println("monedausadar recibida en resultados: "
				+ monedausadar);

		// saber si esta conectado
		conec = Logado.comprobarlog(this);

		// declaracion variables

		Button actfiltros = (Button) findViewById(R.id.filtros);// ver filtros

		actfiltros.setOnClickListener(this);

		gridMA = (ImageView) findViewById(R.id.grid);
		listaMA = (ImageView) findViewById(R.id.lista);

		fotoprincipalMA = (ImageView) findViewById(R.id.foto);

		gridMA.setOnClickListener(this);
		listaMA.setOnClickListener(this);

		lista = (ListView) findViewById(R.id.listaresultados);
		arrayres = new ArrayList<Directivo>();

		Json resultado = new Json();
		JSONArray Array = new JSONArray();

		ciudadrec = reicieveParams.getString("ciudad");// ciudad seleccionada
		
		System.out.println("ciudadrec" + ciudadrec);
		
		String caux = reicieveParams.getString("ciudadaux"); 
		
		System.out.println("caux" + caux);

		if (!(caux == null)) {

			String filt = reicieveParams.getString("filtro");

			aplicarfiltros(filt);

		}// end if
		else {			

			String urlsend = "http://www.arcanetsl.com/inmobiliaria/web/buscar-movil/"; // {cadena}/{idioma}"

			// ///////////////

			System.out.println("vamos a llamar al JSon");

			try {// llamar a json para que devuelva los resultados en funcion de
					// la
					// busqueda

				urlsend = urlsend + ciudadrec + "/" + langusado;// añadir a la
																// url la ciudad
				// seleccionada

				System.out.println("vamos a llamar al JSon con la url: "
						+ urlsend);

				Array = resultado.readJSON(urlsend);// devuelve un json con los
													// datos de la ciudad
													// seleccionada
				
				mostrarresultados(Array);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("ha petado el retorno de readjson");

				e.printStackTrace();
			}// end catch

			
		}// end else
		
		// saber que elemetno de la lista se selecciona
					System.out.println("saber que elemento de la lista se selecciona");

					lista.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int posicion, long arg3) {

							// En posicion tienes el indice que se ha seleccionado
							System.out.println("posicion seleccionada: " + posicion);
							System.out.println("id de casa seleccionada: "
									+ id[posicion]);

							Intent intent = new Intent(Resultados.this, Resultado.class);
							intent.putExtra("id", id[posicion]);// enviarle a la
																// pagina
																// de resultado el
																// id
																// seleccionado
							intent.putExtra("langusado", langusado);
							System.out.println("langusado al pulsar en la lista: ");
							intent.putExtra("moneda", monedausadar);
							VariableAtras.setcont(0);
							System.out.println("metiendo valor conta en resultados");
							startActivity(intent);

						}

					});// end OnItemClickListener

	}// end onCreate

	public void aplicarfiltros(String filtro) {

		JSONParser parserfil = new JSONParser();

		try {

			JSONArray Arrayfil = new JSONArray();

			Object objfil = parserfil.parse(filtro);

			Arrayfil = (JSONArray) objfil;

			mostrarresultados(Arrayfil);

		} catch (Exception e) {

			System.out
					.println("Ha petao miserablemente el jsonobjetct de filtros---------------------");
			e.printStackTrace();
			System.out
					.println("------------------------------------------------------------");
		}// end try-catch

	}// end aplicarfiltros

	public void mostrarresultados(JSONArray Array) {
		
		String titulo = "";
		String precio = "";

		try {

			System.out.println("Vamos a sacar los datos que necesitamos");

			//final Long id[] = new Long[Array.size()];
			id = new Long[Array.size()];

			for (int i = 0; i < Array.size(); i++) {// obtener los datos que
													// necesitamos

				System.out.println("dentro del for: " + i);

				JSONObject c = (JSONObject) Array.get(i);// crear object para
															// recuperar datos
															// concreto del
															// array

				id[i] = (Long) c.get("id");// en array id se mete el id
											// correspondiente para saber
											// despues que casa se selecciona
				System.out.println("id: " + id[i]);

				// //////////

				String foto1 = (String) c.get("foto1");
				System.out.println("foto del json: " + (String) c.get("foto1"));

				System.out
						.println("enviando fotoprincipalMA desde resultados: "
								+ fotoprincipalMA);

				fotoprincipalMA = CargarImagenes.downloadImg(fotoprincipalMA,
						foto1);// buscar la foto que necesitamos

				System.out
						.println("despues llamada CargarImagenes.downloadImg();");

				// ///////////

				titulo = (String) c.get("titulo_anuncio");
				System.out.println("titulo del json: "
						+ (String) c.get("titulo_anuncio"));

				// ////////////////////

				capacidad = (Long) c.get("capacidad");
				System.out.println("capacidad: " + capacidad);

				// //////

				Long metrosl = (Long) c.get("metros");
				metros = (Long.toString(metrosl));
				System.out.println("metros: " + metros);

				// ////////

				dormitorios = (Long) c.get("dormitorios");
				System.out.println("dormitorios: " + dormitorios);

				// unirlo todo en una etiqueta

				titulo = titulo + "\n" + this.getString(R.string.capacidad)
						+ capacidad + "\n"
						+ this.getString(R.string.dormitorios) + dormitorios
						+ "\n" + this.getString(R.string.metros) + metros;

				Long preciol = (Long) c.get("precio");
				precio = (Long.toString(preciol));
				System.out.println("precio del json: " + precio);

				// meter la imagen en un objeto drawable para poder mostrarla
				dw = fotoprincipalMA.getDrawable();

				// llamar a directivo para que cree el resultado con todo junto
				resultadodir = new Directivo(dw, titulo, precio);

				// crear array con todos los elementos
				arrayres.add(resultadodir);

				/* }// end if */

			}// end for

			System.out.println("he salido del for");

			// Creo el adapter personalizado
			AdapterResultados adapter = new AdapterResultados(this, arrayres,
					s, monedausadar);

			System.out.println("creado adapter resuoltados");

			// Lo aplico
			lista.setAdapter(adapter);

			// saber que elemetno de la lista se selecciona
			/*System.out.println("saber que elemento de la lista se selecciona");

			lista.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int posicion, long arg3) {

					// En posicion tienes el indice que se ha seleccionado
					System.out.println("posicion seleccionada: " + posicion);
					System.out.println("id de casa seleccionada: "
							+ id[posicion]);

					Intent intent = new Intent(Resultados.this, Resultado.class);
					intent.putExtra("id", id[posicion]);// enviarle a la
														// pagina
														// de resultado el
														// id
														// seleccionado
					intent.putExtra("langusado", langusado);
					System.out.println("langusado al pulsar en la lista: ");
					intent.putExtra("moneda", monedausadar);
					VariableAtras.setcont(0);
					System.out.println("metiendo valor conta en resultados");
					startActivity(intent);

				}*/

			//});// end OnItemClickListener

		} catch (Exception e) {
			System.out
					.println("Resultados ha petao como un cabron-------------------------");
			e.printStackTrace();
			System.out
					.println("-------------------------------------------------------------------");
		}// end try-catch

	}// end mostrarresultados

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

			intent = new Intent(Resultados.this, Busqueda.class);
			intent.putExtra("moneda", monedausadar);
			intent.putExtra("langusado", langusado);

			startActivity(intent);

			return true;

		case R.id.registromn:

			intent = new Intent(Resultados.this, Registro.class);
			intent.putExtra("moneda", monedausadar);
			intent.putExtra("llamado", "resultados");
			intent.putExtra("ciudadrec", ciudadrec);
			intent.putExtra("langusado", langusado);

			startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				// Login2.logout();
				intent = new Intent(Resultados.this, MiPerfil.class);
				intent.putExtra("moneda", monedausadar);
				intent.putExtra("llamado", "resultados");
				intent.putExtra("ciudadrec", ciudadrec);
				intent.putExtra("langusado", langusado);

				startActivity(intent);

			}// end if
			else {

				intent = new Intent(Resultados.this, Login.class);
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

		AdapterResultados adapter;

		switch (v.getId()) {

		case R.id.grid:

			System.out.println("pinchado grid");

			s = 1;

			// Creo el adapter personalizado
			adapter = new AdapterResultados(this, arrayres, s, monedausadar);
			System.out.println("creado adapter resuoltados grid");

			// Lo aplico
			lista.setAdapter(adapter);

			break;// end case R.id.buscar

		case R.id.lista:

			System.out.println("pinchado lista");

			s = 2;

			// Creo el adapter personalizado
			adapter = new AdapterResultados(this, arrayres, s, monedausadar);
			System.out.println("creado adapter resuoltados lista");

			// Lo aplico
			lista.setAdapter(adapter);

			break;

		case R.id.filtros:

			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time

					final Intent intent = new Intent(Resultados.this,
							Filtros.class);
					intent.putExtra("ciudad", ciudadrec);
					intent.putExtra("moneda", monedausadar);
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

			break;

		}// end switch

	}// end onclick

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

				final Intent intent = new Intent(Resultados.this,
						Resultados.class);
				intent.putExtra("ciudad", ciudadrec);
				intent.putExtra("moneda", monedausadar);
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

		if (datoatras.equals("resultados") && cont == 2) {

			System.out.println("Pulsado atras en resultados");

			// ////////
			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);// se muestre efecto cargando

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Resultados.this,
							Busqueda.class);
					intent.putExtra("moneda", monedausadar);
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

			// /////

		}// end if

		super.onBackPressed();

		VariableAtras.setContador(cont - 1);
		System.out.println("despues de reducir busqueda: " + cont);

	}// end onBackPressed

}// end class Resultados
