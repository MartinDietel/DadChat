package com.dadapp.seniorproject.event;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dadapp.seniorproject.user.UserRepo;

@Controller
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    UserRepo userRepo;

    @RequestMapping(value = "/addEvent", method = { RequestMethod.GET, RequestMethod.POST })
    public String addEvent(@ModelAttribute("eventForm") Event eventForm, Model model, Principal principal) {
        eventForm.setCreatedOn(eventForm.getTimestamp());
        eventForm.setUsername(principal.getName());
        eventService.addEvent(eventForm);
        return "redirect:/events";
    }

    @RequestMapping(value = "/receiveEvents", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> receiveEvents(@ModelAttribute Event event) {
        return eventService.getEvents();
    }

    @RequestMapping(value = "/receiveUpcomingEvents", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> receiveUpcomingEvents() {
        LocalDate localDate = LocalDate.now();
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Event> upcomingEvents = eventService.getEvents().stream()
                .filter(event -> ((LocalDate.parse(event.getEventDate(), dtf).isAfter(localDate))
                        || (LocalDate.parse(event.getEventDate(), dtf).isEqual(localDate))))
                .collect(Collectors.toList());

        upcomingEvents.sort(Comparator.comparing(Event::getEventDate));
        return upcomingEvents;
    }

    @RequestMapping(value = "/searchEvents", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> searchEvents(@RequestParam(defaultValue = "") String searchString, String ageFilter) {
        List<Event> matchingEvents = eventService.getEventsSearch(searchString, ageFilter);
        matchingEvents.sort(Comparator.comparing(Event::getEventDate));
        return matchingEvents;
    }

}
