package pio.daw;

import java.util.List;

public interface Controlable {

    public void registerChange(String id, EventType e);

    public List<User> getCurrentInside();

    public List<User> getMaxEntryUsers();

    public List<User> getUserList();

    public void printResume();
}
