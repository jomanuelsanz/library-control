package pio.daw;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class Library implements Controlable {
 
    private Map<String, User> users;
 
    public static Library fromFile(Path path) {
        Library library = new Library();
 
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
 
                String[] splittedLine = line.split(";");
                if (splittedLine.length != 2) continue;
 
                String id = splittedLine[0].trim();
                EventType e = null;
 
                if (splittedLine[1].trim().equals("ENTRADA")) {
                    e = EventType.ENTRY;
                } else if (splittedLine[1].trim().equals("SALIDA")) {
                    e = EventType.EXIT;
                }
 
                if (e != null) {
                    library.registerChange(id, e);
                }
            }
        } catch (IOException ex) {
            System.err.println("Error al leer el fichero: " + ex.getMessage());
            System.exit(1);
        }
 
        return library;
    }
 
    private Library() {
        this.users = new HashMap<>();
    }
 
    @Override
    public void registerChange(String id, EventType e) {
        User u = this.users.get(id);
        if (u == null) {
            u = new User(id);
        }
        u.registerNewEvent(e);
        this.users.put(id, u);
    }
 
    @Override
    public List<User> getCurrentInside() {
        List<User> inside = new ArrayList<>();
        for (User u : this.users.values()) {
            if (u.isInside()) {
                inside.add(u);
            }
        }
        inside.sort(Comparator.comparing(User::getId));
        return inside;
    }
 
    @Override
    public List<User> getMaxEntryUsers() {
        if (this.users.isEmpty()) return new ArrayList<>();
 
        int max = 0;
        for (User u : this.users.values()) {
            if (u.getNEntries() > max) {
                max = u.getNEntries();
            }
        }
 
        List<User> topUsers = new ArrayList<>();
        for (User u : this.users.values()) {
            if (u.getNEntries() == max) {
                topUsers.add(u);
            }
        }
 
        topUsers.sort(Comparator.comparing(User::getId));
        return topUsers;
    }
 
    @Override
public List<User> getUserList() {
    List<User> list = new ArrayList<>();
    for (User u : this.users.values()) {
        if (u.getNEntries() > 0) {
            list.add(u);
        }
    }
    list.sort(Comparator.comparing(User::getId));
    return list;
}
 
    @Override
    public void printResume() {
        String nl = "\n";
        StringBuilder sb = new StringBuilder();
 
        sb.append("Usuarios actualmente dentro de la biblioteca:").append(nl);
        for (User u : getCurrentInside()) {
            sb.append(u.getId()).append(nl);
        }
 
        sb.append(nl);
        sb.append("Número de entradas por usuario:").append(nl);
        for (User u : getUserList()) {
            sb.append(u.getId()).append(" -> ").append(u.getNEntries()).append(nl);
        }
 
        sb.append(nl);
        sb.append("Usuario(s) con más entradas:").append(nl);
        List<User> topUsers = getMaxEntryUsers();
        for (int i = 0; i < topUsers.size(); i++) {
            if (i == topUsers.size() - 1) {
                sb.append(topUsers.get(i).getId());
            } else {
                sb.append(topUsers.get(i).getId()).append(nl);
            }
        }
 
        System.out.print(sb.toString());
    }
}