public class clase {
	private String nombre;
	private String apellido;
	private int edad;
	private String documento;
	private boolean estudiante;

	public clase(String nombre, String apellido, int edad, String documento, boolean estudiante) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.documento = documento;
		this.estudiante = estudiante;
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
    
    public Boolean getEstudiante(){
        return estudiante;
    }

	public void setEstudiante(Boolean estudiante){
		this.estudiante = estudiante;
	}
}
