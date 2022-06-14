package juego;

import java.awt.Image;

import javax.sound.sampled.Clip;

import entorno.Entorno;
import entorno.Herramientas;

public class Disparo {

	private double alto;
	private double ancho;

	private double x;
	private double y;

	private double velocidad;
	private boolean tipo;

	private double angulo;

	private Image imgSword;
	private Image imgRock;

	private Clip colisionObstaculo;
	private Clip colisionKyojin;

	public Disparo(double x, double y, double direccion, boolean tipo) {
		this.alto = 20;
		this.ancho = 3;
		this.x = x;
		this.y = y;
		this.velocidad = 3;
		this.angulo = direccion;
		this.tipo = tipo;

		this.imgSword = Herramientas.cargarImagen("mikasaSword1.png");
		this.imgRock = Herramientas.cargarImagen("rockShoot.png");
		this.colisionObstaculo = Herramientas.cargarSonido("espadaColisionObstaculo.wav");
		this.colisionKyojin = Herramientas.cargarSonido("espadaColisionKyojin.wav");
	}

	public void dibujar(Entorno e) {
		if (tipo == false) {
			e.dibujarImagen(imgSword, x, y, angulo - 5.5, 0.09);
		} else {
			e.dibujarImagen(imgRock, x, y, angulo - 5.5, 0.09);
		}
	}

	public void avanzar() {
		x += Math.cos(angulo) * velocidad;
		y += Math.sin(angulo) * velocidad;
	}

	public boolean estoyColisionandoEntorno(Entorno entorno) {
		return x >= entorno.ancho() || x <= 0 || y <= 0 || y >= entorno.alto();
	}

	public boolean estoyColisionandoConUnObstaculo(Obstaculo o) {
		if ((x + ancho / 2 >= o.getX() - o.getAncho() / 2) && (x - ancho / 2 <= o.getX() + o.getAncho() / 2)
				&& (y - alto / 2 <= o.getY() + o.getAlto() / 2) && (y + alto / 2 >= o.getY() - o.getAlto() / 2)) {
			colisionObstaculo.start();
			return true;
		}
		return false;
	}

	public boolean estoyColisionandoConUnKyojin(Kyojin k) {
		if ((x + ancho / 2 >= k.getX() - k.getAncho() / 2) && (x - ancho / 2 <= k.getX() + k.getAncho() / 2)
				&& (y - alto / 2 <= k.getY() + k.getAlto() / 2) && (y + alto / 2 >= k.getY() - k.getAlto() / 2)) {
			colisionKyojin.start();
			return true;
		}

		return false;
	}

	public boolean estoyColicionandoConMuchosObstaculos(Obstaculo[] edificios) {
		for (Obstaculo o : edificios) {
			if (estoyColisionandoConUnObstaculo(o) == true) {
				return true;
			}
		}
		return false;
	}

	public boolean colisionKyojin(Kyojin[] kyojines) {
		for (int j = 0; j <= kyojines.length - 1; j++) {
			if (kyojines[j] != null) {
				if (estoyColisionandoConUnKyojin(kyojines[j]) == true) {
					kyojines[j] = null;
					return true;
				}
			}
		}
		return false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getDireccion() {
		return angulo;
	}

	public double getAncho() {
		return ancho;
	}

	public double getAlto() {
		return alto;
	}

}
