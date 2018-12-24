package org.iota.jota.account.event.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.iota.jota.account.event.AccountEvent;
import org.iota.jota.account.event.Event;
import org.iota.jota.account.event.EventListener;
import org.iota.jota.account.event.EventManager;
import org.iota.jota.utils.Pair;

public class EventManagerImpl implements EventManager {
    
    Map<Class<? extends Event>, List<Pair<EventListener, Method>>> listeners;

    public EventManagerImpl() {
        listeners = new ConcurrentHashMap<>();
    }

    @Override
    public void emit(Event event) {
        List<Pair<EventListener, Method>> listeners = this.listeners.get(event.getClass());
        
        if (listeners == null || listeners.size() == 0) return;
        
        for (Pair<EventListener, Method> listener : listeners) {
            try {
                boolean accessible = listener.hi.isAccessible();
                if (!accessible) {
                    listener.hi.setAccessible(true);
                }
                
                listener.hi.invoke(listener.low, event);
                
                if (!accessible) {
                    listener.hi.setAccessible(false);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
                
                //Remove listener?
            }
        }
    }

    @Override
    public void registerListener(EventListener listener) {
       for (Method method : listener.getClass().getDeclaredMethods()) {
           if (method.isAnnotationPresent(AccountEvent.class)) {
               if (method.getParameterCount() != 1) {
                   //Invalid parameters assigned
                   continue;
               }
               
               Parameter param = method.getParameters()[0];
               if (!param.getType().isAssignableFrom(Event.class)) {
                   //Not an event parameter
                   continue;
               }
               
               List<Pair<EventListener, Method>> listeners = this.listeners.get(param.getClass());
               synchronized (listeners) {
                   if (listeners == null) {
                       listeners = Collections.synchronizedList(new ArrayList<>());
                       this.listeners.put((Class) param.getClass(), listeners);
                   }
                   
                   listeners.add(new Pair<EventListener, Method>(listener, method));
               }
           }
       }
    }

    @Override
    public void unRegisterListener(EventListener listener) {
        Set<Entry<Class<? extends Event>, List<Pair<EventListener, Method>>>> lists = listeners.entrySet();
        Iterator<Entry<Class<? extends Event>, List<Pair<EventListener, Method>>>> listIterator = lists.iterator();
        
        while (listIterator.hasNext()) {
            Entry<Class<? extends Event>, List<Pair<EventListener, Method>>> entry = listIterator.next();
            
            List<Pair<EventListener, Method>> pairs = entry.getValue();
            synchronized (pairs) {
                int size = pairs.size();
            
            
                Iterator<Pair<EventListener, Method>> pairIterator = pairs.iterator();
                
                while (pairIterator.hasNext()) {
                    Pair<EventListener, Method> pair = pairIterator.next();
                    if (pair.low.equals(listener)) {
                        pairIterator.remove();
                        size--;
                    }
                }
                
                if (size == 0) {
                    listIterator.remove();
                }
            }
        }
    }

}
