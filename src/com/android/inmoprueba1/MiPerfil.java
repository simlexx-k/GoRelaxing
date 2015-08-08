package com.android.inmoprueba1;

import java.util.Locale;

import org.json.simple.JSONObject;

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
import android.widget.ImageView;
import android.widget.TextView;

public class MiPerfil extends Activity implements
		android.view.View.OnClickListener {

	Button closesesion;
	TextView name, description;
	ImageView fotocliente;

	Menu mn;
	String seleccion, langusado;
	String monedausada = "";// que moneda se usa para mostrar
	String llamadodesde;// saber desde donde ha sido llamado para volver

	String apikey, usurec, passrec, llamadodesdehost;
	String fotocliente2;

	// variables que hay que recoger para poder volver a llamar a la vista que
	// ha llamado al login
	String ciudadlog, longitudlog, latitudlog, tituloanunlog, direclog, fechac,
			autorc, textoc, fotocasero, nombrecasero, descripcioncasero;
	Long idlog;
	// //////////

	Bundle reicieveParams;

	String conec = "N";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.miperfil);

		VariableAtras.setDato("miperfil");
		int cont = VariableAtras.getContador();
		VariableAtras.setContador(cont + 1);

		System.out.println("Entrando en miperfil");

		// actionbar
		ActionBar actionBar = getActionBar();
		actionBar.show();
		actionBar.setBackgroundDrawable(new ColorDrawable(0x666699));
		// ////////

		closesesion = (Button) this.findViewById(R.id.cerrarsesion);
		name = (TextView) this.findViewById(R.id.nombreanfitrion);
		description = (TextView) this.findViewById(R.id.descripcionanfi);
		fotocliente = (ImageView) this.findViewById(R.id.fotocliente);
		
		///////////////////////////
		
		mostrarinfo();
		
		/////////////////////////

		reicieveParams = getIntent().getExtras();// recibiendo paramentros de

		monedausada = reicieveParams.getString("moneda");
		llamadodesde = reicieveParams.getString("llamado");
		langusado = reicieveParams.getString("langusado");

		System.out.println("langusado en buscar: " + langusado);
		recogerParametros();// recoger los parametros en funcion de la pantalla
							// desde la que ha sido llamado

		// saber si esta conectado
		conec = Logado.comprobarlog(this);

		closesesion.setOnClickListener(this);

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

			intent = new Intent(MiPerfil.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", langusado);
			startActivity(intent);

			return true;

			case R.id.registromn:

				intent = new Intent(MiPerfil.this, Registro.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				//Login2.logout();
				intent = new Intent(MiPerfil.this, EditarPerfil.class);
				intent.putExtra("nombre", name.getText());
				intent.putExtra("descripcion", description.getText());
				intent.putExtra("foto", fotocliente2);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "miperfil");
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			}// end if
			else {

				intent = new Intent(MiPerfil.this, Login.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "miperfil");
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
				final Intent intent = new Intent(MiPerfil.this, MiPerfil.class);
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

			txtperfil.setTitle(R.string.editperfil);

		}// end if
		else {

			txtperfil.setTitle(R.string.miperfil);

		}// end else

	}// end loginout

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.cerrarsesion:
			
			final ProgressDialog progress;

			progress = ProgressDialog.show(this, "", "Procesing...", true);

						new Thread(new Runnable() {
							@Override
							public void run() {
								// do the thing that takes a long time
								Login2.logout();
								final Intent intent = new Intent(MiPerfil.this, Busqueda.class);
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

		}// end swithc

	}// end onclick

	public void volvervista() {// si el login es valido se vuelve a la pantalla
		// desde la que se llamo

		final ProgressDialog progress;

		if (llamadodesde.equals("busqueda")) {

			progress = ProgressDialog.show(this, "", "Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(MiPerfil.this, Busqueda.class);
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
						final Intent intent = new Intent(MiPerfil.this,
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
							final Intent intent = new Intent(MiPerfil.this,
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
								final Intent intent = new Intent(MiPerfil.this,
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
											MiPerfil.this, Mapa.class);
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
											MiPerfil.this, VerComentario.class);
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
												MiPerfil.this, Host.class);
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

		if (datoatras.equals("miperfil")) {

			System.out.println("Pulsado atras en miperfil");

			volvervista();

		}// end if

		super.onBackPressed();

	}// end onBackPressed
	
	public void mostrarinfo() {// mostrar info cliente falta poner "Long idclient"

		Json resultado = new Json();
		JSONObject c = new JSONObject();

		try {

			String url = "http://www.arcanetsl.com/inmobiliaria/web/movil/api/perfil-privado?apikey=";
			
			String apikey = Variablecookie.getCookie();

			System.out.println("apikey recogida: " + apikey);

			url = url + apikey;// establecer que casero concreto buscamos

			System.out.println("URL cliente a buscar: " + url);

			c = resultado.readJSONOBJECT(url);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ha petado el retorno de readjson");
			e.printStackTrace();
		}

		System.out.println("array creado y devuelto");

		try {

			System.out.println("Vamos a sacar los datos que necesitamos");

			name.setText((String) c.get("nombre"));

			description.setText((String) c.get("descripcion"));

			fotocliente2 = (String) c.get("foto");
			System.out.println("fotocasero: " + fotocliente);

			fotocliente = CargarImagenes.downloadImg(fotocliente, fotocliente2);// buscar
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


	}// end mostraranfi

}// end class
