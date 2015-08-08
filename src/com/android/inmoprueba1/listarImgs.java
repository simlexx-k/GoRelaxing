package com.android.inmoprueba1;

import android.graphics.drawable.Drawable;

public class listarImgs {
	
	protected Drawable foto;
	protected long id;


	public listarImgs(Drawable foto) {

		super();
		this.foto = foto;


	}// end Directivo

	public listarImgs(Drawable foto, long id) {

		super();

		this.foto = foto;

	}// end Directivo2

	public Drawable getFoto() {

		return foto;

	}// end getfoto

	public void setFoto(Drawable foto) {

		this.foto = foto;

	}// end setfoto
	
	public long getid() {

		return id;

	}// end getid

	public void setid(long id) {

		this.id = id;

	}// end setid
	

}
