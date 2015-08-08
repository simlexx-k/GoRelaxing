package com.android.inmoprueba1;

import java.text.DecimalFormat;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Host extends Resultado implements
		android.view.View.OnClickListener {

	Long id;

	ImageView fotohostver;
	TextView descripcionver, nombrehostver;

	String fotocasero, nombrecasero, descripcioncasero;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host);

		VariableAtras.setDato("host");// controlar boton atras telefono
		int cont = VariableAtras.getContador();
		VariableAtras.setContador(cont + 1);

		nombrehostver = (TextView) findViewById(R.id.nombrehostver);
		fotohostver = (ImageView) findViewById(R.id.fotohostver);
		descripcionver = (TextView) findViewById(R.id.descripcionver);

		// recibir el id de la propiedad seleccionada
		reicieveParams = getIntent().getExtras();

		id = reicieveParams.getLong("id");
		nombrecasero = reicieveParams.getString("nombrecasero");
		descripcioncasero = reicieveParams.getString("descripcioncas");
		fotocasero = reicieveParams.getString("fotocasero");
		System.out.println("fotocasero en host" + fotocasero);
		langusado = reicieveParams.getString("langusado");

		System.out.println("langusado en buscar: " + langusado);
		//

		// cargar foto casero
		fotohostver = CargarImagenes.downloadImg(fotohostver, fotocasero);// buscar
																			// la
		// foto que
		// necesitamos

		nombrehostver.setText(this.getString(R.string.anfitrion2) + " "
				+ nombrecasero);

		descripcionver.setText(descripcioncasero);

		monedausada = reicieveParams.getString("moneda");

		System.out.println("monedausada recibida en host: " + monedausada);

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

			intent = new Intent(Host.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", langusado);
			startActivity(intent);

			return true;

			case R.id.registromn:

				intent = new Intent(Host.this, Registro.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("nombrecasero", nombrecasero);
				intent.putExtra("descripcioncas", descripcioncasero);
				intent.putExtra("fotocasero", fotocasero);
				intent.putExtra("id", id);
				intent.putExtra("llamado", "host");
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				//Login2.logout();
				intent = new Intent(Host.this, MiPerfil.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("nombrecasero", nombrecasero);
				intent.putExtra("descripcioncas", descripcioncasero);
				intent.putExtra("fotocasero", fotocasero);
				intent.putExtra("id", id);
				intent.putExtra("llamado", "host");
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			}// end if
			else {

				intent = new Intent(Host.this, Login.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("nombrecasero", nombrecasero);
				intent.putExtra("descripcioncas", descripcioncasero);
				intent.putExtra("fotocasero", fotocasero);
				intent.putExtra("id", id);
				intent.putExtra("llamado", "host");
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

		case R.id.botonsendhost:// si se pulsa en comentarios muestra los
			// coments

			progress = ProgressDialog.show(this, "", "Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time

					// codigo para enviar cometario a host

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
				final Intent intent = new Intent(Host.this, Host.class);
				intent.putExtra("id", id);// enviarle a la pagina
											// de resultado el id
											// seleccionado
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

	public void onBackPressed() {// si se pulsa el boton atras del tlf
		// Write your code here

		String datoatras = VariableAtras.getDato();

		if (datoatras.equals("host")) {

			VariableAtras.setDato("resultado");

			System.out.println("Pulsado atras en host");

			// /////
			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);// se muestre efecto cargando

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Host.this, Resultado.class);
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

	}// end onBackPressed

}// end class
