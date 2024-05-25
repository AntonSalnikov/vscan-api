package com.dataslab.vscan.service.file;

import lombok.NonNull;

import java.io.File;
import java.util.UUID;

public interface MailPort {


    void sendFile(@NonNull UUID messageId,
                  @NonNull File attachment);
}
