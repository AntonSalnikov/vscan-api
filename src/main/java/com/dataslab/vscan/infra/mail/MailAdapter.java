package com.dataslab.vscan.infra.mail;

import com.dataslab.vscan.config.misc.CustomMailProperties;
import com.dataslab.vscan.service.file.MailPort;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailAdapter implements MailPort {

    private static final String MESSAGE_ID_HEADER_NAME = "Message-ID";

    private final JavaMailSender mailSender;
    private final CustomMailProperties mailProperties;

    @Override
    public void sendFile(@NonNull UUID messageId, @NonNull File attachment) {
        log.info("Sending message with id {} with attachment {}", messageId, attachment);

        try {
            var mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(mailProperties.getFrom());
            helper.setTo(mailProperties.getTo());
            helper.setSubject(mailProperties.getSubject().concat(messageId.toString()));
            helper.addAttachment(messageId.toString(), attachment);

            mimeMessage.setHeader(MESSAGE_ID_HEADER_NAME, "<%s>".formatted(messageId));
            mailSender.send(mimeMessage);

        } catch (MessagingException me) {
            log.error("Error appeared while sending email", me);
            throw new IllegalStateException("Email with message id %s was not sent".formatted(messageId));
        }

    }
}
