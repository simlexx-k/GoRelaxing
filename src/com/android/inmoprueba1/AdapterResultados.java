package com.android.inmoprueba1;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterResultados extends BaseAdapter {// seleccionar el tipo de
													// vista en resultados y
													// crear y mostar el array
													// con todos

	protected Activity activity;
	protected ArrayList<Directivo> items;
	protected int ss;

	Double tipoChange;// saber el tipo de cambio al cambiar de moneda

	protected String monedausada = "";// que moneda se usa para mostrar

	public AdapterResultados(Activity activity, ArrayList<Directivo> items,
			int s, String tipomoneda) {
		this.activity = activity;
		this.items = items;
		this.ss = s;
		this.monedausada = tipomoneda;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Generamos una convertView por motivos de eficiencia
		View v = convertView;

		// Asociamos el layout de la lista que hemos creado
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (ss == 1) {
				v = inf.inflate(R.layout.lista_resultados2, null);
			}// end if
			else {
				v = inf.inflate(R.layout.lista_resultados, null);
			}// end else
		}

		// Creamos un objeto directivo
		Directivo dir = items.get(position);

		// Rellenamos la fotografía
		ImageView foto = (ImageView) v.findViewById(R.id.foto);
		foto.setImageDrawable(dir.getFoto());

		// Rellenamos el nombre
		TextView titulo = (TextView) v.findViewById(R.id.titulo);
		titulo.setText(dir.getTitulo());

		// Rellenamos el precio y la moneda en la funcion cambio moneda

		String preciocal = dir.getPrecio();// el precio del inmueble para el
											// cambio de divisa

		String preciofin = cambioMoneda(monedausada, preciocal);

		TextView precio = (TextView) v.findViewById(R.id.precio);
		precio.setText(preciofin);

		TextView divisa = (TextView) v.findViewById(R.id.euro);

		if (monedausada.equals("EUR")) {

			divisa.setText("€");
			TextView avisocambio = (TextView) v.findViewById(R.id.cambiomoneda);
			avisocambio.setVisibility(View.GONE);// NO mostrar aviso de cambio
													// de moneda

		}// end if euro

		if (monedausada.equals("USD")) {

			divisa.setText("$");
			TextView avisocambio = (TextView) v.findViewById(R.id.cambiomoneda);
			avisocambio.setVisibility(View.VISIBLE);// mostrar aviso de cambio
													// de moneda

		}// end if DOLAR
		if (monedausada.equals("GBP")) {

			divisa.setText("£");
			TextView avisocambio = (TextView) v.findViewById(R.id.cambiomoneda);
			avisocambio.setVisibility(View.VISIBLE);// mostrar aviso de cambio
													// de moneda

		}// end if euro

		// Retornamos la vista
		return v;
	}

	public String cambioMoneda(String tipomoneda, String monedacal) {

		String valor = monedacal;
		Double preciocalculo;
		Double preciocambiado;

		if (tipomoneda.equals("EUR")) {

			valor = monedacal;

		} else {

			System.out.println("llamando a CambioMoneda con tipomoneda: "
					+ tipomoneda);

			tipoChange = CambioMoneda.getcambiomoneda(tipomoneda);

			System.out.println("tipo de cambio devuelto en busqueda: "
					+ tipoChange);

			preciocalculo = Double.parseDouble(monedacal);

			preciocambiado = preciocalculo * tipoChange;

			System.out.println("preciocambiado: " + preciocambiado);

			// cambio format para obtener solo 2 decimales
			DecimalFormat df = new DecimalFormat("######.##");

			valor = df.format(preciocambiado);

		}// end if EUR

		return valor;

	}// end cambio moneda

}// end class
