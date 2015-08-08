package com.android.inmoprueba1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

public class Login2 {

	public static void logout() {

		System.out.println("entrando logout");

		InputStream is = null;
		String result = "";

		//String url = "http://www.arcanetsl.com/inmobiliaria/web/movil/logout";// url
																				// que
																				// realiza
																				// el
																				// logout
																				// en
																				// el
																				// servidor
		
		String url = "http://www.arcanetsl.com/inmobiliaria/web/movil/api/logout?apikey=";// url
		
		String apikey = Variablecookie.getCookie();

		System.out.println("apikey recogida: " + apikey);

		url = url + apikey;// establecer que casero concreto buscamos

		System.out.println("URL cliente a buscar: " + url);

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);

			HttpEntity httpEntity = httpResponse.getEntity();

			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, HTTP.UTF_8), 8);

			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {

				sb.append(line + "\n");

			}

			is.close();

			result = sb.toString();

			System.out.println("resultado logout: " + result);

		} catch (Exception e) {

			System.out
					.println("Ha petao miserablemente el jsonobjetct---------------------");
			e.printStackTrace();
			System.out
					.println("------------------------------------------------------------");
		}

		// vaciando variable cookie

		Variablecookie.setCookie("N");

		// /////////

		System.out.println("retornando fin logout");

	}// end logout

}// end class login2
