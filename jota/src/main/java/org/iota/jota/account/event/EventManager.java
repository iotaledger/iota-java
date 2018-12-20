package org.iota.jota.account.event;

public interface EventManager {

    public void emit(Event event);

    public void registerListener(EventListener listener);
    
    public void unRegisterListener(EventListener listener);
}
