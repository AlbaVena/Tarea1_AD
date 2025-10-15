package principal;

import java.util.Scanner;

import entidades.Perfil;
import entidades.Sesion;

public class Principal {

	static Scanner leer = new Scanner(System.in);

	public static void main(String[] args) {

		Sesion actual = new Sesion(Perfil.INVITADO, false);

		int opcion = -1, opcion2 = -1;

		System.out.println("**Bienvenido al Circo**");

		// MENU INVITADO
		/**
		 * 1. ver espectaculos 2. Log IN 3. Salir
		 */
		mostrarMenuSesion(actual);
		do {
			System.out.println("Elige una opcion: \n\t1. Ver espectaculos\n\t.2 " + "Log IN\n\t3. Salir");
			opcion = leer.nextInt();
			leer.nextLine();
			switch (opcion) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			default:
				System.out.println("No has introducido una opcion valida." + " Por favor intentalo de nuevo.");

			}
		} while (opcion != 3);

		// MENU COORDINACION
		/**
		 * 1. ver espectaculos 2. gestionar espectaculos 2.1 crear-modificar espectaculo
		 * 2.2 crear-modificar numero 2.3 asignar artistas 3. Log OUT
		 */
		mostrarMenuSesion(actual);
		do {
			System.out.println("Elige una opcion: \n\t1. Ver espectaculos\n\t.2 "
					+ "Crear o Modificar espectaculos\n\t3. Log OUT\n\t4. Salir");
			opcion = leer.nextInt();
			leer.nextLine();
			switch (opcion) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			default:
				System.out.println("No has introducido una opcion valida." + " Por favor intentalo de nuevo.");
			}

		} while (opcion != 4);

		// MENU ARTISTA
		/**
		 * 1. ver tu ficha 2. ver espectaculos 3. Log OUT
		 */
		mostrarMenuSesion(actual);
		do {
			System.out.println(
					"Elige una opcion: \n\t1. Ver tu ficha\n\t2. Ver " + "espectaculos\n\t3. Log OUT\n\t4. Salir");
			opcion = leer.nextInt();
			leer.nextLine();
			switch (opcion) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			default:
				System.out.println("No has introducido una opcion valida." + " Por favor intentalo de nuevo.");
			}

		} while (opcion != 4);

		// MENU ADMIN
		/**
		 * 1. ver espectaculos 2. gestionar espectaculos 2.1 crear-modificar espectaculo
		 * 2.2 crear-modificar numero 2.3 asignar artistas 3. gestionar personas y
		 * credenciales 3.1 registrar persona 3.2 asignar perfil y credenciales 3.3
		 * gestionar datos artista-coordinador 4. Log OUT
		 */
		
		mostrarMenuSesion(actual);
		do {
			System.out.println(
					"Elige una opcion: \n\t1. Ver espectaculos"
					+ "\n\t2. Gestionar espectaculos"
					+ "\n\t3. Gestionar personas y credenciales"
					+ "\n\t4. Log OUT"
					+ "\n\t5. Salir");
			opcion = leer.nextInt();
			leer.nextLine();
			switch (opcion) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			default: System.out.println("No has introducido una opcion valida." + " Por favor intentalo de nuevo.");
			}
		}
		while(opcion != 5);
	}

	public static void mostrarMenuSesion(Sesion actual) {
		System.out.println("Menu " + actual.getPerfilActual() + ":");
	}

}
