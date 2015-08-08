package com.android.inmoprueba1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditarPerfil extends Activity implements
		android.view.View.OnClickListener {

	Menu mn;
	String seleccion, langusado;
	String monedausada = "";// que moneda se usa para mostrar
	String llamadodesde;// saber desde donde ha sido llamado para volver

	// variables que hay que recoger para poder volver a llamar a la vista que
	// ha llamado al login
	String ciudadlog, longitudlog, latitudlog, tituloanunlog, direclog, fechac,
			autorc, textoc, fotocasero, nombrecasero, descripcioncasero;
	Long idlog;
	// //////////

	Bundle reicieveParams;

	String urlreg = "http://www.arcanetsl.com/inmobiliaria/web/movil/api/editar-cliente?apikey=";
	String conec = "N";

	EditText nombrer, passwordr, apellido1, descripcion;
	Button aceptarreg;
	TextView errorreg, comreg;
	ImageView fotoreg;

	Bitmap photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.editarperfil);

		VariableAtras.setDato("editarperfil");
		int cont = VariableAtras.getContador();
		VariableAtras.setContador(cont + 1);

		System.out.println("Entrando en editarperfil");

		// actionbar
		ActionBar actionBar = getActionBar();
		actionBar.show();
		actionBar.setBackgroundDrawable(new ColorDrawable(0x666699));
		// ////////
	
		nombrer = (EditText) this.findViewById(R.id.nombrer);
		passwordr = (EditText) this.findViewById(R.id.passr);
		apellido1 = (EditText) this.findViewById(R.id.apellido1);
		descripcion = (EditText) this.findViewById(R.id.descripcion);
		aceptarreg = (Button) this.findViewById(R.id.editperfil);
		errorreg = (TextView) this.findViewById(R.id.errorreg);
		comreg = (TextView) this.findViewById(R.id.resgistrado);
		fotoreg = (ImageView) this.findViewById(R.id.fotoreg);

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
		nombrer.setText(reicieveParams.getString("nombre"));
		descripcion.setText(reicieveParams.getString("descripcion"));
		String foto = reicieveParams.getString("foto");
		fotoreg = CargarImagenes.downloadImg(fotoreg, foto);
		
		//CARGAR DATOS PERFIL//////////////////////////////

		errorreg.setVisibility(View.GONE);
		comreg.setVisibility(View.GONE);
		aceptarreg.setOnClickListener(this);
		fotoreg.setOnClickListener(this);

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

			intent = new Intent(EditarPerfil.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", langusado);
			startActivity(intent);

			return true;

		case R.id.registromn:

			intent = new Intent(EditarPerfil.this, Registro.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("llamado", "registro");
			intent.putExtra("langusado", langusado);
			startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				// Login2.logout();
				intent = new Intent(EditarPerfil.this, MiPerfil.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("langusado", langusado);
				intent.putExtra("llamado", "registro");
				startActivity(intent);

			}// end if
			else {

				intent = new Intent(EditarPerfil.this, Login.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "registro");
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
				final Intent intent = new Intent(EditarPerfil.this, EditarPerfil.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "EditarPerfil");
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

		switch (v.getId()) {

		case R.id.editperfil:

			// usurec = userlog.getText().toString();
			String
			nombre,pass,
			apellidos,
			descrip;

			nombre = nombrer.getText().toString();
			pass = passwordr.getText().toString();
			apellidos = apellido1.getText().toString();
			descrip = descripcion.getText().toString();

			if (!nombre.equals("") || !apellidos.equals("")) {

				try {// enviar datos de registro

					// create a file to write bitmap data
					File ruta_sd = Environment.getExternalStorageDirectory();

					File f = new File(ruta_sd.getAbsolutePath(), "reg.jpg");
 
					System.out.println("file f: " + f);
					
					String apikey = Variablecookie.getCookie();

					System.out.println("apikey recogida: " + apikey);

					urlreg = urlreg + apikey;// establecer que casero concreto buscamos

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httpost = new HttpPost(urlreg);

					// httpost.setHeader("Content-type", "multipart/form-data");

					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addPart("nombre", new StringBody(nombre));
					entity.addPart("password", new StringBody(pass));
					entity.addPart("apellidos", new StringBody(apellidos));
					entity.addPart("descripcion", new StringBody(descrip));
					if (f.canRead()) {
						entity.addPart("fotografia", new FileBody(f));
					}// end if
					httpost.setEntity(entity);
					HttpResponse response;
					response = httpclient.execute(httpost);

					HttpEntity ent = response.getEntity();/*
														 * y obtenemos una
														 * respuesta
														 */

					String text = EntityUtils.toString(ent);

					System.out.println("respuesta editarperfil: " + text);
					
					f.delete();//borrar la foto creada temporalmente

					if (text.indexOf("OK") != -1) {// si todo correcto

						final String ok = this.getString(R.string.aceptaredit);

						final CharSequence[] options = { ok };

						AlertDialog.Builder builder = new AlertDialog.Builder(
								EditarPerfil.this);

						builder.setTitle(this.getString(R.string.confreg));

						builder.setItems(options,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int item) {

										if (options[item].equals(ok))// confirmar
																		// registro
																		// e ir
																		// a
																		// miperfil

										{

											Intent intent = new Intent(EditarPerfil.this, MiPerfil.class);
											intent.putExtra("moneda", monedausada);
											intent.putExtra("langusado", langusado);
											intent.putExtra("llamado", "editarperfil");
											startActivity(intent);

										}

									}

								});

						builder.show();

					}// end if
					else {// si hay algun error

						errorreg.setVisibility(View.VISIBLE);

					}// end else

				} catch (Exception e) {

					// return "error";}
					System.out.println("Excepcion post registro");
					e.printStackTrace();
				}// end catch

			}// end if
			else {// si hay algun campo vacio

				errorreg.setVisibility(View.VISIBLE);

			}// end else

			break;

		case R.id.fotoreg:

			final String tomar = this.getString(R.string.camara);
			final String galery = this.getString(R.string.galeria);
			final String cancel = this.getString(R.string.cancelar);

			final CharSequence[] options = { tomar, galery, cancel };

			AlertDialog.Builder builder = new AlertDialog.Builder(EditarPerfil.this);

			builder.setTitle(this.getString(R.string.addfoto));

			builder.setItems(options, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int item) {

					if (options[item].equals(tomar))

					{

						Intent cameraIntent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(cameraIntent, 1888);

					}

					else if (options[item].equals(galery))

					{

						Intent i = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

						startActivityForResult(i, 1);
					}

					else if (options[item].equals(cancel)) {

						dialog.dismiss();

					}

				}

			});

			builder.show();

			break;
		}// end switch

	}// end onclick

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			photo = BitmapFactory.decodeFile(picturePath);

			fotoreg.setImageBitmap(photo);
		}

		if (requestCode == 1888 && resultCode == RESULT_OK) {
			// Bitmap photo = (Bitmap) data.getExtras().get("data");
			photo = (Bitmap) data.getExtras().get("data");
			fotoreg.setImageBitmap(photo);

		}

		// ///almacenar foto para subir
		try {

			System.out.println("entrando en guardafoto");

			File ruta_sd = Environment.getExternalStorageDirectory();

			File f = new File(ruta_sd.getAbsolutePath(), "reg.jpg");

			FileOutputStream fos = null;
			fos = new FileOutputStream(f);
			photo.compress(Bitmap.CompressFormat.JPEG, 10, fos);
			fos.flush();

		} catch (IOException ex) {
			System.out.println("escribir en archivo ha petao");
			ex.printStackTrace();
			System.out
					.println("fin exception escribir en archivo----------------");
		}

		// ///

	}

	public void volvervista() {// si el login es valido se vuelve a la pantalla
	// desde la que se llamo

		final ProgressDialog progress;

		if (llamadodesde.equals("busqueda")) {

			progress = ProgressDialog.show(this, "", "Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(EditarPerfil.this,
							Busqueda.class);
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
						final Intent intent = new Intent(EditarPerfil.this,
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
							final Intent intent = new Intent(EditarPerfil.this,
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
								final Intent intent = new Intent(EditarPerfil.this,
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
											EditarPerfil.this, Mapa.class);
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
											EditarPerfil.this, VerComentario.class);
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
												EditarPerfil.this, Host.class);
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

		if (datoatras.equals("editarperfil")) {

			System.out.println("Pulsado atras en registro");

			volvervista();

		}// end if

		super.onBackPressed();

	}// end onBackPressed{

}// end class
