package pio.daw;

public class User implements Localizable {
    private String id;
    private EventType lasEvent = null;
    private Boolean inside = false;

    public User(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    //TODO
    public Boolean isInside(){
        return null
    }
}
