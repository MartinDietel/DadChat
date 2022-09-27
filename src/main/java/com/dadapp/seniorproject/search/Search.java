package com.dadapp.seniorproject.search;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jdk.jfr.BooleanFlag;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Table(name = "search")
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@Getter
@Setter
public class Search {

    private final static Logger log = LoggerFactory.getLogger(Search.class);

    @Id
    @SequenceGenerator(
            name = "search_sequence",
            sequenceName = "search_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "search_sequence"
    )
    private Long searchId;

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "review_sequence"
    )
    private Long reviewId;

    private int zipcode;

    private String result;

    @Transient
    @BooleanFlag
    private boolean doctorCheck;

    @Transient
    @BooleanFlag
    private boolean lawyerCheck;

    @Transient
    @BooleanFlag
    private boolean daycareCheck;

    @Transient
    @BooleanFlag
    private boolean supportCheck;


    public Search(Long searchId, int zipcode, String result, Long reviewId, boolean doctorCheck, boolean lawyerCheck, boolean daycareCheck, boolean supportCheck) {
        this.searchId = searchId;
        this.zipcode = zipcode;
        this.result = result;
        this.reviewId = reviewId;
        this.doctorCheck = doctorCheck;
        this.lawyerCheck = lawyerCheck;
        this.daycareCheck = daycareCheck;
        this.supportCheck = supportCheck;
    }


    public Search(int zipcode, String result, boolean doctorCheck, boolean lawyerCheck, boolean daycareCheck, boolean supportCheck) {
        this.zipcode = zipcode;
        this.result = result;
        this.doctorCheck = doctorCheck;
        this.lawyerCheck = lawyerCheck;
        this.daycareCheck = daycareCheck;
        this.supportCheck = supportCheck;
    }

}
