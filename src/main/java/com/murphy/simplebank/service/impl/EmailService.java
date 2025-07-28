package com.murphy.simplebank.service.impl;

import com.murphy.simplebank.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailAttachment(EmailDetails emailDetails);
}
