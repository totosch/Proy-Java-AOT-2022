package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Mikasa {

	private double alto;
	private double altoTitan;
	private double ancho;
	private double x;
	private double y;
	private double velocidad;
	private double angulo;

	private Image imgMikasa;
	private Image imgMikasaTitan;
	private Image flechaDireccion;

	private boolean soyMikasaTitan;
	private boolean modoTurbo;

	public Mikasa(double x, double y, double velocidad) {

		this.x = x;
		this.y = y;
		this.alto = 40;
		this.altoTitan = alto * 2;
		this.ancho = 20;
		this.velocidad = velocidad;
		this.angulo = Math.PI * 2;

		soyMikasaTitan = false;
		modoTurbo = false;

		this.imgMikasa = Herramientas.cargarImagen("mikasaImage.png");
		this.imgMikasaTitan = Herramientas.cargarImagen("kyojin.png");
		this.flechaDireccion = Herramientas.cargarImagen("flechaDireccion.png");
	}

	public void dibujar(Entorno entorno) {
		if (soyMikasaTitan) {
			entorno.dibujarImagen(flechaDireccion, x, y, angulo, 0.15);
			entorno.dibujarImagen(imgMikasaTitan, x, y, 0, 0.3);
		} else {
			entorno.dibujarImagen(flechaDireccion, x, y, angulo, 0.1);
			entorno.dibujarImagen(imgMikasa, x, y, 0, 0.23);
		}
	}

	private boolean hayColisionMikasaConEdificio(Obstaculo obstaculo, double direccionX, double direccionY) {
		if (!soyMikasaTitan) {
			double obstaculoX = obstaculo.getX() - (obstaculo.getAncho() / 2) + ancho / 2;
			double obstaculoY = obstaculo.getY() - (obstaculo.getAlto() / 2) + alto / 2;
			return direccionX < obstaculoX + obstaculo.getAncho() && obstaculoX < direccionX + ancho
					&& direccionY < obstaculoY + obstaculo.getAlto() && obstaculoY < direccionY + alto;
		} else {
			double obstaculoX = obstaculo.getX() - (obstaculo.getAncho() / 2) + ancho / 2;
			double obstaculoY = obstaculo.getY() - (obstaculo.getAlto() / 2) + alto / 2;
			return direccionX < obstaculoX + obstaculo.getAncho() && obstaculoX < direccionX + ancho
					&& direccionY - altoTitan / 4 < obstaculoY + obstaculo.getAlto()
					&& obstaculoY < direccionY + altoTitan / 4 + alto;
		}
	}

	private boolean hayColisionesMikasaConEdificios(Obstaculo[] edificios, double x, double y) {
		for (int i = 0; i < edificios.length; i++) {
			if (hayColisionMikasaConEdificio(edificios[i], x, y)) {
				return true;
			}
		}
		return false;
	}

	public boolean hayColisionPowerUp(PowerUp powerUp) {
		if (!soyMikasaTitan) {
			double powerUpX = powerUp.getX() - (powerUp.getAncho() / 2) + ancho / 2;
			double powerUpY = powerUp.getY() - (powerUp.getAlto() / 2) + alto / 2;
			return x < powerUpX + powerUp.getAncho() && powerUpX < x + ancho && y < powerUpY + powerUp.getAlto()
					&& powerUpY < y + alto;
		} else {
			double powerUpX = powerUp.getX() - (powerUp.getAncho() / 2) + ancho / 2;
			double powerUpY = powerUp.getY() - (powerUp.getAlto() / 2) + alto / 2;
			return x < powerUpX + powerUp.getAncho() && powerUpX < x + ancho
					&& y - altoTitan / 4 < powerUpY + powerUp.getAlto() && powerUpY < y + altoTitan / 4 + alto;
		}
	}

	private boolean hayColisionConKyojin(Kyojin kyojin) {
		if (!soyMikasaTitan) {
			double kyojinX = kyojin.getX() - (kyojin.getAncho() / 2) + ancho / 2;
			double kyojinY = kyojin.getY() - (kyojin.getAlto() / 2) + alto / 2;
			return x < kyojinX + kyojin.getAncho() && kyojinX < x + ancho && y < kyojinY + kyojin.getAlto()
					&& kyojinY < y + alto;
		} else {
			double kyojinX = kyojin.getX() - (kyojin.getAncho() / 2) + ancho / 2;
			double kyojinY = kyojin.getY() - (kyojin.getAlto() / 2) + alto / 2;
			return x < kyojinX + kyojin.getAncho() && kyojinX < x + ancho
					&& y - altoTitan / 4 < kyojinY + kyojin.getAlto() && kyojinY < y + altoTitan / 4 + alto;

		}
	}

	public boolean estoyColisionandoConAlgunKyojin(Kyojin[] kyojines) {
		for (int i = 0; i < kyojines.length; i++) {
			if (kyojines[i] != null) {
				if (hayColisionConKyojin(kyojines[i])) {
					kyojines[i] = null;
					return true;
				}
			}
		}
		return false;
	}

	public void rotarHaciaIzquierda(Entorno entorno) {
		angulo = angulo - 0.06;
	}

	public void rotarHaciaDerecha(Entorno entorno) {
		angulo = angulo + 0.06;
	}

	public void moverHaciaAdelante() {
		x += Math.cos(angulo) * velocidad;
		y += Math.sin(angulo) * velocidad;
	}

	public void choqueObstaculo(Entorno entorno, Obstaculo[] edificios, PowerUp powerUp) {

		if (hayColisionesMikasaConEdificios(edificios, x, y)) {
			angulo = angulo - 180;
			return;
		}

		if (y < alto / 2 || y > entorno.alto() - alto / 2) {
			angulo = angulo * -1;
			return;
		}
		if (x > entorno.ancho() - ancho / 2 || x < ancho / 2) {
			angulo = angulo - 180;
			return;
		}
	}

	public Disparo disparar() {
		return new Disparo(x, y, angulo, soyMikasaTitan);
	}

	public void involucionarAMikasaNormal() {
		soyMikasaTitan = false;
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

	public void acelerar() {
		if (modoTurbo == false) {
			velocidad += 1;
			modoTurbo = true;
		} else {
			modoTurbo = false;
			velocidad -= 1;
		}
	}

	public boolean getModoTurbo() {
		return modoTurbo;
	}

	public boolean getSoyMikasaTitan() {
		return soyMikasaTitan;
	}

	public void convertirATitan() {
		soyMikasaTitan = true;
	}

}
