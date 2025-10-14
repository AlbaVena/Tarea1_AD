package entidades;

public class Sesion {
	private String usuActual;
	private Perfil perfilActual = Perfil.INVITADO;
	private boolean logueado;
	
	public Sesion(String usuActual, Perfil perfilActual, boolean logueado) {
		super();
		this.usuActual = usuActual;
		this.perfilActual = perfilActual;
		this.logueado = logueado;
	}

	public String getUsuActual() {
		return usuActual;
	}

	public void setUsuActual(String usuActual) {
		this.usuActual = usuActual;
	}

	public Perfil getPerfilActual() {
		return perfilActual;
	}

	public void setPerfilActual(Perfil perfilActual) {
		this.perfilActual = perfilActual;
	}

	public boolean isLogueado() {
		return logueado;
	}

	public void setLogueado(boolean logueado) {
		this.logueado = logueado;
	}

	
}
