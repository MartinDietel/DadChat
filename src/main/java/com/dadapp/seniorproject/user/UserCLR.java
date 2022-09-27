//package com.dadapp.seniorproject.user;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.concurrent.ThreadLocalRandom;
//
//import static java.time.Month.JANUARY;
//
//
//@Component("user")
//public class UserCLR implements CommandLineRunner {
//
//    private final static Logger log = LoggerFactory.getLogger(UserCLR.class);
//    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
//    private final int randomNum = ThreadLocalRandom.current().nextInt(4, 10 + 1);
//
//
//    @Autowired
//    UserService userService;
//
//    @Bean
//    CommandLineRunner userCommandLineRunner(UserRepo userRepo) {
//        return args -> {
//            User username = new User (
//                    userService.randomString(randomNum),
//                    "name@name.com",
//                    41011,
//                    "username",
//                    "password",
//                    "the",
//                    "guy",
//                    LocalDate.of(1988, JANUARY, 5),
//                    sdf1.format(timestamp)
//            );
//
//            User username2 = new User (
//                    userService.randomString(randomNum),
//                    "username2@name.com",
//                    48823,
//                    "user2",
//                    "password2",
//                    "name",
//                    "guy",
//                    LocalDate.of(1988, JANUARY, 5),
//                    sdf1.format(timestamp)
//            );
//
//            userRepo.saveAll(
//                    List.of(username, username2)
//            );
//        };
//    }
//
//    @Override
//    public void run(String... args) {
//        log.info("running user command line runner");
//    }
//}
