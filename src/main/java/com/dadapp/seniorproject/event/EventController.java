package com.dadapp.seniorproject.event;

import com.dadapp.seniorproject.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.Collections;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    UserRepo userRepo;


    @RequestMapping(value = "/addEvent", method = { RequestMethod.GET, RequestMethod.POST })
    public String addEvent(@ModelAttribute("eventForm") Event eventForm, Model model, Principal principal)
    {
        eventForm.setCreatedOn(eventForm.getTimestamp());
        eventForm.setUsername(principal.getName());
        eventService.addEvent(eventForm);
        return "redirect:/events";
    }

    @RequestMapping(value = "/receiveEvents", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> receiveEvents(@ModelAttribute Event event)
    {
        return eventService.getEvents();
    }

    @RequestMapping(value = "/receiveUpcomingEvents", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> receiveUpcomingEvents(@ModelAttribute Event event)
    {
        List<Event> allEvents = eventService.getEvents();

        LocalDate localDate = LocalDate.now();
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for(int i = 0; i < allEvents.size(); i++) {
            LocalDate inputDate = LocalDate.parse(allEvents.get(i).getEventDate(), dtf);
            if(inputDate.isBefore(localDate)){
                allEvents.remove(i);
            }
        }

        allEvents.sort(Comparator.comparing(Event::getEventDate));
        return allEvents;
    }



    @RequestMapping(value = "/searchEvents", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> searchEvents(@RequestParam(defaultValue = "") String searchString, String ageFilter)
    {
        List<Event> matchingEvents = eventService.getEventsSearch(searchString, ageFilter);
        matchingEvents.sort(Comparator.comparing(Event::getEventDate));
        return matchingEvents;
    }

}
