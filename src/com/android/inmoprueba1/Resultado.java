package com.android.inmoprueba1;

import java.text.DecimalFormat;
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

import android.os.Bundle; 
import android.os.StrictMode;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;

public class Resultado extends Resultados implements
		android.view.View.OnClickListener {
	
	List<String> imgs;

	ImageView fotoprincipalMA;
	Button comenthost;
	EditText introcomenthost;
	TextView precioRes, euroRes, descripcionRes, caracteristicasRes,
			comentariosRes, popularidadRes, disponibilidadRes, mapaRes,
			descripcionjson, caracteristicasjson, comentariosjson, avisocambio,
			popularidadjson, nombrehost, erroranfi, camast, personast,
			metrosdirect;
	ImageView fotohost, camasimgt, personasimgt;

	Bundle reicieveParams;
	WebView calendario;

	Menu mn;
	String langusado;
	Long id;// id del inmueble seleccionado

	// variables descripcion
	String titulo_anuncio = "";
	String descripcion_anuncio = "";

	// variables caracteristicas
	String direccion = "";
	String metros = "";
	String ciudad = "";
	String provincia = "";
	String pais = "";
	String tipo_propiedad = "";
	String capacidad = "";
	String dormitorios = "";
	//
	String precio = "";
	String latitud = "";
	String longitud = "";

	// informacion casero
	Long idcasero;
	String nombrecasero = "";
	String descripcioncasero = "";
	String fotocasero = "";
	// ///////////

	Double tipoChange;// saber el tipo de cambio al cambiar de moneda

	String monedausada = "";// que moneda se usa para mostrar

	String conec = "N";

	// viewpager
	PagerAdapter pageAdapter;
	ViewPager pager;

	// ////////////

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultado);

		// ///////////
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		// /////////////

		VariableAtras.setDato("resultado");// controlar boton atras telefono
		VariableAtras.setContador(3);

		precioRes = (TextView) findViewById(R.id.preciores);
		euroRes = (TextView) findViewById(R.id.eurores);
		descripcionRes = (TextView) findViewById(R.id.descripcionres);
		caracteristicasRes = (TextView) findViewById(R.id.caracteristicasres);
		comentariosRes = (TextView) findViewById(R.id.comentariosres);
		mapaRes = (TextView) findViewById(R.id.mapares);
		popularidadRes = (TextView) findViewById(R.id.popularidadres);
		disponibilidadRes = (TextView) findViewById(R.id.disponibilidadres);

		descripcionjson = (TextView) findViewById(R.id.descripcionjson);
		caracteristicasjson = (TextView) findViewById(R.id.caracteristicasjson);
		camast = (TextView) findViewById(R.id.camas);
		personast = (TextView) findViewById(R.id.personas);
		camasimgt = (ImageView) findViewById(R.id.camasimg);
		personasimgt = (ImageView) findViewById(R.id.personasimg);
		metrosdirect = (TextView) findViewById(R.id.metrosdirec);
		popularidadjson = (TextView) findViewById(R.id.popularidadjson);
		fotohost = (ImageView) findViewById(R.id.fotohost);
		nombrehost = (TextView) findViewById(R.id.nombrehost);

		avisocambio = (TextView) findViewById(R.id.cambiomoneda);

		introcomenthost = (EditText) findViewById(R.id.introcomenthost);
		comenthost = (Button) findViewById(R.id.botonsendhost);
		erroranfi = (TextView) findViewById(R.id.avisoenviohost);

		// recibir el id de la propiedad seleccionada
		reicieveParams = getIntent().getExtras();
		id = reicieveParams.getLong("id");
		System.out.println("id recogido en resultado: " + id);
		//

		comentariosRes.setOnClickListener(this);
		mapaRes.setOnClickListener(this);
		fotohost.setOnClickListener(this);
		comenthost.setOnClickListener(this);

		// saber si esta conectado
		conec = Logado.comprobarlog(this);

		// obtener datos del id seleccionado///////////////////////////
		langusado = reicieveParams.getString("langusado");

		obtenerdatos(reicieveParams.getLong("id"));

		// /////////

		monedausada = reicieveParams.getString("moneda");

		System.out.println("monedausada recibida en resultado: " + monedausada);
		System.out.println("langusado recibida en resultado: " + langusado);

		cambioMoneda(monedausada);// establecer que moneda se usa y si no es la
									// por defecto realizar el cambio

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

			intent = new Intent(Resultado.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", langusado);
			startActivity(intent);

			return true;

			case R.id.registromn:

				intent = new Intent(Resultado.this, Registro.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "resultado");
				intent.putExtra("id", id);
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				//Login2.logout();
				intent = new Intent(Resultado.this, MiPerfil.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "resultado");
				intent.putExtra("id", id);
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			}// end if
			else {

				intent = new Intent(Resultado.this, Login.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "resultado");
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

			cambioMoneda(monedausada);

			return true;

		case R.id.usdmn:

			monedausada = "USD";
			System.out.println("monedausada: " + monedausada);

			cambioMoneda(monedausada);

			return true;

		case R.id.gbpmn:

			monedausada = "GBP";
			System.out.println("monedausada: " + monedausada);

			cambioMoneda(monedausada);

			return true;

		default:

			return true;

		}

	}// end menu

	public void onClick(View v) {

		final ProgressDialog progress;

		switch (v.getId()) {

		case R.id.comentariosres:// si se pulsa en comentarios muestra los
									// coments
			
System.out.println("vamos a las imgs........................");
			
			Intent intentimg = new Intent(Resultado.this, ImgResultado.class);
			intentimg.putExtra("id", id);
			intentimg.putExtra("moneda", monedausada);
			intentimg.putExtra("langusado", langusado);
			startActivity(intentimg);

			/*progress = ProgressDialog.show(this, "", "Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Resultado.this,
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
			}).start();*/

			break;

		case R.id.mapares:// si se pulsa en mapa abre el mapa con la
							// localizacion

			progress = ProgressDialog.show(this, "", "Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent2 = new Intent(Resultado.this,
							Mapa.class);
					intent2.putExtra("id", id);
					intent2.putExtra("latitud", latitud);
					intent2.putExtra("longitud", longitud);
					intent2.putExtra("titulo_anuncio", titulo_anuncio);
					intent2.putExtra("direccion", direccion);
					intent2.putExtra("moneda", monedausada);
					intent2.putExtra("langusado", langusado);

					startActivity(intent2);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progress.dismiss();
						}
					});
				}
			}).start();

			break;

		case R.id.fotohost:// si se pulsa en comentarios muestra los
			// coments

			System.out.println("pulsado host");

			progress = ProgressDialog.show(this, "", "Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Resultado.this, Host.class);
					intent.putExtra("id", id);
					intent.putExtra("nombrecasero", nombrecasero);
					intent.putExtra("descripcioncas", descripcioncasero);
					intent.putExtra("fotocasero", fotocasero);
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

			break;

		case R.id.botonsendhost:// enviar mail al casero
						

			progress = ProgressDialog.show(this, "", "Procesing...", true);

			final String mensaje = introcomenthost.getText().toString();

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					// enviar mail al casero

					enivaranfi(mensaje);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progress.dismiss();
						}
					});
				}
			}).start();
			
			introcomenthost.setText("");
			introcomenthost.setHint(this.getString(R.string.comenthost));

			break;

		}// end switch

	}// end onclick

	public void obtenerdatos(Long id) {// recibe el id de la propiedad
										// seleccionada

		Json resultado = new Json();
		JSONObject c = new JSONObject();

		try {

			String url = "http://www.arcanetsl.com/inmobiliaria/web/ver-inmueble/";

			url = url + id + "/" + langusado;// establecer que inmueble concreto buscamos

			System.out.println("URL inmueble a buscar: " + url);

			c = resultado.readJSONOBJECT(url);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ha petado el retorno de readjson");
			e.printStackTrace();
		}

		System.out.println("array creado y devuelto");

		try {

			System.out.println("Vamos a sacar los datos que necesitamos");

			// recoger id para comparar con el seleccionado

			// descripcion/////////////////////////////////////////////
			titulo_anuncio = (String) c.get("titulo_anuncio");
			System.out.println("titulo_anuncio: " + titulo_anuncio);

			descripcion_anuncio = (String) c.get("descripcion_anuncio");
			System.out.println("descripcion_anuncio: " + descripcion_anuncio);

			// caracteristicas///////////////////////////////////////
			direccion = (String) c.get("direccion");
			System.out.println("direccion: " + direccion);

			Long metrosl = (Long) c.get("metros");
			metros = (Long.toString(metrosl));
			System.out.println("metros: " + metros);

			ciudad = (String) c.get("ciudad");
			System.out.println("ciudad: " + ciudad);

			provincia = (String) c.get("provincia");
			System.out.println("provincia: " + provincia);

			pais = (String) c.get("pais");
			System.out.println("pais: " + pais);

			tipo_propiedad = (String) c.get("tipo_propiedad");
			System.out.println("tipo_propiedad: " + tipo_propiedad);

			Long capacidad1 = (Long) c.get("capacidad");
			capacidad = (Long.toString(capacidad1));
			System.out.println("capacidad: " + capacidad);

			Long dormitorios1 = (Long) c.get("dormitorios");
			dormitorios = (Long.toString(dormitorios1));
			System.out.println("dormitorios: " + dormitorios);

			// precio/////////////////////////////////////////////
			Long preciol = (Long) c.get("precio");
			precio = (Long.toString(preciol));
			System.out.println("precio: " + precio);

			// coordenadas//////////////////////////////////////
			latitud = (String) c.get("latitud");
			longitud = (String) c.get("longitud");
			System.out.println("coordenadas: " + latitud + "," + longitud);

			// casero//////////////////////////////////
			idcasero = (Long) c.get("casero");
			System.out.println("precio: " + precio);

			// fotos/////////////////////////////////

			JSONArray fotos = (JSONArray) c.get("fotos");

			String foto = "";// foto sacada del array
			//List<String> imgs = new ArrayList<String>();// lista de fotos para
														// el viewpager
			
			imgs = new ArrayList<String>();// lista de fotos para

			System.out.println("sacando jsonarray fotos: " + fotos.size());

			for (int j = 0; j < fotos.size(); j++) {

				foto = (String) fotos.get(j);
				System.out.println("foto del jsonobject: " + foto);

				// if (foto.endsWith(".jpg") || foto.endsWith(".JPG")) {// si
				// hay
				// alguna
				// foto

				imgs.add(foto); // cargar foto en el list
				System.out.println("cargada foto en el list: " + j);

				// } // end if

			}// end for
			System.out.println("salida del for");

			// cargando el viewpager
			try {

				pageAdapter = new CustomPageAdapter(this, imgs);
				pager = (ViewPager) findViewById(R.id.viewPager);
				pager.setAdapter(pageAdapter);			
					

				// solucion problema pageadapter
				pager.setOnPageChangeListener(new OnPageChangeListener() {
			        private int prevPage = -1;
			        @Override
			        public void onPageSelected(int position) {	
			        	
			        	
			        	
			        }

			        @Override
			        public void onPageScrolled(int arg0, float arg1, int arg2) {
			        	
			        	
			        }

			        @Override
			        public void onPageScrollStateChanged(int arg0) {
			        	
			        	System.out.println("arg0: " + arg0);

			        	if (arg0 == ViewPager.SCROLLBAR_POSITION_LEFT) {
			        		
			        		
			        		/*prevPage = pager.getCurrentItem();
							pager.setCurrentItem(prevPage + 1);
			                //pager.setCurrentItem(nextPage);*/
			            }
			        	if (arg0 == ViewPager.SCROLLBAR_POSITION_RIGHT) {
			        		/*prevPage = pager.getCurrentItem();
			        		System.out.println("prevPage: " + prevPage);		        			
			        			pager.setCurrentItem(prevPage - 1);*/
							
			                //pager.setCurrentItem(prevPage);
			            }
		
			        }
			    });			

				// ///////////////////
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// mostrar precio
			precioRes.setText(precio);
			// mostrar datos
			mostrardesc();
			mostrarcarac();
			mostrardispo();
			mostraranfi(idcasero);
			//

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ha petado al sacar los datos del json");
			e.printStackTrace();
		}// end catch

	}// end obtenerdatos

	public void mostrardesc() {

		descripcionjson.setText(titulo_anuncio + "\n" + "(" + tipo_propiedad
				+ ")" + "\n\n" + descripcion_anuncio);

	}// end mostardesc

	public void mostrarcarac() {

		camast.setText(dormitorios + "  ");

		personast.setText(capacidad + "  ");

		metrosdirect.setText(metros + " " + this.getString(R.string.metros)
				+ "\n\n" + this.getString(R.string.direccion) + direccion
				+ ", " + ciudad + ", " + pais);

	}// end mostarcarac

	public void mostrardispo() {

		String urlcalendar = "http://www.arcanetsl.com/inmobiliaria/web/calendario-movil/";

		urlcalendar = urlcalendar + id + "/" + langusado;// cargar el calendario
															// del inmueble
		// selec con el idioma selec

		System.out.println("urlcalendar: " + urlcalendar);

		calendario = (WebView) this.findViewById(R.id.urlcalendar);
		WebSettings webSettings = calendario.getSettings();
		webSettings.setJavaScriptEnabled(true);
		calendario.loadUrl(urlcalendar);

	}// end mostrardispo

	public void mostraranfi(Long idhost) {// mostrar info anfitrion

		Json resultado = new Json();
		JSONObject c = new JSONObject();

		try {

			String url = "http://www.arcanetsl.com/inmobiliaria/web/perfil-casero/";

			url = url + idhost;// establecer que casero concreto buscamos

			System.out.println("URL casero a buscar: " + url);

			c = resultado.readJSONOBJECT(url);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ha petado el retorno de readjson");
			e.printStackTrace();
		}

		System.out.println("array creado y devuelto");

		try {

			System.out.println("Vamos a sacar los datos que necesitamos");

			// descripcion/////////////////////////////////////////////
			nombrecasero = (String) c.get("nombre");
			System.out.println("nombrecasero: " + nombrecasero);

			descripcioncasero = (String) c.get("descripcion");
			System.out.println("descripcioncasero: " + descripcioncasero);

			fotocasero = (String) c.get("foto");
			System.out.println("fotocasero: " + fotocasero);

			fotohost = CargarImagenes.downloadImg(fotohost, fotocasero);// buscar
																		// la
																		// foto
																		// que
																		// necesitamos

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out
					.println("ha petado al sacar los datos del casero del json");
			e.printStackTrace();
		}// end catch

		nombrehost.setText(this.getString(R.string.anfitrion2) + " "
				+ nombrecasero);

		if (conec.equals("S")) {

			// si esta logado
			introcomenthost.setVisibility(View.VISIBLE);
			comenthost.setVisibility(View.VISIBLE);
			erroranfi.setVisibility(View.GONE);

		}// end if
		else {

			// si no esta logado
			introcomenthost.setVisibility(View.GONE);
			comenthost.setVisibility(View.GONE);
			erroranfi.setVisibility(View.VISIBLE);

		}// end else

	}// end mostraranfi

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
				final Intent intent = new Intent(Resultado.this,
						Resultado.class);
				intent.putExtra("id", id);// enviarle a la pagina
											// de resultado el id
											// seleccionado
				intent.putExtra("langusado", langusado);
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

			GuardaIdioma.onClickGuardar(lang, this);

		} catch (Exception e) {

			System.out
					.println("Ha petado la llamada a guarda idioma desde main");
			System.out.println(e);
			System.out
					.println("fin excepcion guarda idioma main---------------------------");

		}// end try catch*/

	}// guardaselecidioma

	public void cambioMoneda(String tipomoneda) {

		Double preciocambiado, preciocalculo;
		String preciocambiadostr;

		if (tipomoneda.equals("EUR")) {

			euroRes.setText("€");

			precioRes.setText(precio);

			avisocambio.setVisibility(View.GONE);

			System.out.println("ocultado aviso");

		} else {

			System.out.println("llamando a CambioMoneda con tipomoneda: "
					+ tipomoneda);

			tipoChange = CambioMoneda.getcambiomoneda(tipomoneda);

			System.out.println("tipo de cambio devuelto en busqueda: "
					+ tipoChange);

			preciocalculo = Double.parseDouble(precio);

			preciocambiado = preciocalculo * tipoChange;

			System.out.println("preciocambiado: " + preciocambiado);

			// cambio format para obtener solo 2 decimales
			DecimalFormat df = new DecimalFormat("######.##");

			preciocambiadostr = df.format(preciocambiado);

			if (tipomoneda.equals("USD")) {

				euroRes.setText("$");

				precioRes.setText(preciocambiadostr);

				avisocambio.setVisibility(View.VISIBLE);// mostrar aviso de
														// cambio de moneda

				System.out.println("mostrado aviso");

			}// end if USD
			if (tipomoneda.equals("GBP")) {

				euroRes.setText("£");

				precioRes.setText(preciocambiadostr);

				avisocambio.setVisibility(View.VISIBLE);// mostrar aviso de
														// cambio de moneda

				System.out.println("mostrado aviso");

			}// end if GBP

		}// end if EUR

	}// end cambio moneda

	public void enivaranfi(String msg) {// enviar una consulta al casero
		
		System.out.println("msg: " + msg);

		if (!msg.equals("")) {

			String urlsend = "http://www.arcanetsl.com/inmobiliaria/web/movil/api/contacto-casero?apikey=";

			String apikey = Variablecookie.getCookie();

			System.out.println("apikey recogida: " + apikey);

			urlsend = urlsend + apikey;// añadir el apikey a la url

			String idinmu = id.toString();// pasar a string el id para el post
											// de
											// parametros

			String asunto = this.getString(R.string.asuntoemail);

			try {

				HttpClient httpclient = new DefaultHttpClient();

				/*
				 * Creamos el objeto de HttpClient que nos permitira conectarnos
				 * mediante peticiones http
				 */

				HttpPost httppost = new HttpPost(urlsend);

				/*
				 * El objeto HttpPost permite que enviemos una peticion de tipo
				 * POST a una URL especificada
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

				params.add(new BasicNameValuePair("asunto", asunto));

				params.add(new BasicNameValuePair("consulta", msg));

				params.add(new BasicNameValuePair("idinmueble", idinmu));

				System.out.println("params enviarcoment: " + params);
				/*
				 * Una vez añadidos los parametros actualizamos la entidad de
				 * httppost, esto quiere decir en pocas palabras anexamos los
				 * parametros al objeto para que al enviarse al servidor envien
				 * los datos que hemos añadido
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

		}// end if
		else{
			
			introcomenthost.setHint(this.getString(R.string.errorcomenthost));
			
		}//end else

	}// end enivaranfi

	public void onBackPressed() {// si se pulsa el boton atras del tlf
		// Write your code here

		String datoatras = VariableAtras.getDato();

		int cont = VariableAtras.getContador();

		System.out.println("Pulsado atras en busqueda: " + cont);

		if (datoatras.equals("resultado") && cont == 3) {

			VariableAtras.setDato("resultados");

			System.out.println("Pulsado atras en resultado");

			// /////
			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);// se muestre efecto cargando

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Resultado.this,
							Resultados.class);

					intent.putExtra("ciudad", ciudad);
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

	}// end onBackPressed

}// end class resultado
