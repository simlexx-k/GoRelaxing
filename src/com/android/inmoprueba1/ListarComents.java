package com.android.inmoprueba1;

public class ListarComents {

	protected String comentario;
	protected String fecha;
	protected String autor;
	protected long id;

	public ListarComents(String comentario, String fecha) {

		super();
		this.comentario = comentario;
		this.fecha = fecha;

	}// end ListarComents

	public ListarComents(String comentario, String fecha, long id) {

		super();

		this.comentario = comentario;
		this.fecha = fecha;

	}// end Directivo2

	public String getComentario() {

		System.out.println("getComentario: " + comentario);

		return comentario;

	}// end getComentario

	public void setFecha(String fecha) {

		System.out.println("setFecha: " + fecha);

		this.fecha = fecha;

	}// end setComentario
	
	public String getFecha() {

		System.out.println("getFecha: " + fecha);

		return fecha;

	}// end getComentario

	public void setComentario(String comentario) {

		System.out.println("setComentario: " + comentario);

		this.comentario = comentario;

	}// end setComentario

	public long getid() {

		return id;

	}// end getid

	public void setid(long id) {

		this.id = id;

	}// end setid

}// end class
