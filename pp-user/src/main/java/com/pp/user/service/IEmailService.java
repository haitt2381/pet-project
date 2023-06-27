package com.pp.user.service;

public interface IEmailService {
    void sendSimpleMessage(String to, String subject, String text);
}