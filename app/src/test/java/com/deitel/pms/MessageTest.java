package com.deitel.pms;

import static org.junit.Assert.*;

import com.deitel.pms.messaging.Message;
import org.junit.Test;

public class MessageTest {

    String messageBody = "this is a test message";
    String date = "11/04/2022";
    String id = "123456789";
    String recipient = "John Doe";

    @Test
    public void getRecipient() {
        Message message = new Message(messageBody, date, id, recipient);
        assertEquals(message.getRecipient(), recipient);
    }

    @Test
    public void setRecipient() {
        Message message = new Message(messageBody, date, id, recipient);
        message.setRecipient("Sammy Sams");
        assertEquals(message.getRecipient(), "Sammy Sams");
    }

    @Test
    public void getContent() {
        Message message = new Message(messageBody, date, id, recipient);
        assertEquals(message.getContent(), messageBody);
    }

    @Test
    public void setContent() {
        Message message = new Message(messageBody, date, id, recipient);
        message.setContent("new content");
        assertEquals(message.getContent(), "new content");
    }

    @Test
    public void getCreatedAt() {
        Message message = new Message(messageBody, date, id, recipient);
        assertEquals(message.getCreatedAt(), date);
    }

    @Test
    public void setCreatedAt() {
        Message message = new Message(messageBody, date, id, recipient);
        message.setCreatedAt("19/01/2001");
        assertEquals(message.getCreatedAt(), "19/01/2001");
    }

    @Test
    public void getSenderId() {
        Message message = new Message(messageBody, date, id, recipient);
        assertEquals(message.getSenderId(), id);
    }

    @Test
    public void setSenderId() {
        Message message = new Message(messageBody, date, id, recipient);
        message.setSenderId("Adam Twed");
        assertEquals(message.getSenderId(), "Adam Twed");
    }
}
