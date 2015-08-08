package com.android.inmoprueba1;

import android.graphics.drawable.Drawable;

//clase auxiliar para mostrar todos los resultados en la listview

public class Directivo {

	protected Drawable foto;
	protected String titulo;
	protected String precio;
	protected long id;

	public Directivo(Drawable foto, String titulo, String precio) {

		super();
		this.foto = foto;
		this.titulo = titulo;
		this.precio = precio;

	}// end Directivo

	public Directivo(Drawable foto, String titulo, String precio, long id) {

		super();

		this.foto = foto;
		this.titulo = titulo;
		this.precio = precio;

	}// end Directivo2

	public Drawable getFoto() {

		return foto;

	}// end getfoto

	public void setFoto(Drawable foto) {

		this.foto = foto;

	}// end setfoto

	public String getTitulo() {

		return titulo;

	}// end gettitulo

	public void setTitulo(String titulo) {

		this.titulo = titulo;

	}// end settitulo

	public String getPrecio() {

		return precio;

	}// end getprecio

	public void setPrecio(String precio) {

		this.precio = precio;

	}// end setprecio

	public long getid() {

		return id;

	}// end getid

	public void setid(long id) {

		this.id = id;

	}// end setid

}// end class
