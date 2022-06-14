package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class PowerUp {

	private double x;
	private double y;
	private double ancho;
	private double alto;

	private int tipo; // 1: ammo, 2: turbo, 3: suero
	private Image imgAmmo;
	private Image imgSpeed;
	private Image imgSyringe;

	public PowerUp(int x, int y, int tipo) {
		this.ancho = 20;
		this.alto = 20;
		this.x = x;
		this.y = y;
		this.tipo = tipo;

		this.imgAmmo = Herramientas.cargarImagen("ammoPowerUp.png");
		this.imgSpeed = Herramientas.cargarImagen("speedPowerUp.png");
		this.imgSyringe = Herramientas.cargarImagen("syringePowerUp.png");
	}

	public void dibujar(Entorno entorno) {
		if (tipo == 1) {
			entorno.dibujarImagen(imgAmmo, x, y, 0, 0.09);
		} else {
			if (tipo == 2) {
				entorno.dibujarImagen(imgSpeed, x, y, 0, 0.05);
			} else {
				entorno.dibujarImagen(imgSyringe, x, y, 0, 0.09);
			}
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

	public double getTipo() {
		return tipo;
	}

}