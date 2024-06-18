package com.dataslab.vscan.web

import org.springframework.stereotype.Component

import com.dataslab.vscan.ResourceManager

@Component
class ContentPayload {

    private final ResourceManager rm = new ResourceManager(this.class)

    def createFileUploadResponse() {
        rm.load('content/file_upload_response.json')
    }

    def createFileUploadWitResultResponse() {
        rm.load('content/file_upload_with_result_response.json')
    }
}
