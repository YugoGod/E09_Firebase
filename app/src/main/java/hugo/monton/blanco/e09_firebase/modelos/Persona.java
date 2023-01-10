package hugo.monton.blanco.e09_firebase.modelos;

public class Persona {

    // ATRIBUTOS
    private String nombre;
    private int edad;

    // CONSTRUCTORES -> Hay que poner todos los constructores que hagan falta para la base de datos.
    public Persona() {
    }
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    // GETTERS AND SETTERS
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
}
