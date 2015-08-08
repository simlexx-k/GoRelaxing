package com.android.inmoprueba1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;

public class Json {

	static String url = "";

	public static Object getJSONfromURL() {

		System.out.println("entrando getJSONfromURL");

		InputStream is = null;
		String result = "";
		JSONArray Array = null;
		Object obj = null;
		JSONParser parser = new JSONParser();

		System.out.println("llamando a url");

		try {

			System.out.println("dentro del try");

			DefaultHttpClient httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);

			HttpEntity httpEntity = httpResponse.getEntity();

			is = httpEntity.getContent();
			System.out.println("relleno is");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("entrando en try buffered reader");

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, HTTP.UTF_8), 8);

			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {

				sb.append(line + "\n");

			}
			System.out.println("hecho sb.append");

			is.close();

			result = sb.toString();

			System.out.println("result " + result);

			obj = parser.parse(result);

			System.out.println("array que se va a enviar: " + Array);

		} catch (Exception e) {

			System.out
					.println("Ha petao miserablemente el jsonobjetct---------------------");
			e.printStackTrace();
			System.out
					.println("------------------------------------------------------------");
		}

		return obj;
	}

	// ///////////////////////////////////

	public JSONArray readJSON(String urlr) throws ParseException,
			MalformedURLException, IOException {// si el json a leer es un array

		System.out.println("dentro de readJSon");

		url = urlr;

		System.out.println("va a llamar getJSONfromURL con la url: " + url);

		JSONArray Array = new JSONArray();

		Array = (JSONArray) getJSONfromURL();

		return Array;

	}// end readJSON

	public JSONObject readJSONOBJECT(String urlr) throws ParseException,
			MalformedURLException, IOException {// si el json a leer es un
												// object

		System.out.println("dentro de readJSonOBJECT");

		url = urlr;

		System.out.println("va a llamar getJSONfromURL con la url: " + url);

		JSONObject object = new JSONObject();

		object = (JSONObject) getJSONfromURL();

		return object;

	}// end readJSON

}// end jsonclass
