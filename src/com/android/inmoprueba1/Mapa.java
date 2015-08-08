package com.android.inmoprueba1;

import java.util.Locale;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//mostar el mapa con la ubicacion del inmueble

public class Mapa extends Resultado implements
		android.view.View.OnClickListener {

	// Declaracion variables

	public GoogleMap mapa;

	public double logitud, latitud;

	String longstr, latstr;

	public String tituloanun = "";
	public String direcanun = "";

	String conectado = "n";

	Long id;

	TextView cabeceramapa, pulsaestandar, pulsasatelite, pulsaruta;

	int conta;

	// ///////////////////////

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);

		VariableAtras.setDato("mapa");// controlar boton atras telefono
		VariableAtras.setContador(4);

		conta = VariableAtras.getcont();
		System.out.println("conta: " + conta);
		conta = conta + 1;
		System.out.println("conta despues de sumar1: " + conta);
		VariableAtras.setcont(conta);
		System.out.println("conta despues de sumar2: "
				+ VariableAtras.getcont());

		System.out.println("Entrando en mapa");

		// recoger coordenadas de inmueble
		reicieveParams = getIntent().getExtras();// recibir el coordenadas de la
													// propiedad seleccionada

		mapa = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.mvMapa)).getMap();
		cabeceramapa = (TextView) findViewById(R.id.mapa);
		pulsaestandar = (TextView) findViewById(R.id.estandar);
		pulsasatelite = (TextView) findViewById(R.id.satelite);
		pulsaruta = (TextView) findViewById(R.id.ruta);

		cabeceramapa.setOnClickListener(this);
		pulsaestandar.setOnClickListener(this);
		pulsasatelite.setOnClickListener(this);
		pulsaruta.setOnClickListener(this);

		// recibir param ////////////////////////////

		id = reicieveParams.getLong("id");

		System.out.println("id recogido en mapa: " + id);

		int conta2 = VariableAtras.getcont();
		System.out.println("conta2: " + conta2);
		if (conta2 == 1) {

			VariableAtras.setcontrol(id);
		}

		longstr = reicieveParams.getString("longitud");
		latstr = reicieveParams.getString("latitud");

		logitud = Double.parseDouble(longstr);
		latitud = Double.parseDouble(latstr);

		tituloanun = reicieveParams.getString("titulo_anuncio");
		direcanun = reicieveParams.getString("direccion");
		monedausada = reicieveParams.getString("moneda");

		langusado = reicieveParams.getString("langusado");

		System.out.println("langusado en buscar: " + langusado);

		mostrarpunto();// mostar punto exacto solo si esta logueado

		String idi = (String) pulsaruta.getText();
		System.out.println("idi en mapa: " + idi);

		if (!langusado.equals("es") && idi.equals("ruta")) {

			languagechange(langusado);
		}//
		if (!langusado.equals("en") && idi.equals("route")) {

			languagechange(langusado);
		}//

	}// end oncreate

	public void mostrarmapa() {// mostrar coordenadas del inmueble en el mapa

		String nuevalinea = System.getProperty("line.separator");// salto de
																	// linea

		// Activo mi localización en el mapa
		mapa.setMyLocationEnabled(true);

		// establecer posicion de la propiedad
		CameraPosition position = new CameraPosition.Builder()
				.target(new LatLng(latitud, logitud)).zoom(15).build();
		CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);

		// añadir un marcador con el titulo del anuncio y la dirección
		mapa.addMarker(new MarkerOptions().position(

		new LatLng(latitud, logitud))
				.title(tituloanun + nuevalinea + direcanun));

		mapa.animateCamera(update);// mover la "camara" del mapa al punto donde
									// estan las coordenadas marcadas

	}// end mostrarmaopa

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

			intent = new Intent(Mapa.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", langusado);
			startActivity(intent);

			return true;

			case R.id.registromn:

				intent = new Intent(Mapa.this, Registro.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "mapa");
				intent.putExtra("longitud", longstr);
				intent.putExtra("latitud", latstr);
				intent.putExtra("titulo_anuncio", tituloanun);
				intent.putExtra("direccion", direcanun);
				intent.putExtra("id", id);
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				//Login2.logout();
				intent = new Intent(Mapa.this, MiPerfil.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "mapa");
				intent.putExtra("longitud", longstr);
				intent.putExtra("latitud", latstr);
				intent.putExtra("titulo_anuncio", tituloanun);
				intent.putExtra("direccion", direcanun);
				intent.putExtra("id", id);
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			}// end if
			else {

				intent = new Intent(Mapa.this, Login.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "mapa");
				intent.putExtra("longitud", longstr);
				intent.putExtra("latitud", latstr);
				intent.putExtra("titulo_anuncio", tituloanun);
				intent.putExtra("direccion", direcanun);
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

		switch (v.getId()) {

		case R.id.estandar:// mostrar en modo estandar el mapa

			System.out.println("Dentro de estandar");

			mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			break;// end case R.id.estandar:

		case R.id.satelite:// mostrar en modo satelite el mapa

			System.out.println("Dentro de satelite");

			mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

			break;// end R.id.satelite:

		case R.id.ruta:// abrir el navegador o la app especifica de mapas de
						// google con la ruta desde nuestra ubicacion al
						// inmueble

			System.out.println("Dentro de ruta: " + latitud + " , " + logitud);

			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri
							.parse("http://maps.google.com/maps?saddr="
									+ "&daddr=" + latitud + "," + logitud));
					if (intent.resolveActivity(getPackageManager()) != null) {
						startActivity(intent);
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progress.dismiss();
						}
					});
				}
			}).start();

			break;// end R.id.ruta:

		}// end switch

	}// end onclick

	public void mostrarpunto() {// mostrar el punto exacto del inmueble solo si
								// esta logueado

		System.out.println("Dentro de mostrarpunto");

		// saber si esta conectado
		conec = Logado.comprobarlog(this);

		if (conec.equals("S")) {

			System.out.println("Esta logueado");

			conectado = "s";

		}// end if
		else {

			System.out.println("No esta logueado");

			conectado = "n";

		}// end else

		mostrarmapa();

	}// end mostrarpunto

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
				final Intent intent2 = new Intent(Mapa.this, Mapa.class);
				intent2.putExtra("id", id);
				intent2.putExtra("latitud", latstr);
				intent2.putExtra("longitud", longstr);
				intent2.putExtra("titulo_anuncio", tituloanun);
				intent2.putExtra("direccion", direcanun);
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

		System.out.println("Pulsado atras en mapa: " + cont);
		System.out.println("id en mapa: " + id);

		if (datoatras.equals("mapa") && cont == 4) {

			VariableAtras.setDato("resultado");

			System.out.println("Pulsado atras en mapa");

			// /////////
			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);// se muestre efecto cargando

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(Mapa.this, Resultado.class);

					intent.putExtra("id", VariableAtras.getcontrol());
					System.out.println("id a devolver de mapa: "
							+ VariableAtras.getcontrol());
					VariableAtras.setcont(0);
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

			// /////

		}// end if

		super.onBackPressed();

		VariableAtras.setContador(cont - 1);
		System.out.println("despues de reducir mapa: " + cont);

	}// end onback

}// end class mapa
