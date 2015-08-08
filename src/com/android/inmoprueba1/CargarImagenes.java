package com.android.inmoprueba1;

import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class CargarImagenes {

	// recuperar las imagenes para un inmueble determinado (recibe la foto a
	// buscar)
	static ImageView downloadImg(ImageView fotodevuelta, String fotorecuperada) {
		
		System.out.println("fotodevuelta al entrar en cargarimagenes: "
				+ fotodevuelta);

		System.out.println("entrando en downloadImg, fotorecuperada: "
				+ fotorecuperada);

		Bitmap loadedImage;
		String imageHttpAddress1 = "http://www.arcanetsl.com";// raiz de url
																// generica para
																// todas

		String imageHttpAddress = imageHttpAddress1 + fotorecuperada;

		System.out.println("imageHttpAddress concatenada: " + imageHttpAddress);

		URL imageUrl = null;

		try {

			imageUrl = new URL(imageHttpAddress);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.connect();
			loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
			
			System.out.println("imageUrl: " + imageUrl);
			
			System.out.println("loadedImage: " + loadedImage);

			fotodevuelta.setImageBitmap(loadedImage);
			
			System.out.println("fotodevuelta: " + fotodevuelta);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return fotodevuelta;

	}// downloadImg

}// end CargarImagenes class
