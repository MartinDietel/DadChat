package com.dadapp.seniorproject.event;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Component
public class EventService implements IEventService {

    Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepo eventRepo;

    public EventService(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    @Override
    public List<com.dadapp.seniorproject.event.Event> getEvents() {
        return eventRepo.findAll();
    }

    public List<com.dadapp.seniorproject.event.Event> getEventsSearch(String searchString, String ageFilter) {

        //List<Event> allEvents = eventRepo.findAll();
//        List<Event> matchingEvents = new ArrayList<Event>();
//
//        allEvents.stream().filter(o -> o.getTitle().toLowerCase().contains(searchString.toLowerCase())).forEach(
//                o -> {
//                    matchingEvents.add(o);
//                }
//        );

        //(event.getTitle().toLowerCase().contains(searchString.toLowerCase()) && event.getAge() == 36)
        List<Event> matchingEvents = eventRepo.findAll().stream()
                .filter(event ->
                        (event.getTitle().toLowerCase().contains(searchString.toLowerCase()) && event.getChildrenAgeRange().toLowerCase().contains(ageFilter.toLowerCase()))
                                || (event.getDescription().toLowerCase().contains(searchString.toLowerCase()) && event.getChildrenAgeRange().toLowerCase().contains(ageFilter.toLowerCase()))
                                || (event.getCategory().toLowerCase().contains(searchString.toLowerCase()) && event.getChildrenAgeRange().toLowerCase().contains(ageFilter.toLowerCase()))
                                || (event.getAddress().toLowerCase().contains(searchString.toLowerCase()) && event.getChildrenAgeRange().toLowerCase().contains(ageFilter.toLowerCase()))
                ).collect(Collectors.toList());

        return matchingEvents;
    }

    public void addEvent(Event event) {
        eventRepo.save(event);

   }

}
