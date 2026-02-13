package pio.daw;

public class User implements Localizable {

    private String id;
    private EventType lastEvent = null;
    private Boolean inside = false;
    private int entryCount = 0;

    public User(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    // Contador de entradas
    public int getEntryCount(){
        return this.entryCount;
    }

    // El usuario entra
    public void enter(){
        this.inside = true;
        this.lastEvent = EventType.ENTRY;
        this.entryCount++;
    }

    // El usuario sale
    public void exit(){
        this.inside = false;
        this.lastEvent = EventType.EXIT;
    }

    // Devuelve si está dentro
    public Boolean isInside(){
        return this.inside;
    }
}
