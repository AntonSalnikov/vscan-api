package com.dataslab.vscan.web

import com.dataslab.vscan.service.file.FileService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Profile
import org.springframework.restdocs.ManualRestDocumentation
import spock.lang.Specification


/**
 * @author antin.salnikov
 */
@TestConfiguration
@Profile("mvc-test")
@ComponentScan([
        "com.dataslab.vscan.web",
        "com.dataslab.vscan.config.misc",
        "com.dataslab.vscan.config.web"
])
class MvcTestConfig extends Specification {

    /**
     * MocMvc configuration
     */
    @Bean
    ManualRestDocumentation getManualRestDocumentation() {
        new ManualRestDocumentation("build/generated-snippets")
    }


    /**
     * Services
     */
    @Bean
    FileService getFileService() {
        Mock(FileService)
    }
}
