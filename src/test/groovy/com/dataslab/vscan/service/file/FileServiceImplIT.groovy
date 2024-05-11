package com.dataslab.vscan.service.file

import com.dataslab.vscan.BaseIT
import com.dataslab.vscan.dto.ValidationStatus
import org.springframework.beans.factory.annotation.Autowired

import java.nio.file.Files

class FileServiceImplIT extends BaseIT {

    @Autowired
    private FileService underTest

    def "should upload file"() {
        given:
        def tempFile = createFile()

        when:
        def result = underTest.uploadFile(tempFile)

        then:
        result != null
        result.id() != null
        result.sha256Hash() != null
        result.validationStatus() == ValidationStatus.QUEUED

        cleanup:
        tempFile.delete()
    }

    def "should retrieve upload file result by id"() {
        given:
        def tempFile = createFile()
        def created = underTest.uploadFile(tempFile)
        assert created.id() != null

        when:
        def result = underTest.getById(created.id())

        then:
        result.isPresent()
        result.get() == created

        cleanup:
        tempFile.delete()
    }

    def "should return empty optional if file is not present"() {
        when:
        def result = underTest.getById(UUID.randomUUID())

        then:
        result.isEmpty()
    }

    def "should retrieve upload file result by hash"() {
        given:
        def tempFile = createFile()
        def created = underTest.uploadFile(tempFile)
        assert created.id() != null

        when:
        def result = underTest.getBySha256Hash(created.sha256Hash())

        then:
        result.isPresent()
        result.get() == created

        cleanup:
        tempFile.delete()
    }

    def "should return empty optional if file is not present with such hash"() {
        when:
        def result = underTest.getBySha256Hash("some_hash")

        then:
        result.isEmpty()
    }

    def createFile() {
        def tempFile = Files.createTempFile(UUID.randomUUID().toString(), ".tmp").toFile()
        def ln = System.getProperty('line.separator')
        tempFile << "Line one of output example${ln}" +
                "Line two of output example${ln}Line three of output example"

        return tempFile
    }
}
