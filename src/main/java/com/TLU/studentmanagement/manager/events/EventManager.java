// EventManager.java
package main.java.com.TLU.studentmanagement.manager.events;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private List<EventListener> listeners = new ArrayList<>();

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners(String eventType) {
        for (EventListener listener : listeners) {
            listener.onEvent(eventType);
        }
    }
}


