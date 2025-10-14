package entidades;

public class Sesion {
	private String usuActual = Perfil.INVITADO.toString();
	private Perfil perfilActual = Perfil.INVITADO;
	private boolean logueado;
	
	public Sesion( Perfil perfilActual, boolean logueado) {
		super();
		this.usuActual = perfilActual.name();
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
