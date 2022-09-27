//package com.dadapp.seniorproject.message.model;
//
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonSerialize
//@Data
//public class Message {
//
//    @Id
//    @Column(name = "id", nullable = false)
//    @GeneratedValue(
//            strategy = GenerationType.IDENTITY
//    )
//    private Long id;
//
//    @Column(name = "chat_id", nullable = false)
//    private String chatId;
//
//    @Column(name = "sender_id", nullable = false)
//    private String senderId;
//
//    @Column(name = "receiver_id", nullable = false)
//    private String receiverId;
//
//    @Column(name = "sender_name", nullable = false)
//    private String senderName;
//
//    @Column(name = "receiver_name", nullable = false)
//    private String receiverName;
//
//    @Column(name = "message")
//    private String message;
//
//    @Column(name = "files")
//    private String files;
//
//    @CreationTimestamp
//    @Column(name = "date", columnDefinition = "timestamp default current_timestamp")
//    private Date messageDate;

//    @Column(name = "seen")
//    private Boolean messageSeen;
//
//    @Column(name = "received")
//    private Boolean messageReceived;
//
//    @Column(name = "delete_sender")
//    private Boolean deleteSender;
//
//    @Column(name = "delete_receiver")
//    private Boolean deleteReceiver;
//
//    @Column(name = "type")
//    private String type;

//}
