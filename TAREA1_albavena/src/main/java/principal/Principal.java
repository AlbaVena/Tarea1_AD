package principal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import entidades.Credenciales;
import entidades.Persona;
import entidades.Espectaculo;
import entidades.Perfil;
import entidades.ProgramProperties;
import entidades.Sesion;

public class Principal {

	static Scanner leer = new Scanner(System.in);
	
	

	public static void main(String[] args) {

		// Comenzamos configurando el programa
		cargarProperties();
		ArrayList<Persona> credencialesSistema = cargarCredenciales();
		ArrayList<Espectaculo> espectaculos = cargarEspectaculos();
		ArrayList<String> paises = cargarPaises();
		
		
		Sesion actual = new Sesion();

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
			System.out.println("Elige una opcion: \n\t1. Ver espectaculos" + "\n\t2. Gestionar espectaculos"
					+ "\n\t3. Gestionar personas y credenciales" + "\n\t4. Log OUT" + "\n\t5. Salir");
			opcion = leer.nextInt();
			leer.nextLine();
			switch (opcion) {
			case 1:
				break;
			case 2:
				do {
					System.out.println("Que deseas hacer?");
					System.out.println("\t1. Crear o modificar un espectaculo\n\t2. "
							+ "Crear o modificar un numero\n\t3. " + "Asignar artistas\n\t4. Salir");
					opcion2 = leer.nextInt();
					leer.nextLine();
				} while (opcion2 != 4);
				break;

			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			default:
				System.out.println("No has introducido una opcion valida." + " Por favor intentalo de nuevo.");
			}
		} while (opcion != 5);
	}


	/**
	 * Muestra el perfil de sesion activo.
	 * 
	 * @param actual
	 */
	public static void mostrarMenuSesion(Sesion actual) {
		System.out.println("Menu " + actual.getPerfilActual() + ":");
	}

	/**
	 * METODOS:
	 */

	
	private static void cargarProperties() {
		Properties p = new Properties();
        try (
        	InputStream input = Principal.class.getClassLoader().getResourceAsStream("application.properties")){
        	p.load(input);
        	
        	ProgramProperties.usuarioAdmin= p.getProperty("usuarioAdmin");
        	ProgramProperties.passwordAdmin = p.getProperty("passwordAdmin");
        	ProgramProperties.credenciales = p.getProperty("credenciales");
        	ProgramProperties.espectaculos = p.getProperty("espectaculos");
        	ProgramProperties.paises = p.getProperty("paises");
        	
        } catch (FileNotFoundException e) {
        	System.out.println("No pude encontrar el fichero de properties");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Hubo problemas al leer el fichero de properties");
			e.printStackTrace();
		}
	}

	private static ArrayList<String> cargarPaises() {
		ArrayList<String> paises = new ArrayList<String>();
		
		//TODO cargarPaises pero, en vez de linea por linea como un xml
		
		return paises;
	}

	private static ArrayList<Espectaculo> cargarEspectaculos() {
		ArrayList<Espectaculo> espectaculos = new ArrayList<Espectaculo>();
		ArrayList<String> lineas = leerFichero(ProgramProperties.espectaculos);
		
		//TODO cargarEspectaculo
		return espectaculos;
	}

	private static ArrayList<Persona> cargarCredenciales() {
		ArrayList <Persona> personas = new ArrayList<>();
		//incluimos en administrador
		personas.add(new Persona(ProgramProperties.usuarioAdmin, ProgramProperties.passwordAdmin));
		// TODO leer el fichero de credenciales
		ArrayList<String> lineas = leerFichero(ProgramProperties.credenciales);
		
		return personas;
	} 
	
	private static ArrayList <String> leerFichero (String ruta){
		ArrayList <String> credenciales = new ArrayList<>();
		File archivo = new File(ruta);
		try {
			
			if (!archivo.exists()) {
				FileWriter writer = new FileWriter(archivo);
	        	writer.write("");
	        }
	        else {

	        		BufferedReader reader = new BufferedReader(new FileReader(ruta));
	        		String linea;
	        		while ((linea = reader.readLine()) != null)
	        		{
	        			System.out.println(linea);
	        		}
	        	
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        return credenciales;
	}
	
}
