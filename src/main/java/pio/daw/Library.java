package pio.daw;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.io.IOException;

public class Library implements Controlable {

    private Map<String, User> users;

    /**
     * Read the library register file (.txt) and create a library object
     * with the current status of the users.
     * @param path Library registry file path.
     * @return Library object.
     */
    public static Library fromFile(Path path){
        Library library = new Library();

        try {
            // Leemos todas las líneas del fichero
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                // Separar ID y evento
                String[] parts = line.split(";");

                if(parts.length != 2) continue;

                String id = parts[0].trim();
                String event = parts[1].trim();

                EventType e = event.equals("ENTRADA") ? EventType.ENTRY : EventType.EXIT;

                // Registramos el evento en la biblioteca
                library.registerChange(id, e);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo el fichero: " + e.getMessage());
        }

        return library;
    }

    private Library(){
        this.users = new HashMap<>();
    }

    /**
     * Si el usuario no existe, lo crea.
     * Luego actualiza su estado según el evento.
     */
    public void registerChange(String id, EventType e) {

        // Si el usuario no existe, lo creamos
        users.putIfAbsent(id, new User(id));

        User u = users.get(id);

        // Si es entrada
        if(e == EventType.ENTRY) {
            // Si ya estaba dentro, no contamos otra entrada
            if(!u.isInside()) {
                u.enter();
            }
        } else { // si es salida
            // Solo se permite salir si estaba dentro
            if(u.isInside()) {
                u.exit();
            }
            // Si no estaba dentro, descartamos el evento
        }
    }

    /**
     * Devuelve usuarios actualmente dentro (ordenados por ID)
     */
    public List<User> getCurrentInside() {
        List<User> inside = new ArrayList<>();

        for(User u : users.values()) {
            if(u.isInside()) {
                inside.add(u);
            }
        }

        inside.sort(Comparator.comparing(User::getId));
        return inside;
    }

    /**
     * Devuelve el/los usuarios con más entradas
     */
    public List<User> getMaxEntryUsers() {
        List<User> list = new ArrayList<>(users.values());

        // Ordenamos por entradas descendente
        list.sort((a, b) -> Integer.compare(b.getEntryCount(), a.getEntryCount()));

        List<User> maxUsers = new ArrayList<>();

        if(list.size() == 0) return maxUsers;

        int max = list.get(0).getEntryCount();

        for(User u : list) {
            if(u.getEntryCount() == max) {
                maxUsers.add(u);
            }
        }

        return maxUsers;
    }

    /**
     * Devuelve todos los usuarios ordenados por ID
     */
    public List<User> getUserList() {
        List<User> list = new ArrayList<>(users.values());
        list.sort(Comparator.comparing(User::getId));
        return list;
    }

    /**
     * Imprime el resumen final
     */
    public void printResume() {
        System.out.println("Usuarios actualmente dentro de la biblioteca:");

        for(User u : getCurrentInside()) {
            System.out.println(u.getId());
        }

        System.out.println("Número de entradas por usuario:");
        for(User u : getUserList()) {
            System.out.println(u.getId() + " -> " + u.getEntryCount());
        }

        System.out.print("Usuario(s) con más entradas: ");
        List<User> maxUsers = getMaxEntryUsers();

        for(int i = 0; i < maxUsers.size(); i++) {
            System.out.print(maxUsers.get(i).getId());
            if(i < maxUsers.size()-1) System.out.print(", ");
        }
        System.out.println();
    }
}
