package com.dataslab.vscan.web

import com.dataslab.vscan.dto.ValidationStatus
import com.dataslab.vscan.service.domain.FileUploadResult
import com.dataslab.vscan.service.file.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.ResultActions

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields

@WebMvcTest([FileUploadController])
class FileUploadControllerTest extends BaseControllerSpec {


    @Autowired
    private FileService fileService

    @Autowired
    private ContentPayload contentPayload

    def basePath = "/api/files"


    def "should successfully upload file"() {
        given:
        def file = new MockMultipartFile("file", "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes())

        def responsePayload = contentPayload.createFileUploadResponse()

        when:
        ResultActions result = this.mockMvc.perform(
                multipart(basePath + "/upload").file(file).contentType(MediaType.MULTIPART_FORM_DATA)
        )

        then:
        1 * fileService.uploadFile(_ as File, "hello.txt") >> new FileUploadResult(fileUploadResultId, ValidationStatus.PROCESSING, "sha256Hash")

        result
                .andExpect(status().isAccepted())
                .andExpect(content().json(responsePayload, true))
                .andDo(documentSetup("get-file-upload-result",
                        //requestHeaders(getAccessHeaderDocumentation()),
                        responseFields(
                                prepareFileResultFields("")
                        ))
                )
    }


    def "should get file upload result by id"() {
        given:
        def responsePayload = contentPayload.createFileUploadResponse()

        when:
        ResultActions result = this.mockMvc.perform(
                get(basePath + "/{fileUploadResultId}", fileUploadResultId)
        )

        then:
        1 * fileService.getById(fileUploadResultId) >> Optional.of(
                new FileUploadResult(fileUploadResultId, ValidationStatus.PROCESSING, "sha256Hash")
        )

        result
                .andExpect(status().isOk())
                .andExpect(content().json(responsePayload, true))
                .andDo(documentSetup("upload-file",
                        //requestHeaders(getAccessHeaderDocumentation()),
                        responseFields(
                                prepareFileResultFields("")
                        ))
                )
    }

}
