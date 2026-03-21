package pio.daw;
 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
 
public class App {
 
    public static Path getPathFromArgs(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Numero de argumentos incorrecto. Uso: java App <ruta_fichero>");
        }
 
        Path path = Paths.get(args[0]);
 
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("El fichero no existe: " + args[0]);
        }
 
        return path;
    }
 
    public static void main(String[] args) {
        try {
            Path p = getPathFromArgs(args);
            Controlable controler = Library.fromFile(p);
            controler.printResume();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Uso: java App <ruta_fichero>");
        }
    }
}