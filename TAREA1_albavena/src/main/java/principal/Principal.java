package principal;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

import entidades.Credenciales;
import entidades.Persona;
import entidades.Espectaculo;
import entidades.Perfil;
import entidades.ProgramProperties;
import entidades.Sesion;
import util.MenuUtils;

public class Principal {

	static Scanner leer = new Scanner(System.in);

	static ArrayList<Persona> credencialesSistema = null;
	static ArrayList<Espectaculo> espectaculos = null;
	static ArrayList<String> paises = null;

	static int opcion = -1, opcion2 = -1;

	public static void main(String[] args) {

		// Comenzamos configurando el programa

		cargarProperties();
		credencialesSistema = cargarCredenciales();
		espectaculos = cargarEspectaculos();
		Map<String, String> paises = cargarPaises();

		Sesion actual = new Sesion();
		
		System.out.println("**Bienvenido al Circo**");

		// MENU INVITADO
		/**
		 * 1. ver espectaculos 2. Log IN 3. Salir
		 */
		
		do {
			mostrarMenuSesion(actual);
			System.out.println("Elige una opcion: \n\t1. Ver espectaculos\n\t2. " + "Log IN\n\t3. Salir");
			opcion = leer.nextInt();
			leer.nextLine();
			switch (opcion) {
			case 1:
				cargarEspectaculos();
				break;
			case 2:
				Persona usuarioIntento = login(credencialesSistema);
				if (usuarioIntento != null) {
					actual = new Sesion(usuarioIntento);
					switch (actual.getPerfilActual()) {
					case ARTISTA:
						menuArtista();
						break;
					case COORDINACION:
						menuCoordinacion();
						break;
					case ADMIN:
						menuAdmin();
						break;
					default:
						System.out.println("Perfil no reconocido");
					}

				} else
					System.out.println("usuario o contraseña incorrecto");

				break;
			case 3:
				break;
			default:
				System.out.println("No has introducido una opcion valida." + " Por favor intentalo de nuevo.");

			}
		} while (opcion != 3);

	}

	/**
	 * METODOS:
	 */

	/**
	 * Muestra el perfil de sesion activo.
	 * 
	 * @param actual
	 */
	public static void mostrarMenuSesion(Sesion actual) {
		System.out.println("Menu " + actual.getPerfilActual() + ":");
	}

	private static void cargarProperties() {
		Properties p = new Properties();
		try (InputStream input = Principal.class.getClassLoader().getResourceAsStream("application.properties")) {
			p.load(input);

			ProgramProperties.usuarioAdmin = p.getProperty("usuarioAdmin");
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

	private static Map<String, String> cargarPaises() {
		Map<String, String> paises = new HashMap<String, String>();

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document documento = builder.parse(ProgramProperties.paises);
			documento.getDocumentElement().normalize();

			NodeList listaPaises = documento.getElementsByTagName("pais"); // en la lista los elementos con etiqueta
																			// "pais"
			for (int i = 0; i < listaPaises.getLength(); i++) {
				Node nodo = listaPaises.item(i); // me devuelve el nodo en posicion i

				if (nodo.getNodeType() == Node.ELEMENT_NODE) { // devuelve un entero. solo los elementos tienen
																// etiquetas hijo
					Element elemento = (Element) nodo; // lo covertimos a element para usar los metodos <PAIS>

					String id = getNodo("id", elemento);
					String nombre = getNodo("nombre", elemento);

					paises.put(id, nombre);

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return paises;
	}

	private static String getNodo(String etiqueta, Element elem) { //"etiqueta" concreta
		NodeList nodo = elem.getElementsByTagName(etiqueta).item(0).getChildNodes(); //busca todas las qtiquetas hijas con el nombre de la etiqueta
																	//devuelve los nodos hijos 
		Node valorNodo = nodo.item(0); //primer hijo ID
		return valorNodo.getNodeValue(); //el nodo de TEXTO (valor real) NOMBRE
	}

	private static ArrayList<Espectaculo> cargarEspectaculos() {
		ArrayList<Espectaculo> espectaculos = new ArrayList<Espectaculo>();
		File archivo = new File(ProgramProperties.espectaculos);
		if (!archivo.exists()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(ProgramProperties.espectaculos))) {
				oos.writeObject(espectaculos);
				oos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ProgramProperties.espectaculos))) {

				espectaculos = (ArrayList<Espectaculo>) ois.readObject();
				ois.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return espectaculos;
	}

	private static ArrayList<Persona> cargarCredenciales() {
		ArrayList<Persona> personas = new ArrayList<>();
		// incluimos el administrador
		personas.add(new Persona(ProgramProperties.usuarioAdmin, ProgramProperties.passwordAdmin));
		// TODO leer el fichero de credenciales
		ArrayList<String> lineas = leerFichero(ProgramProperties.credenciales);

		for (String linea : lineas) {
			personas.add(new Persona(linea));
		}
		return personas;
	}

	private static ArrayList<String> leerFichero(String ruta) {
		ArrayList<String> lineas = new ArrayList<>();
		File archivo = new File(ruta);
		try {

			if (!archivo.exists()) {
				FileWriter writer = new FileWriter(archivo);
				writer.write("");
			} else {

				BufferedReader reader = new BufferedReader(new FileReader(ruta));
				String linea;
				while ((linea = reader.readLine()) != null) {
					lineas.add(linea);
				}

			}
		} catch (IOException e) {
			System.out.println("No se ha podido cargar el fichero: " + ruta);
			e.printStackTrace();
		}

		return lineas;
	}

	private static Persona login(ArrayList<Persona> credenciales) {
		String usuario, password;
		Persona usuarioLogueado = null;

		do {
			System.out.println("Introduce tu nombre de usuario");
			usuario = leer.nextLine();
		} while (usuario == null);

		do {
			System.out.println("Introduce tu contraseña");
			password = leer.nextLine();
		} while (password == null);

		for (Persona p : credenciales) {
			if (p.getCredenciales().getNombre().equals(usuario) && p.getCredenciales().getPassword().equals(password)) {
				usuarioLogueado = p;
			}
		}
		return usuarioLogueado;
	}

	// MENUS
	// MENU COORDINACION
	/**
	 * 1. ver espectaculos 2. gestionar espectaculos 2.1 crear-modificar espectaculo
	 * 2.2 crear-modificar numero 2.3 asignar artistas 3. Log OUT
	 */
	public static void menuCoordinacion() {
		do {
			System.out.println("Menu COORDINACION\nElige una opcion: \n\t1. Ver espectaculos\n\t.2 "
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
	}

	// MENU ARTISTA
	/**
	 * 1. ver tu ficha 2. ver espectaculos 3. Log OUT
	 */
	public static void menuArtista() {

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
	}

	// MENU ADMIN
	/**
	 * 1. ver espectaculos 2. gestionar espectaculos 2.1 crear-modificar espectaculo
	 * 2.2 crear-modificar numero 2.3 asignar artistas 3. gestionar personas y
	 * credenciales 3.1 registrar persona 3.2 asignar perfil y credenciales 3.3
	 * gestionar datos artista-coordinador 4. Log OUT
	 */
	public static void menuAdmin() {

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
}
