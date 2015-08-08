package com.android.inmoprueba1;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

//clase auxiliar para recuperar la cotizacion de una moneda

public class CambioMoneda {

	public static Double getcambiomoneda(String monedachange) {

		System.out.println("entrando getcambiomoneda con: " + monedachange);

		String monedastr = "";
		String tipocambiostr = "";
		Double tipocambio = null;

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder
					.parse("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");// cargar
																							// xml
			doc.getDocumentElement().normalize();

			System.out.println("El elemento raíz es: "
					+ doc.getDocumentElement().getNodeName());
			NodeList listaMonedas = doc.getElementsByTagName("Cube");

			int i = 0;

			while (tipocambio == null) {

				Node moneda = listaMonedas.item(i);

				if (moneda.getNodeType() == Node.ELEMENT_NODE) {

					Element elemento = (Element) moneda;

					monedastr = elemento.getAttribute("currency");
					System.out.println("currency : "
							+ elemento.getAttribute("currency"));

					tipocambiostr = elemento.getAttribute("rate");
					System.out.println("rate : "
							+ elemento.getAttribute("rate"));
					System.out.println("antes de llamar if con  monedastr: "
							+ monedastr + "y monedachange: " + monedachange);
					if (monedastr.equals(monedachange)) {

						System.out
								.println("La moneda coincide con la seleccionada");

						tipocambio = Double.parseDouble(tipocambiostr);

						System.out.println("tipocambio: " + tipocambio);

					}// end if monedastr

				}// end if moneda

				i++;

			}// end while

		} catch (Exception e) {

			System.out
					.println("Ha petao miserablemente el jsonobjetct---------------------");
			e.printStackTrace();
			System.out
					.println("------------------------------------------------------------");
		}

		System.out.println("retornando");

		return tipocambio;

	}

}// end class
