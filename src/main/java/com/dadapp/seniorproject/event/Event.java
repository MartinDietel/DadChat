package com.dadapp.seniorproject.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@Getter
@Setter
public class Event {

    private final static Logger log = LoggerFactory.getLogger(Event.class);

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long eventId;

    private String address;

    private int zipcode;

    private String username;

    private String title;

    private String description;

    private String category;

    private String createdOn;

    private String eventDate;

    private String eventTime;

    private String childrenAgeRange;


    public String getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        createdOn = sdf2.format(timestamp);
        return createdOn;
    }

    public Event(String username, String address,
                 int zipcode, String title,
                 String description, String category,
                 String eventDate, String eventTime, String childrenAgeRange) {
        this.username = username;
        this.address = address;
        this.zipcode = zipcode;
        this.title = title;
        this.description = description;
        this.category = category;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
      //  this.createdOn = createdOn;
        this.createdOn = getTimestamp();
        this.childrenAgeRange = childrenAgeRange;
    }
}
