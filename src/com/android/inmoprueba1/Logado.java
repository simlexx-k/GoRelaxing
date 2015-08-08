package com.android.inmoprueba1;

import android.content.Context;

//Comprobar si se ha iniciado o no sesion en la app

public class Logado {

	public static String comprobarlog(Context c) {

		System.out.println("entrando en Logado");

		String conec = "N";

		String cookielogin;// recoger el valor de la apikey si existe

		cookielogin = Variablecookie.getCookie();

		System.out.println("cookielogin resultado: " + cookielogin);

		if (!cookielogin.equals("N")) {// si existe un valor apikey esq se ha
										// realizado el login

			conec = "S";

			// }//end if

		}// end if

		System.out.println("conec cookie resultado: " + conec);

		return conec;

	}// end comprobarlog

}// en class
