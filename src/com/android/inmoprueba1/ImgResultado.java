package com.android.inmoprueba1;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ImgResultado extends Resultado {

	ImageView image, imagen;
	
	ArrayList<listarImgs> arraimg;
	listarImgs resultadoimg;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide title bar
		// actionbar
				ActionBar actionBar = getActionBar();
				actionBar.hide();

		setContentView(R.layout.imgresultado);

		// listaimgs = (ListView) findViewById(R.id.listaimg);

		//final HorizontalScrollView scroll = (HorizontalScrollView) findViewById(R.id.scrollView);

		LinearLayout layout = (LinearLayout) findViewById(R.id.content);

		System.out.println("Creado listaimgs horizontalview: " + imgs);

		//LayoutInflater inflater = LayoutInflater.from(this);

		//image = (ImageView) findViewById(R.id.foto2);
		image = (ImageView) findViewById(R.id.imageView1);

		for (int i = 0; i < imgs.size(); i++) {

			String foto = imgs.get(i);

			System.out.println("foto imgs: " + foto);

			image = CargarImagenes.downloadImg(image, foto);
			// fotoprincipalMA = CargarImagenes.downloadImg(fotoprincipalMA,
			// foto);

			System.out.println("image horizontalview: " + image);

			// meter la imagen en un objeto drawable para poder mostrarla
			Drawable dw1 = image.getDrawable();
			
			System.out.println("dw1 horizontalview: " + dw1);
			// Drawable dw1 = fotoprincipalMA.getDrawable();

			/*RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(
					R.layout.imgresultadoitem, null);*/
			Activity activity = this;
			View v;
			
			/********************************************/
			
			// llamar a directivo para que cree el resultado con todo junto
				resultadoimg = new listarImgs(dw);

				// crear array con todos los elementos
				arraimg.add(resultadoimg);
				
				Adapterimg adapterimg = new Adapterimg(this, arraimg);
				System.out.println("creado adapter imagenes");
				
			 /****************************************************/

			// Asociamos el layout de la lista que hemos creado

				LayoutInflater inf = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = inf.inflate(R.layout.imgresultadoitem, null);

			//imagen = (ImageView) relativeLayout.findViewById(R.id.itemImage);
					imagen = (ImageView) v.findViewById(R.id.itemImage);

			imagen.setImageDrawable(dw1);

			System.out.println("imagen horizontalview ultimo: " + imagen);
			
			//layout params
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	     
	        v.setPadding(5, 3, 5, 3); 
	        //v.setLayoutParams(params);
	        /////// 

			//layout.addView(relativeLayout);
			layout.addView(v);
			

			// scroll to last element
			// http://stackoverflow.com/questions/6438061/can-i-scroll-a-scrollview-programmatically-in-android
			/*scroll.post(new Runnable() {
				public void run() {
					scroll.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});*/
			// /////

		}// end for

	}// end oncreate

}// end class

