package com.android.inmoprueba1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.os.Environment;

public class GuardaIdioma {// si se cambia el idioma de la aplicacion hay que
							// guardar la preferencia para las proximas veces

	static final int READ_BLOCK_SIZE = 100;

	public static void onClickGuardar(String idioma, Context c)
			throws IOException {// guardar idioma seleccionado

		try {

			System.out.println("entrando en onClickGuardar");

			File ruta_sd = Environment.getExternalStorageDirectory();

			File f = new File(ruta_sd.getAbsolutePath(), "idioma.txt");

			OutputStreamWriter fout = new OutputStreamWriter(
					new FileOutputStream(f));

			fout.write(idioma);
			fout.close();

		} catch (IOException ex) {
			System.out.println("escribir en archivo ha petao");
			ex.printStackTrace();
			System.out
					.println("fin exception escribir en archivo----------------");
		}

	}

	public static String onClickCargar(Context c) {

		String s = "";

		try {

			File ruta_sd = Environment.getExternalStorageDirectory();

			File f = new File(ruta_sd.getAbsolutePath(), "idioma.txt");

			BufferedReader fin = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));

			s = fin.readLine();
			fin.close();

			// Establecemos en el EditText el texto que hemos leido
			System.out.println("Recuperado del archivo: " + s);

		} catch (IOException ex) {
			System.out.println("leer de archivo ha petao");
			ex.printStackTrace();
			System.out.println("fin exception leer de archivo----------------");
		}// end try-catch

		return s;
	}

}// end class
