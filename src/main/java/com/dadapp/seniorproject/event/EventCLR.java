package com.dadapp.seniorproject.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Component("event")
public class EventCLR implements CommandLineRunner {

    private final static Logger log = LoggerFactory.getLogger(EventCLR.class);
    private final int randomNum = ThreadLocalRandom.current().nextInt(4, 10 + 1);

    @Autowired
    EventService eventService;

    @Bean
    CommandLineRunner eventCommandLineRunner(EventRepo eventRepo) {
        return args -> {
            Event event1 = new Event (
                    "ThatDuder",
                    "123 Stuff Lane, Pooopy, PANTS",
                    41011,
                    "Come to My Boys Birthday Party!",
                    "This little shit needs friends.",
                    "Birthday",
                    "2022-02-22",
                    "16:00",
                    "3-5"
            );



            Event event2 = new Event (
                    "ThatOtherDude",
                    "456 Things Lane, PeePee, PANTS",
                    41011,
                    "Open Call For Play Date",
                    "This little shit needs friends.",
                    "Playdate",
                    "2022-03-03",
                    "13:00",
                    "13-15"
            );

            eventRepo.saveAll(
                    List.of(event1, event2)
            );
        };
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
