package com.android.inmoprueba1;

public class Variablecookie {//almacear el apikey devuelto por el servidor cuando te logas

	static String cookielogado = "N";

	public static String getCookie() {
		System.out.println("cookielogado get: " + cookielogado);
		return cookielogado;
	}// end get

	public static void setCookie(String s) {
		cookielogado = s;
		System.out.println("cookielogado set: " + cookielogado);
	}// end set

}// end class
