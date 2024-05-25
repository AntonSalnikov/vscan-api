package com.dataslab.vscan.web

import com.dataslab.vscan.config.security.AuthenticationService
import com.dataslab.vscan.dto.ValidationStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration
import org.springframework.context.annotation.Import
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.snippet.Attributes
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@ContextConfiguration(classes = [MvcTestConfig])
@ActiveProfiles(value = "mvc-test")
@TestPropertySource(value = "classpath:application-mvc-test.yaml")
@Import([MockMvcAutoConfiguration, RestDocsAutoConfiguration])
class BaseControllerSpec extends Specification {

    @Autowired
    protected MockMvc mockMvc

    @Value("\${vscan-api.api-key}")
    protected String apiKey

    static UUID fileUploadResultId = UUID.fromString("65d634c3-d992-44cb-b991-132f2942d226")


    //-------------Documentation: Payload----------------

    def static documentSetup(String identifier, Snippet... snippets) {

        MockMvcRestDocumentation.document(identifier, Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()), snippets)
    }

    static def prepareFileResultFields(String prefix = "") {
        [
                fieldWithPath("id", JsonFieldType.STRING, prefix, "File upload result id"),
                fieldWithPath("validationStatus", JsonFieldType.STRING, prefix, "Current validation status",
                [], "Values: ${ValidationStatus.values().toList()}"),
                fieldWithPath("sha256Hash", JsonFieldType.STRING, prefix, "SHA256 hash value of the uploaded file")
        ]
    }

    //-------------Documentation: Path and URI variables----------------

    def static fieldWithPath(String pathValue, JsonFieldType jsonFieldType = JsonFieldType.STRING, String prefix = "",
                             String description = "", List<String> constraints = [], String values = " - ") {
        PayloadDocumentation.fieldWithPath("${prefix}${pathValue}")
                .type(jsonFieldType)
                .description(description)
                .attributes(Attributes.key("constraints").value(constraints))
                .attributes(Attributes.key("values").value(values))
    }


    //-------------Documentation: Headers----------------

    def static prepareApiKeyHeader() {
        HeaderDocumentation.headerWithName(AuthenticationService.AUTH_TOKEN_HEADER_NAME).description(
                "API key header name")
    }

}
