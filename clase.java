public class clase {
	private String nombre;
	private String apellido;
	private int edad;
	private String documento;
	private String estadoCivil;

	public clase(String nombre, String apellido, int edad, String documento, String estadoCivil) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.documento = documento;
		this.estadoCivil = estadoCivil;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public int getEdad() {
		return edad;
	}

	public String getDocumento() {
		return documento;
	}


	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

    
}
