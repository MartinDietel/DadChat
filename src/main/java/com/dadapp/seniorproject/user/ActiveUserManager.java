package com.dadapp.seniorproject.user;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@Component
public class ActiveUserManager {
 
    private final Map<String, Object> map;
 
    private final List<ActiveUserChangeListener> listeners;
 
    private final ThreadPoolExecutor notifyPool;
 
    private ActiveUserManager() {
        map = new ConcurrentHashMap<>();
        listeners = new CopyOnWriteArrayList<>();
        notifyPool = new ThreadPoolExecutor(1, 5, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }
 
    public void add(String userName, String channel) {
        map.put(userName, channel);
        notifyListeners();
    }
 
    /**
     * Removes username from the concurrentHashMap.
     *
     * @param username - to be removed
     */
    public void remove(String username) {
        map.remove(username);
        notifyListeners();
    }
 
    /**
     * Get all active user
     *
     * @return - Set of active users.
     */
    public Set<String> getAll() {
        return map.keySet();
    }
 
    /**
     * Get a set of all active user except username that passed in the parameter
     *
     * @param username - current username
     * @return - set of users except passed username
     */
    public Set<String> getActiveUsersExceptCurrentUser(String username) {
        Set<String> users = new HashSet<>(map.keySet());
        users.remove(username);
        return users;
    }
    
    /**
     * To get notified when active users changed
     *
     * @param listener - object that implements ActiveUserChangeListener
     */
    public void registerListener(ActiveUserChangeListener listener) {
        listeners.add(listener);
    }
 
    /**
     * Stop receiving notification.
     *
     * @param listener - object that implements ActiveUserChangeListener
     */
    public void removeListener(ActiveUserChangeListener listener) {
        listeners.remove(listener);
    }
 
    private void notifyListeners() {
        notifyPool.submit(() -> listeners.forEach(ActiveUserChangeListener::notifyActiveUserChange));
    }
}