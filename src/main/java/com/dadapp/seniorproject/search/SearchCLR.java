package com.dadapp.seniorproject.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("search")
public class SearchCLR implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(SearchCLR.class);

    @Bean
    CommandLineRunner searchCommandLineRunner(SearchRepo searchRepo) {
        return args -> {
            Search search = new Search (
                    41011,
                    "Neato",
                    false,
                    true,
                    false,
                    false

            );

            Search search2 = new Search (
                    48823,
                    "Badass",
                    true,
                    false,
                    false,
                    false
            );

            searchRepo.saveAll(
                    List.of(search, search2)
            );
        };
    }

    @Override
    public void run(String... strings) throws Exception {
            log.info("running search command line runner");
    }
}
