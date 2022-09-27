package com.dadapp.seniorproject;

import com.dadapp.seniorproject.event.Event;
import com.dadapp.seniorproject.user.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping
public class DadappController {

    private final static Logger log = LoggerFactory.getLogger(DadappController.class);

    @Autowired
    UserRepo userRepo;

    public DadappController() {
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("text", "DadApp - Welcome Home!");
        return "index";
    }

    @RequestMapping(value = "/changePageTitle", method = RequestMethod.POST)
    public String changeText(@RequestParam(name = "inputText") String text, Model model) {
        model.addAttribute("text", text);
        return "index";
    }


    @RequestMapping("/reviews")
    public String reviews(Model model) {
        model.addAttribute("text", "DadApp - Reviews");
        return "reviews";
    }

    @RequestMapping("/support")
    public String support(Model model) {
        model.addAttribute("text", "DadApp - Support We Can All Use");
        return "support";
    }


    @RequestMapping("/searchmap")
    public String searchMap() {
        return "searchmap";
    }

    @RequestMapping("/localresources")
    public String localSupport(Model model) {
        model.addAttribute("text", "DadApp - Resources Near You");
        return "localresources";
    }

    @RequestMapping("/events")
    public String events(Model model) {
        Event event = new Event();
        model.addAttribute("event", event);
        model.addAttribute("text", "DadApp - Events Page");
        return "events";
    }

}

