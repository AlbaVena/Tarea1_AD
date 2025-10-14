package entidades;

public abstract class Persona {
	protected long id;
	protected String email;
	protected String nombre;
	protected String nacionalidad;
	protected Credenciales credenciales;
	
	
	public Persona(long id, String email, String nombre, String nacionalidad, Credenciales credenciales) {
		super();
		this.id = id;
		this.email = email;
		this.nombre = nombre;
		this.nacionalidad = nacionalidad;
		this.credenciales = credenciales;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getNacionalidad() {
		return nacionalidad;
	}


	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}


	public Credenciales getCredenciales() {
		return credenciales;
	}


	public void setCredenciales(Credenciales credenciales) {
		this.credenciales = credenciales;
	}
	
	
	
	
	

}
