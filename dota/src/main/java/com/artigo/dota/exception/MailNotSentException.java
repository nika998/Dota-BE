package com.artigo.dota.exception;

public class MailNotSentException extends RuntimeException{

    public MailNotSentException(String message) {
        super(message);
    }
}
