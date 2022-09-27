package com.dadapp.seniorproject.chat.repo;

import com.dadapp.seniorproject.chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<ChatMessage, Long> {


    @Query(value = "SELECT content, sender, recipient FROM ChatMessage WHERE ((sender = :sender AND recipient = :recipient) OR (sender = :recipient AND recipient = :sender)) order by id")
    List<ChatMessage> findAllByUsers(@Param("sender") String sender, @Param("recipient") String recipient);

    @Query("select c from ChatMessage c where c.sender = ?1 and c.channel = ?2")
    List<ChatMessage> findAllBySenderAndAndChannel(@Param("sender") String sender, @Param("channel") String channel);
}
