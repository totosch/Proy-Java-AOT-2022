package juego;

import java.awt.Image;
import java.util.Random;

import javax.sound.sampled.Clip;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	private Entorno entorno;

	private Mikasa mikasa;

	private Kyojin[] kyojines;
	private int cantidadDeKyojinesAsesinados;

	private boolean invulnerabilidadDeMikasa;

	private PowerUp powerUp;

	private Random random;

	private Obstaculo[] obstaculos;
	private Disparo disparo;

	private int temporizador;
	private int temporizadorVelocidad;

	private boolean playMusica;
	private boolean standby;

	private Clip musica;
	private Clip dispararEspada;
	private Clip muerteMikasa;
	private Clip recargar;
	private Clip winTheme;

	private int cantidadDeMuniciones;
	private int cantidadDisparosRealizados;

	private Image fondo;
	private Image gameoverScreen;
	private Image winScreen;

	public Juego() {

		this.entorno = new Entorno(this, "Mikasa", 800, 600);

		fondo = Herramientas.cargarImagen("fondo.png");

		obstaculos = new Obstaculo[5];

		obstaculos[0] = new Obstaculo((entorno.ancho() / 5) * 2 - ((entorno.ancho() / 10)),
				((entorno.alto() / 5) * 2) - (entorno.alto() / 10), 1);
		obstaculos[1] = new Obstaculo((entorno.ancho() / 5) * 4 - ((entorno.ancho() / 10)),
				((entorno.alto() / 5) * 2) - (entorno.alto() / 10), 1);
		obstaculos[2] = new Obstaculo((entorno.ancho() / 5) * 2 - ((entorno.ancho() / 10)),
				((entorno.alto() / 5) * 4) - (entorno.alto() / 10), 1);
		obstaculos[3] = new Obstaculo((entorno.ancho() / 5) * 4 - ((entorno.ancho() / 10)),
				((entorno.alto() / 5) * 4) - (entorno.alto() / 10), 1);
		obstaculos[4] = new Obstaculo(entorno.ancho() / 2, entorno.alto() / 2 - 15, 20, 50, 4);

		cantidadDeMuniciones = 15;
		cantidadDisparosRealizados = 0;

		random = new Random();

		mikasa = new Mikasa(entorno.ancho() / 2, entorno.alto() / 2 + 30, 1.5);

		kyojines = new Kyojin[5];
		cantidadDeKyojinesAsesinados = 0;

		invulnerabilidadDeMikasa = false;

		powerUp = null;

		disparo = null;

		temporizador = 0;

		musica = Herramientas.cargarSonido("shingekiNoKyojinOP.wav");
		dispararEspada = Herramientas.cargarSonido("espadaDisparar.wav");
		muerteMikasa = Herramientas.cargarSonido("gameover.wav");
		gameoverScreen = Herramientas.cargarImagen("gameover.gif");
		winScreen = Herramientas.cargarImagen("winScreen.png");
		recargar = Herramientas.cargarSonido("recargaEspadas.wav");

		this.entorno.iniciar();

	}

	public void tick() {
		if (!playMusica && !standby) {
			musica.start();
			playMusica = true;
		}

		if (mikasa == null) {
			muerteMikasa.start();
			musica.stop();
			standby = true;
			entorno.dibujarImagen(gameoverScreen, entorno.ancho() / 2, entorno.alto() / 2, 0);
			if (entorno.sePresiono(entorno.TECLA_ENTER)) {
				mikasa = new Mikasa(entorno.ancho() / 2, entorno.alto() / 2 + 30, 1.5);
				kyojines = new Kyojin[5];
				cantidadDisparosRealizados = 0;
				cantidadDeKyojinesAsesinados = 0;
				temporizador = 0;
				powerUp = null;
				muerteMikasa.stop();
				standby = false;
			}
			return;
		}

		if (mikasa != null && cantidadDeKyojinesAsesinados == 15) {
			winTheme.start();
			musica.stop();
			entorno.dibujarImagen(winScreen, entorno.ancho() / 2, entorno.alto() / 2, 0, 0.5);
			standby = true;
			if (entorno.sePresiono(entorno.TECLA_ENTER)) {
				mikasa = new Mikasa(entorno.ancho() / 2, entorno.alto() / 2 + 30, 1.5);
				kyojines = new Kyojin[5];
				cantidadDisparosRealizados = 0;
				cantidadDeKyojinesAsesinados = 0;
				temporizador = 0;
				powerUp = null;
				winTheme.stop();
				standby = false;

			}
			return;
		}

		entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto() / 2, 0);

		for (Obstaculo obstaculo : obstaculos) {
			obstaculo.dibujar(entorno);
		}

		mikasa.dibujar(entorno);

		//// POWER UPS ////

		if (temporizador != 0 && temporizador % 750 == 0 && powerUp == null) {
			int[] posicionArribaYAbajo = { 1, 9 };
			int eleccionAleatoriaPowerUp = random.nextInt(2);
			int yPowerUp = 60 * posicionArribaYAbajo[eleccionAleatoriaPowerUp];
			int xPowerUp = 0;
			xPowerUp = 80 + random.nextInt(440);
			int tipoPowerUp = random.nextInt(3);
			powerUp = new PowerUp(xPowerUp, yPowerUp, tipoPowerUp);
		}

		if (powerUp != null) {
			powerUp.dibujar(entorno);
		}

		boolean quieroRecargar = false;

		if (quieroRecargar == true) {
			recargar.start();
		}

		if (powerUp != null && mikasa.hayColisionPowerUp(powerUp)) {
			if (powerUp.getTipo() == 1) {
				quieroRecargar = true;
			} else if (powerUp.getTipo() == 2) {
				mikasa.acelerar();
			} else {
				mikasa.convertirATitan();
			}
			powerUp = null;
		}

		if (mikasa.getModoTurbo() == true) {
			temporizadorVelocidad += 1;
			if (temporizadorVelocidad % 350 == 0) {
				mikasa.acelerar();
				temporizadorVelocidad = 0;
			}
		}

		if (mikasa.getSoyMikasaTitan()) {
			invulnerabilidadDeMikasa = true;
		}

		//// KYOJINES ////

		if (temporizador == 0) {
			for (int i = 0; i < 5; i++) {
				kyojines[i] = new Kyojin(100 + 150 * i, 90 * i, 1);
			}
		}

		for (int i = 0; i < kyojines.length; i++) {
			if (kyojines[i] != null) {
				kyojines[i].dibujar(entorno);
				kyojines[i].siguiendoAMikasa(entorno, mikasa, obstaculos, kyojines);
			}
			if (kyojines[i] == null) {
				for (int j = 0; j < kyojines.length; j++) {
					if (kyojines[j] != null && temporizador % 50 == 0) {
						kyojines[i] = kyojines[j].spawnearKyojin(mikasa, entorno);
					}
				}
			}
			if (cantidadDeKyojinesAsesinados == 15) {
				kyojines[i] = null;
			}
		}

		entorno.escribirTexto("KYOJINES ASESINADOS: " + cantidadDeKyojinesAsesinados + "", 620, 40);
		entorno.escribirTexto("MATA A 15 KYOJINES PARA GANAR", 20, 590);

		//// KYOJINES ////

		if (invulnerabilidadDeMikasa == true) {
			if (mikasa.estoyColisionandoConAlgunKyojin(kyojines)) {
				invulnerabilidadDeMikasa = false;
				mikasa.involucionarAMikasaNormal();
				cantidadDeKyojinesAsesinados++;
			}
		}

		if (entorno.estaPresionada('a')) {
			mikasa.rotarHaciaIzquierda(entorno);
		}

		if (entorno.estaPresionada('d')) {
			mikasa.rotarHaciaDerecha(entorno);
		}

		if (entorno.estaPresionada('w')) {
			mikasa.moverHaciaAdelante();
			mikasa.choqueObstaculo(entorno, obstaculos, powerUp);
		}

		////// DISPARO //////

		entorno.escribirTexto("MUNICION GASTADA: " + cantidadDisparosRealizados + " / " + cantidadDeMuniciones, 620,
				20);

		if (entorno.sePresiono(entorno.TECLA_ESPACIO) && disparo == null) {
			if (cantidadDisparosRealizados < cantidadDeMuniciones) {
				disparo = mikasa.disparar();
				dispararEspada.loop(1);
				cantidadDisparosRealizados++;
			}
		}

		if (disparo != null) {

			disparo.dibujar(entorno);
			disparo.avanzar();

			if (disparo.estoyColisionandoEntorno(entorno)) {
				disparo = null;
			}

			if (disparo != null) {
				if (disparo.estoyColicionandoConMuchosObstaculos(obstaculos)) {
					disparo = null;
				}
			}

			if (disparo != null) {
				if (disparo.colisionKyojin(kyojines) == true) {
					cantidadDeKyojinesAsesinados++;
					disparo = null;
				}
			}
		}

		if (quieroRecargar == true) {
			if (cantidadDisparosRealizados < 5) {
				cantidadDisparosRealizados = 0;
			} else {
				cantidadDisparosRealizados = cantidadDisparosRealizados - 5;
			}
			quieroRecargar = false;
			recargar.start();
		}
		////// DISPARO //////

		if (mikasa != null && mikasa.estoyColisionandoConAlgunKyojin(kyojines)) {
			mikasa = null;
		}
		temporizador += 1;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

}