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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

import entidades.Persona;
import entidades.Credenciales;
import entidades.Especialidad;
import entidades.Espectaculo;
import entidades.Perfil;
import entidades.ProgramProperties;
import entidades.Sesion;

public class Principal {

	static Scanner leer = new Scanner(System.in);

	static ArrayList<Persona> credencialesSistema = null;
	static ArrayList<Espectaculo> espectaculos = null;
	static Map<String, String> paises = null;
	static Sesion actual = new Sesion();

	public static void main(String[] args) {

		// Comenzamos configurando el programa

		cargarProperties();
		credencialesSistema = cargarCredenciales();
		espectaculos = cargarEspectaculos();
		paises = cargarPaises();

		System.out.println("**Bienvenido al Circo**");

		// MENU INVITADO
		/**
		 * 1. ver espectaculos 2. Log IN 3. Salir
		 */
		int opcion = -1;
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
				System.out.println("Cerrando el programa...");
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

	private static String getNodo(String etiqueta, Element elem) { // "etiqueta" concreta
		NodeList nodo = elem.getElementsByTagName(etiqueta).item(0).getChildNodes(); // busca todas las qtiquetas hijas
																						// con el nombre de la etiqueta
		// devuelve los nodos hijos
		Node valorNodo = nodo.item(0); // primer hijo ID
		return valorNodo.getNodeValue(); // el nodo de TEXTO (valor real) NOMBRE
	}
	public static Espectaculo crearEspectaculo() {
		Espectaculo nuevoEspectaculo = null;
		if (actual.getPerfilActual()==Perfil.COORDINACION) {
			
		}
		
		return nuevoEspectaculo;
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
		// leer el fichero de credenciales
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

		if (usuario.equals(ProgramProperties.usuarioAdmin) && password.equals(ProgramProperties.passwordAdmin)) {
			usuarioLogueado = new Persona(ProgramProperties.usuarioAdmin, ProgramProperties.passwordAdmin);
		} else {
			for (Persona p : credenciales) {
				if (p.getCredenciales().getNombre().equals(usuario)
						&& p.getCredenciales().getPassword().equals(password)) {
					usuarioLogueado = p;
				}
			}
		}
		return usuarioLogueado;
	}

	public static void logOut() {
		System.out.println("Has cerrado la sesion");
		actual.setUsuActual(new Persona());
	}

	// MENUS
	// MENU COORDINACION
	/**
	 * 1. ver espectaculos 2. gestionar espectaculos 2.1 crear-modificar espectaculo
	 * 2.2 crear-modificar numero 2.3 asignar artistas 3. Log OUT
	 */
	public static void menuCoordinacion() {
		int opcion = -1;
		mostrarMenuSesion(actual);
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
		int opcion = -1;
		mostrarMenuSesion(actual);
		do {
			System.out.println("Elige una opcion: \n\t1. Ver tu ficha\n\t2. Ver " + "espectaculos\n\t3. Log OUT");
			opcion = leer.nextInt();
			leer.nextLine();
			switch (opcion) {
			case 1:
				System.out.println("--Ficha del artista--\nNombre: " + actual.getUsuActual().getNombre() + "\nID: "
						+ actual.getUsuActual().getId());

				break;
			case 2:
				break;
			case 3:
				logOut();

				break;
			default:
				System.out.println("No has introducido una opcion valida." + " Por favor intentalo de nuevo.");
			}

		} while (opcion != 3);
	}

	// MENU ADMIN
	/**
	 * 1. ver espectaculos 2. gestionar espectaculos 2.1 crear-modificar espectaculo
	 * 2.2 crear-modificar numero 2.3 asignar artistas 3. gestionar personas y
	 * credenciales 3.1 registrar persona 3.2 asignar perfil y credenciales 3.3
	 * gestionar datos artista-coordinador 4. Log OUT
	 */
	public static void menuAdmin() {
		int opcion = -1;

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
				gestionarEscpectaculos();
				break;

			case 3:
				gestionarPersonas();
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

	public static void gestionarPersonas() {
		int opcion2 = -1;
		Persona nueva = null;
		do {
			System.out.println("Que deseas hacer?");
			System.out.println("\t.1 Registrar persona\n\t.2"
					+ "Gestionar datos artista o coordinador\n\t3. Salir");
			opcion2 = leer.nextInt();
			leer.nextLine();
			switch (opcion2) {
			case 1:

				do {
					nueva = registrarPersona();
					nueva.setId(credencialesSistema.size()+1);
					credencialesSistema.add(nueva);
					persistirCredenciales();
				} while (nueva == null);
				// hasta que persona no null o pulse salir
				// do-while op1 registrar op2 salir
				break;
			case 2:

				break;
			case 3:
				break;

			default:
				System.out.println("no has introducido una opcion valida.");
			}

		} while (opcion2 != 3);
	}

	public static void gestionarEscpectaculos() {
		int opcion2 = -1;
		do {
			System.out.println("Que deseas hacer?");
			System.out.println("\t1. Crear o modificar un espectaculo\n\t2. " + "Crear o modificar un numero\n\t3. "
					+ "Asignar artistas\n\t4. Salir");
			opcion2 = leer.nextInt();
			leer.nextLine();
			switch (opcion2) {

			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			default:
				System.out.println("no has introducido una opcion valida.");
			}
		} while (opcion2 != 4);
	}

	// registrar persona nueva
	public static Persona registrarPersona() {
		Persona resultadoLogin = null;
		String email, nombre, nacionalidad;
		String nombreUsuario = null, passUsuario = null;
		Perfil perfilUsu = null;
		Boolean senior = false;
		String apodo = null;
		LocalDate fecha = null;
		Set<Especialidad> especialidadesUsu = new HashSet<>();

		/**
		 * DATOS PERSONALES
		 */
		System.out.println("introduce un email");
		email = leer.nextLine();
		if (!comprobarEmail(email)) {
			System.out.println("Ese email ya esta registrado");
			return null;
		}
		System.out.println("introduce el nombre de la persona");
		nombre = leer.nextLine();

		System.out.println("introduce el id del pais elegido");
		for (Entry<String, String> entrada : paises.entrySet()) {
			System.out.println(entrada);
		}
		nacionalidad = leer.nextLine().toUpperCase();
		if (paises.containsKey(nacionalidad)) {
			nacionalidad = paises.get(nacionalidad);
		} else {
			System.out.println("Ese pais no se encuentra");
			return null;
		}

		/*
		 * DATOS PROFESIONALES
		 */
		int num = -1;
		do {
			System.out.println("El usuario es Coordinador (1) o Artista (2)?");

			num = leer.nextInt();

			leer.nextLine();
			switch (num) {
			case 1:
				// TODO arreglar menu coordinacion
				perfilUsu = Perfil.COORDINACION;
				int num2 = 0;
				System.out.println("El coordinador es senior? (1- si , 2- no)");
				num2 = leer.nextInt();
				leer.nextLine();

				switch (num2) {
				case 1:
					senior = true;
					System.out.println("desde que fecha es senior? (formato yyyy-mm-dd)");
					fecha = LocalDate.parse(leer.nextLine());
					break;
				case 2:
					senior = false;
					break;
				default:
					System.out.println("no has elegido una opcion valida");
					break;
				}
				break;

			case 2:
				// TODO arreglar menu artista
				perfilUsu = Perfil.ARTISTA;
				System.out.println("el artista tiene apodo? (1-si , 2-no)");

				int num3 = leer.nextInt();
				leer.nextLine();

				switch (num3) {
				case 1:
					System.out.println("cual es su apodo?");
					apodo = leer.nextLine().trim().toLowerCase();
					break;
				case 2:
					apodo = null;
					break;
				default:
					System.out.println("no has elegido una opcion valida");
					break;
				}
				int i = 1;
				System.out.println("indica sus especialidades separadas por comas: ");
				for (Especialidad e : Especialidad.values()) {
					System.out.println(i + "-" + e);
					i++;
				}
				String[] seleccion = leer.nextLine().split(",");

				for (String s : seleccion) {
					int elegida = Integer.parseInt(s.trim());
					switch (elegida) {
					case 1 -> especialidadesUsu.add(Especialidad.ACROBACIA);
					case 2 -> especialidadesUsu.add(Especialidad.HUMOR);
					case 3 -> especialidadesUsu.add(Especialidad.MAGIA);
					case 4 -> especialidadesUsu.add(Especialidad.EQUILIBRISMO);
					case 5 -> especialidadesUsu.add(Especialidad.MALABARISMO);
					default -> System.out.println("Has introducido una opcion invalida");
					}
				}

				break;
			default:
				System.out.println("La opcion elegida no es valida");
				break;
			}
		} while (num != 1 || num != 2);

		/**
		 * DATOS DE CREDENCIALES
		 */

		do {
			System.out.println("introduce el nombre de usuario (ten en cuenta que "
					+ "no admitira letras con tildes o dieresis, ni espacios en blanco)");
			String cadena = leer.nextLine().trim();

			if (cadena.matches("^[a-zA-Z_-]{3,}$")) {
				nombreUsuario = cadena.toLowerCase();
			} else
				System.out.println("ese nombre de usuario no es valido");
		} while (nombreUsuario == null);

		do {
			System.out.println("por ultimo introduce una contraseña valida (debe"
					+ " tener mas de 2 caracteres, y ningun espacio en blanco");
			String pass = leer.nextLine();
			if (pass.matches("^\\S{3,}$")) {
				passUsuario = pass;
			} else
				System.out.println("contraseña no valida");
		} while (passUsuario == null);
		Credenciales credenciales = new Credenciales(nombreUsuario, passUsuario, perfilUsu);		

		return resultadoLogin = new Persona(-1, email, nombreUsuario, nacionalidad, credenciales, perfilUsu);
	}
	
	public static void persistirCredenciales() {
		try {
			FileWriter writer = new FileWriter(ProgramProperties.credenciales);
			for (Persona p : credencialesSistema) {
				writer.write(p.toFicheroCredenciales());
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO 
	}

	public static Boolean comprobarEmail(String email) {
		Boolean valido = true;
		for (Persona p : credencialesSistema) {
			if (p.getEmail() == email) {
				// TODO rellenar mensaje error
				return false;
			}
		}
		return valido;
	}

	public static Boolean comprobarNombreUsuario(String nombreUsuario) {
		Boolean valido = true;
		for (Persona p : credencialesSistema) {
			if (p.getCredenciales().getNombre() == nombreUsuario) {
				// TODO rellenar mensaje error
				return false;
			}
		}
		// Si no hemos fallado en ningún validador, construimos la Persona
		// resultadoLogin = new Persona(..);
		return valido;
	}
	
	

	public static void asignarPerfilYCredenciales() {

		String nombreUsuario, password, perfil;

		// TODO coordinador senior-> fecha // artista -> apodo & especialidades

		System.out.println("introduce un nombre de usuario");
		nombreUsuario = leer.nextLine();
		if (!comprobarNombreUsuario(nombreUsuario)) {
			System.out.println("Ese nombre de usuario ya esta registrado");
		}
	}
	/**
	  
	 */
}
