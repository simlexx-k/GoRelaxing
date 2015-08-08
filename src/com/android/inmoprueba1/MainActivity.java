package com.android.inmoprueba1;

import java.util.Locale;

import com.android.inmoprueba1.R;
import com.android.inmoprueba1.MainActivity;
import com.android.inmoprueba1.Busqueda;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.app.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements
		android.view.View.OnClickListener {

	int indicador = 0;
	ImageView logo;
	String lang = "";
	String monedausada = "EUR";// que moneda se usa para mostrar, el euro
								// sera la por defecto, aunque luego se
								// pueda cambiar

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		logo = (ImageView) findViewById(R.id.imageView1);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		String paislocal = Locale.getDefault().getDisplayLanguage();// extraer
																	// el idioma
																	// del
																	// dispositivo

		System.out.println("Lenguaje del dispositivo: " + paislocal);

		// vamos a ver si se ha definido otro idioma para la app
		try {

			String idi = GuardaIdioma.onClickCargar(this);

			System.out.println("idi: " + idi);

			if (idi != null && !idi.equals("")) {

				lang = idi;

				System.out.println("dentro if: " + lang);

			}// end if
			else {

				if (paislocal.equals("español")) {

					lang = "es";

				}// establecer el idioma que tiene el tlf para la app
				else {

					lang = "en";// English

				}//

			}// end else

			System.out.println("lang despues idi: " + lang);

			languagechange(lang);

		} catch (Exception e) {

			System.out
					.println("Ha petado la llamada a carga idioma desde main");
			System.out.println(e);
			System.out
					.println("fin excepcion carga idioma main---------------------------");

		}// end try catch

		// comprobar si venimos de busqueda
		int cont = VariableAtras.getContador();

		System.out.println("cont: " + cont);

		if (cont != 99) {

			Intent intent = new Intent(MainActivity.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", lang);

			startActivity(intent);

		}// end if
		else {

			indicador = 1;

			VariableAtras.setContador(0);
		}// end else

		logo.setOnClickListener(this);

	}// end oncreate

	public void languagechange(String lang) {

		Locale locale = new Locale(lang);
		Locale.setDefault(locale);

		System.out.println("locale: " + locale);

		Configuration config = new Configuration();
		config.locale = locale;

		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

		System.out.println("fin languajechange");

	}// end languagechange

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		try {
			getMenuInflater().inflate(R.menu.main, menu);
		} catch (Exception e) {

			System.out.println("Ha petado la creacion del menu: " + e);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
		// TODO Auto-generated method stub

	}// end menu

	public void onBackPressed() {// si se pulsa el boton atras del tlf
		// Write your code here

		System.out.println("Pulsado atras en main");

		if (indicador == 1) {

			System.out.println("Pulsado atras en main 2");

			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(startMain);

			System.exit(0);

		}// end if

		super.onBackPressed();

	}// end onBackPressed

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.imageView1:

			Intent intent = new Intent(MainActivity.this, Busqueda.class);
			intent.putExtra("moneda", monedausada);
			intent.putExtra("langusado", lang);

			startActivity(intent);

			break;
		}// end switch

	}// end onclick

}// end class
