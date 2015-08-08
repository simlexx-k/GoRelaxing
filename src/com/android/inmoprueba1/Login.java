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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity implements
		android.view.View.OnClickListener {

	Menu mn;
	String seleccion, langusado;
	String monedausada = "";// que moneda se usa para mostrar
	String llamadodesde;// saber desde donde ha sido llamado para volver

	String apikey, usurec, passrec, llamadodesdehost;

	// variables que hay que recoger para poder volver a llamar a la vista que
	// ha llamado al login
	String ciudadlog, longitudlog, latitudlog, tituloanunlog, direclog, fechac,
			autorc, textoc, fotocasero, nombrecasero, descripcioncasero;
	Long idlog;
	// //////////

	Bundle reicieveParams;

	String urllogin = "http://www.arcanetsl.com/inmobiliaria/web/movil/login";
	String conec = "N";
	//
	EditText userlog, passlog;
	Button aceptarlog;
	TextView errorlog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.login);

		VariableAtras.setDato("login");
		int cont = VariableAtras.getContador();
		VariableAtras.setContador(cont + 1);

		System.out.println("Entrando en Login");

		// actionbar
		ActionBar actionBar = getActionBar();
		actionBar.show();
		actionBar.setBackgroundDrawable(new ColorDrawable(0x666699));
		// ////////

		userlog = (EditText) this.findViewById(R.id.usuario);
		passlog = (EditText) this.findViewById(R.id.pass);
		aceptarlog = (Button) this.findViewById(R.id.login);
		errorlog = (TextView) this.findViewById(R.id.errorlogin);

		reicieveParams = getIntent().getExtras();// recibiendo paramentros de

		monedausada = reicieveParams.getString("moneda");
		llamadodesde = reicieveParams.getString("llamado");
		langusado = reicieveParams.getString("langusado");

		System.out.println("langusado en buscar: " + langusado);
		recogerParametros();// recoger los parametros en funcion de la pantalla
							// desde la que ha sido llamado

		// saber si esta conectado
		conec = Logado.comprobarlog(this);

		// ///////////////////////

		errorlog.setVisibility(View.GONE);
		aceptarlog.setOnClickListener(this);

	}// end oncreate

	public void recogerParametros() {

		if (llamadodesde.equals("resultados")) {

			ciudadlog = reicieveParams.getString("ciudadrec");

		}// end if1
		else {

			if (llamadodesde.equals("resultado")) {

				idlog = reicieveParams.getLong("id");

			}// end if2
			else {

				if (llamadodesde.equals("comentarios")) {

					idlog = reicieveParams.getLong("id");

				}// end if3
				else {

					if (llamadodesde.equals("mapa")) {

						idlog = reicieveParams.getLong("id");
						longitudlog = reicieveParams.getString("longitud");
						latitudlog = reicieveParams.getString("latitud");
						tituloanunlog = reicieveParams
								.getString("titulo_anuncio");
						direclog = reicieveParams.getString("direccion");

					}// end if4
					else {
						if (llamadodesde.equals("vercomentario")) {

							idlog = reicieveParams.getLong("id");
							fechac = reicieveParams.getString("fechac");
							autorc = reicieveParams.getString("autorc");
							textoc = reicieveParams.getString("textoc");

						}// end if5
						else {

							if (llamadodesde.equals("host")) {

								idlog = reicieveParams.getLong("id");
								nombrecasero = reicieveParams
										.getString("nombrecasero");
								descripcioncasero = reicieveParams
										.getString("descripcioncas");
								fotocasero = reicieveParams
										.getString("fotocasero");
							}// end if 6

						}// end else5

					}// end else4

				}// end else3

			}// end else2

		}// end else1

	}// end recogerParametros

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

		switch (item.getItemId()) {

		case R.id.buscarmn:

			intent = new Intent(Login.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", langusado);
			startActivity(intent);

			return true;

			 case R.id.registromn:

				 intent = new Intent(Login.this, Registro.class);
					intent.putExtra("moneda", monedausada);
					intent.putExtra("llamado", "login");
					intent.putExtra("langusado", langusado);
					startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				//Login2.logout();
				intent = new Intent(Login.this, MiPerfil.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("langusado", langusado);
				intent.putExtra("llamado", "login");
				startActivity(intent);

			}// end if
			else {

				intent = new Intent(Login.this, Login.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "login");
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

		}

	}// end menu

	public void languagechange(String lang) {

		Locale locale = new Locale(lang);
		Locale.setDefault(locale);

		System.out.println("locale: " + locale);

		Configuration config = new Configuration();
		config.locale = locale;

		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

		updatetext();// actualizar idioma pantalla

		guardaselecidioma(lang);// guardar la seleccion del idioma para la
								// proxima vez que abra la app

	}// end languagechange

	public void updatetext() {

		System.out.println("mn: " + mn);

		final ProgressDialog progresss = ProgressDialog.show(this, "",
				"Procesing...", true);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// do the thing that takes a long time
				final Intent intent = new Intent(Login.this, Login.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "login");
				intent.putExtra("langusado", langusado);
				startActivity(intent);

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progresss.dismiss();
					}
				});
			}
		}).start();

	}// end updatetext

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

	public void loginout() {

		MenuItem txtperfil = mn.findItem(R.id.perfilmn);

		if (conec.equals("S")) {

			txtperfil.setTitle(R.string.perfil);

		}// end if
		else {

			txtperfil.setTitle(R.string.miperfil);

		}// end else

	}// end loginout

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		JSONParser parser = new JSONParser();

		switch (v.getId()) {

		case R.id.login:

			usurec = userlog.getText().toString();
			passrec = passlog.getText().toString();

			try {// enviar usuario y pass a la url para que lo compruebe

				HttpClient httpclient = new DefaultHttpClient();

				System.out.println("httpclient enviarcoment: " + httpclient);

				/*
				 * Creamos el objeto de HttpClient que nos permitira conectarnos
				 * mediante peticiones http
				 */

				HttpPost httppost = new HttpPost(urllogin);

				System.out.println("httppost enviarcoment: " + httppost);

				/*
				 * El objeto HttpPost permite que enviemos una peticion de tipo
				 * POST a una URL especificada
				 */

				// AÑADIR PARAMETROS

				List<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("usuario", usurec));

				params.add(new BasicNameValuePair("password", passrec));

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

				JSONObject obj = new JSONObject();

				obj = (JSONObject) parser.parse(text);

				apikey = (String) obj.get("apikey");

				System.out.println("apikey: " + apikey);

				if (!apikey.equals("ERROR")) {
					// si el login es correcto se almacena la apikey y se va a
					// la pantalla desde la que se ha llamado a login
					Variablecookie.setCookie(apikey);// guardar la apikey en una
														// variable global
					volvervista();
				}// end if 
				else {
					// si el login no es correcto se queda en la pantalla de
					// login
					userlog.setText("");
					passlog.setText("");
					userlog.setHint(this.getString(R.string.usuario));
					userlog.requestFocus();
					passlog.setHint(this.getString(R.string.password));

					errorlog.setVisibility(View.VISIBLE);

				}// end else

			} catch (Exception e) {

				// return "error";}
				System.out.println("Excepcion post apikey");
				e.printStackTrace();
			}// end catch

			break;

		}// end switch

	}// end onClick

	public void volvervista() {// si el login es valido se vuelve a la pantalla
								// desde la que se llamo

		final ProgressDialog progress;

		if (llamadodesde.equals("busqueda")) {

			progress = ProgressDialog.show(this, "", "Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Login.this, Busqueda.class);
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

		}// end if busqueda
		else {

			if (llamadodesde.equals("resultados")) {

				progress = ProgressDialog.show(this, "", "Procesing...", true);

				new Thread(new Runnable() {
					@Override
					public void run() {
						// do the thing that takes a long time
						final Intent intent = new Intent(Login.this,
								Resultados.class);
						intent.putExtra("moneda", monedausada);
						intent.putExtra("ciudad", ciudadlog);
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

			}// end if resultados
			else {

				if (llamadodesde.equals("resultado")) {

					progress = ProgressDialog.show(this, "", "Procesing...",
							true);

					new Thread(new Runnable() {
						@Override
						public void run() {
							// do the thing that takes a long time
							final Intent intent = new Intent(Login.this,
									Resultado.class);
							intent.putExtra("moneda", monedausada);
							intent.putExtra("id", idlog);
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

				}// end if resultado
				else {

					if (llamadodesde.equals("comentarios")) {

						progress = ProgressDialog.show(this, "",
								"Procesing...", true);

						new Thread(new Runnable() {
							@Override
							public void run() {
								// do the thing that takes a long time
								final Intent intent = new Intent(Login.this,
										Comentarios.class);
								intent.putExtra("moneda", monedausada);
								intent.putExtra("id", idlog);
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

					}// end if comentarios
					else {

						if (llamadodesde.equals("mapa")) {

							progress = ProgressDialog.show(this, "",
									"Procesing...", true);

							new Thread(new Runnable() {
								@Override
								public void run() {
									// do the thing that takes a long time
									final Intent intent = new Intent(
											Login.this, Mapa.class);
									intent.putExtra("id", idlog);
									intent.putExtra("moneda", monedausada);
									intent.putExtra("longitud", longitudlog);
									intent.putExtra("latitud", latitudlog);
									intent.putExtra("titulo_anuncio",
											tituloanunlog);
									intent.putExtra("direccion", direclog);
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

						}// end if mapa
						if (llamadodesde.equals("vercomentario")) {

							final ProgressDialog progresss = ProgressDialog
									.show(this, "", "Procesing...", true);

							new Thread(new Runnable() {
								@Override
								public void run() {
									// do the thing that takes a long time
									final Intent intent = new Intent(
											Login.this, VerComentario.class);
									intent.putExtra("id", idlog);
									intent.putExtra("fechac", fechac);
									intent.putExtra("autorc", autorc);
									intent.putExtra("textoc", textoc);
									intent.putExtra("moneda", monedausada);
									intent.putExtra("langusado", langusado);

									startActivity(intent);

									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											progresss.dismiss();
										}
									});
								}
							}).start();

						}// end vercomentario
						else {

							if (llamadodesde.equals("host")) {

								final ProgressDialog progresss = ProgressDialog
										.show(this, "", "Procesing...", true);

								new Thread(new Runnable() {
									@Override
									public void run() {
										// do the thing that takes a long time
										final Intent intent = new Intent(
												Login.this, Host.class);
										intent.putExtra("id", idlog);
										intent.putExtra("fotocasero",
												fotocasero);
										intent.putExtra("nombrecasero",
												nombrecasero);
										intent.putExtra("descripcioncas",
												descripcioncasero);
										intent.putExtra("moneda", monedausada);
										intent.putExtra("langusado", langusado);

										startActivity(intent);

										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												progresss.dismiss();
											}
										});
									}
								}).start();

							}// end vercomentarios
						}// end else vercoment

					}// end else comentarios

				}// end else resultado

			}// end else resultados

		}// end else busqueda

	}// una vez logado volver a la vista que ha llamado

	public void onBackPressed() {// si se pulsa el boton atras del tlf
		// Write your code here

		String datoatras = VariableAtras.getDato();

		if (datoatras.equals("login")) {

			System.out.println("Pulsado atras en login");

			volvervista();

		}// end if

		super.onBackPressed();

	}// end onBackPressed

}// end class