package com.dojan.infiernoperfecto.entidades;

public class ResultadoAtaque {
	private final float danio;
	private final String efectoMensaje;

	public ResultadoAtaque(float danio, String efectoMensaje) {
		this.danio = danio;
		this.efectoMensaje = efectoMensaje;
	}

	public float getDanio() {
		return danio;
	}

	public String getEfectoMensaje() {
		return efectoMensaje;
	}

	public static ResultadoAtaque ofDanio(float danio) {
		return new ResultadoAtaque(danio, null);
	}

	public static ResultadoAtaque empty() {
		return new ResultadoAtaque(0f, null);
	}
}
