package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Obstaculo {

	private double ancho;
	private double alto;

	private double x;
	private double y;

	private int tipo;

	private Image imgHouse1; // imgHouse
	private Image imgArbol;

	public Obstaculo(double x, double y, int tipo) {

		this.ancho = 160;
		this.alto = 120;
		this.x = x;
		this.y = y;
		this.tipo = tipo;

		this.imgHouse1 = Herramientas.cargarImagen("house.png");

	}

	public Obstaculo(double x, double y, double ancho, double alto, int tipo) {

		this.ancho = ancho;
		this.alto = alto;
		this.x = x;
		this.y = y;
		this.tipo = tipo;
		this.imgArbol = Herramientas.cargarImagen("arbol.png");

	}

	public void dibujar(Entorno entorno) {
		if (tipo == 1) {
			entorno.dibujarImagen(imgHouse1, x, y, 0, 1);
		} else {
			entorno.dibujarImagen(imgArbol, x, y, 0, 0.07);
		}
	}

	public double getAncho() {
		return ancho;
	}

	public double getAlto() {
		return alto;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}