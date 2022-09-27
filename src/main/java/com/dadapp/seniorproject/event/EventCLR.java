package com.dadapp.seniorproject.event;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component("event")
public class EventCLR implements CommandLineRunner {

    private final static Logger log = LoggerFactory.getLogger(EventCLR.class);
    private final int randomNum = ThreadLocalRandom.current().nextInt(4, 10 + 1);

    @Autowired
    EventService eventService;

    @Bean
    CommandLineRunner eventCommandLineRunner(EventRepo eventRepo) {
        return args -> {
            Event event1 = new Event(
                    "dstober",
                    "2600 Clifton Ave, Cincinnati",
                    45220,
                    "Come to My Boys Birthday Party!",
                    "We are celebrating my boys 5th birthday party!!! There will be free cake, games, and fun! Please come make his day.",
                    "Birthday",
                    "2022-03-24",
                    "16:00",
                    "3-5");

            Event event2 = new Event(
                    "eacres",
                    "3251 Brookline Ave, Cincinnati, OH",
                    45220,
                    "Open Call For Play Date",
                    "I'm taking my 7 year old daughter to the park. We are going to do some walking and maybe some soccer if anyone would like to join.",
                    "Playdate",
                    "2022-03-24",
                    "13:00",
                    "6-9");

            eventRepo.saveAll(
                    List.of(event1, event2));
        };
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
