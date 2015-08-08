package com.android.inmoprueba1;

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

public class VerComentario extends Comentarios implements
		android.view.View.OnClickListener {

	TextView fechacoment, autorcoment, textocoment;
	ImageView fotoautor;
	
	String fotoaut;

	Long id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vercomentario);

		System.out.println("entrando en vercomentario");

		VariableAtras.setDato("vercomentario");// para controlar boton atras
												// movil
		VariableAtras.setContador(5);

		fechacoment = (TextView) findViewById(R.id.fecha);
		autorcoment = (TextView) findViewById(R.id.autorcoment);
		textocoment = (TextView) findViewById(R.id.textocoment);

		fotoautor = (ImageView) findViewById(R.id.fotoautor);

		// recoger datos comentario
		reicieveParams = getIntent().getExtras();

		id = reicieveParams.getLong("id");// recoger id para la vuelta
		
		langusado = reicieveParams.getString("langusado");

		System.out.println("langusado en buscar: " + langusado);

		// rellenar textviews
		fechacoment.setText(reicieveParams.getString("fechac"));
		autorcoment.setText(reicieveParams.getString("autorc"));
		textocoment.setText(reicieveParams.getString("textoc"));

		// falta rellenar la imagen del autor

		// ////////////////////////

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

			intent = new Intent(VerComentario.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", langusado);
			startActivity(intent);

			return true;

			case R.id.registromn:

				intent = new Intent(VerComentario.this, Registro.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "vercomentario");
				intent.putExtra("id", id);
				intent.putExtra("fechac", fechacoment.getText());
				intent.putExtra("autorc", autorcoment.getText());
				intent.putExtra("textoc", textocoment.getText());
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			return true;

		case R.id.perfilmn:

			if (conec.equals("S")) {// llama a logout y va a busqueda

				//Login2.logout();
				intent = new Intent(VerComentario.this, MiPerfil.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "vercomentario");
				intent.putExtra("id", id);
				intent.putExtra("fechac", fechacoment.getText());
				intent.putExtra("autorc", autorcoment.getText());
				intent.putExtra("textoc", textocoment.getText());
				intent.putExtra("langusado", langusado);
				startActivity(intent);

			}// end if
			else {

				intent = new Intent(VerComentario.this, Login.class);
				intent.putExtra("moneda", monedausada);
				intent.putExtra("llamado", "vercomentario");
				intent.putExtra("id", id);
				intent.putExtra("fechac", fechacoment.getText());
				intent.putExtra("autorc", autorcoment.getText());
				intent.putExtra("textoc", textocoment.getText());
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

				final Intent intent = new Intent(VerComentario.this,
						VerComentario.class);
				intent.putExtra("id", id);// enviarle a la pagina de
											// resultado el id seleccionado
				intent.putExtra("fechac", fechacoment.getText());
				intent.putExtra("autorc", autorcoment.getText());
				intent.putExtra("textoc", textocoment.getText());
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

		if (datoatras.equals("vercomentario") && cont == 5) {

			VariableAtras.setDato("comentarios");

			System.out.println("Pulsado atras en vercomentarios");
			
			////////
			final ProgressDialog progress = ProgressDialog.show(this, "",
					"Procesing...", true);// se muestre efecto cargando

			new Thread(new Runnable() {
				@Override
				public void run() {
					// do the thing that takes a long time
					final Intent intent = new Intent(VerComentario.this, Comentarios.class);
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
			//////				
			
		}// end if

		super.onBackPressed();
		
		VariableAtras.setContador(cont - 1);
		System.out.println("despues de reducir busqueda: " + cont);
	}

}// end class
