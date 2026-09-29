public class clase {
	private String nombre;
	private String apellido;
	private int edad;
	private String documento;

	public clase(String nombre, String apellido, int edad, String documento) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.documento = documento;
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
}
