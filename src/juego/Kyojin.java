package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Kyojin {

	private double x;
	private double y;

	private double ancho;
	private double alto;
	private double velocidad;
	private Image kyojinImage; // imgRight
	private Image kyojinImage2; // imgLeft
	private char direccion;
	private int contadorParaEvitarSpawnsRepetidos;

	public Kyojin(double x, double y, double velocidad) {
		this.ancho = 40;
		this.alto = 60;
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.contadorParaEvitarSpawnsRepetidos = 0;

		this.kyojinImage = Herramientas.cargarImagen("kyojinImage.png");
		this.kyojinImage2 = Herramientas.cargarImagen("kyojinImage2.png");
	}

	public void dibujar(Entorno entorno) {
		if (direccion == 'L') {
			entorno.dibujarImagen(kyojinImage, x, y, 0, 0.13);
		} else {
			entorno.dibujarImagen(kyojinImage2, x, y, 0, 0.13);
		}
	}

	private boolean hayColisionEntreUnKyojinYObstaculo(Obstaculo obstaculo, double direccionX, double direccionY) {
		double obstaculoX = obstaculo.getX() - (obstaculo.getAncho() / 2) + ancho / 2;
		double obstaculoY = obstaculo.getY() - (obstaculo.getAlto() / 2) + alto / 2;
		return direccionX < obstaculoX + obstaculo.getAncho() && obstaculoX < direccionX + ancho
				&& direccionY < obstaculoY + obstaculo.getAlto() && obstaculoY < direccionY + alto;
	}

	private boolean hayColisionesEntreKyojinesYObstaculos(Obstaculo[] edificios, double x, double y) {
		for (int i = 0; i < edificios.length; i++) {
			if (hayColisionEntreUnKyojinYObstaculo(edificios[i], x, y)) {
				return true;
			}
		}
		return false;
	}

	private boolean hayColisionEntreUnKyojinYOtro(Kyojin kyojin, double direccionX, double direccionY) {
		double xKyojines = kyojin.getX();
		double yKyojines = kyojin.getY();
		return direccionX + (kyojin.getAncho() / 2) > xKyojines - (kyojin.getAncho() / 2)
				&& direccionX - (kyojin.getAncho() / 2) < xKyojines + (kyojin.getAncho() / 2)
				&& direccionY + (kyojin.getAlto() / 2) > yKyojines - (kyojin.getAlto() / 2)
				&& direccionY - (kyojin.getAlto() / 2) < yKyojines + (kyojin.getAlto() / 2);
	}

	private boolean hayColisionesEntreKyojines(Kyojin[] kyojines, double x, double y) {
		for (int i = 0; i < kyojines.length; i++) {
			if (kyojines[i] != null && kyojines[i] != this) {
				if (hayColisionEntreUnKyojinYOtro(kyojines[i], x, y)) {
					return true;
				}
			}
		}
		return false;
	}

	private void siguiendoAMikasaParaArriba(Entorno entorno, Mikasa mikasa, Obstaculo[] obstaculos, Kyojin[] kyojines) {

		if (hayColisionesEntreKyojinesYObstaculos(obstaculos, x, y - velocidad)) {
			return;
		}

		if (hayColisionesEntreKyojines(kyojines, x, y - velocidad)) {
			return;
		}

		if (mikasa.getY() < y) {
			vaHaciaArriba();
		}
	}

	private void siguiendoAMikasaParaDerecha(Entorno entorno, Mikasa mikasa, Obstaculo[] obstaculos,
			Kyojin[] kyojines) {

		if (hayColisionesEntreKyojinesYObstaculos(obstaculos, x + velocidad, y)) {
			return;
		}

		if (hayColisionesEntreKyojines(kyojines, x + velocidad, y)) {
			return;
		}

		if (mikasa.getX() > x) {
			vaHaciaDerecha(entorno);
		}
	}

	private void siguiendoAMikasaParaAbajo(Entorno entorno, Mikasa mikasa, Obstaculo[] obstaculos, Kyojin[] kyojines) {

		if (hayColisionesEntreKyojinesYObstaculos(obstaculos, x, y + velocidad)) {
			return;
		}

		if (hayColisionesEntreKyojines(kyojines, x, y + velocidad)) {
			return;
		}

		if (mikasa.getY() > y) {
			vaHaciaAbajo(entorno);
		}
	}

	private void siguiendoAMikasaParaIzquierda(Entorno entorno, Mikasa mikasa, Obstaculo[] obstaculos,
			Kyojin[] kyojines) {

		if (hayColisionesEntreKyojinesYObstaculos(obstaculos, x - velocidad, y)) {
			return;
		}

		if (hayColisionesEntreKyojines(kyojines, x - velocidad, y)) {
			return;
		}

		if (mikasa.getX() < x) {
			vaHaciaIzquierda();
		}
	}

	public void siguiendoAMikasa(Entorno entorno, Mikasa mikasa, Obstaculo[] obstaculos, Kyojin[] kyojines) {
		siguiendoAMikasaParaArriba(entorno, mikasa, obstaculos, kyojines);
		siguiendoAMikasaParaDerecha(entorno, mikasa, obstaculos, kyojines);
		siguiendoAMikasaParaAbajo(entorno, mikasa, obstaculos, kyojines);
		siguiendoAMikasaParaIzquierda(entorno, mikasa, obstaculos, kyojines);
	}

	public Kyojin spawnearKyojin(Mikasa mikasa, Entorno e) {
		int[] multiplosYQueEvitanObstaculos = { 1, 5, 9 };
		int yKyojin = 60 * multiplosYQueEvitanObstaculos[contadorParaEvitarSpawnsRepetidos];
		Kyojin k = null;
		if (mikasa.getX() > e.ancho() / 2) {
			k = new Kyojin(50, yKyojin, 0.8);
		} else if (mikasa.getX() < e.ancho() / 2) {
			k = new Kyojin(e.ancho() - 50, yKyojin, 0.8);
		}
		incrementarYReiniciarContador();
		return k;
	}

	private void incrementarYReiniciarContador() {
		contadorParaEvitarSpawnsRepetidos += 1;
		if (contadorParaEvitarSpawnsRepetidos >= 3) {
			contadorParaEvitarSpawnsRepetidos = 0;
		}
		return;
	}

	private void vaHaciaArriba() {
		if (y < ancho / 2) {
			return;
		}
		y -= velocidad;
	}

	private void vaHaciaDerecha(Entorno entorno) {
		if (x > entorno.ancho() - ancho / 2) {
			return;
		}
		x += velocidad;
		direccion = 'R';
	}

	private void vaHaciaAbajo(Entorno entorno) {
		if (y > entorno.alto() - alto / 2) {
			return;
		}
		y += velocidad;
	}

	private void vaHaciaIzquierda() {
		if (x < alto / 2) {
			return;
		}
		x -= velocidad;
		direccion = 'L';
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getAncho() {
		return ancho;
	}

	public double getAlto() {
		return alto;
	}

}
