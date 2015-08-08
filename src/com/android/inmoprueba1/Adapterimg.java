package com.android.inmoprueba1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class Adapterimg extends BaseAdapter {// seleccionar comentario

	protected Activity activity;
	protected ArrayList<listarImgs> items;
	//protected ArrayList<Directivo> items;

	public Adapterimg(Activity activity, ArrayList<listarImgs> items) {
		//public Adapterimg(Activity activity, ArrayList<Directivo> items) {
		this.activity = activity;
		this.items = items;
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

			v = inf.inflate(R.layout.imgresultadoitem, null);

		}

		// Creamos un objeto directivo
		listarImgs com = items.get(position);
		//Directivo com = items.get(position);

		// Rellenamos la fotografía
		ImageView foto = (ImageView) v.findViewById(R.id.itemImage);
		foto.setImageDrawable(com.getFoto());

		// Retornamos la vista
		return v;
	}

}
