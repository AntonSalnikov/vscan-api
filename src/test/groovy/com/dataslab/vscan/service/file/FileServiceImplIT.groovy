package com.dataslab.vscan.service.file

import com.dataslab.vscan.BaseIT
import com.dataslab.vscan.dto.ValidationStatus
import com.dataslab.vscan.exception.FileTypeValidationException
import com.dataslab.vscan.infra.dynamodb.FileScanResultEntity
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Autowired
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable

class FileServiceImplIT extends BaseIT {


    def originalFileName = "original_file_name.txt"

    @Autowired
    private FileService underTest

    @Autowired
    private DynamoDbTable<FileScanResultEntity> dbTable

    def cleanup() {
        dbTable.scan().items().forEach {
            dbTable.deleteItem(it)
        }
    }

    def "should upload file"() {
        given:
        def tempFile = createFile(fileName)

        when:
        def result = underTest.uploadFile(tempFile, originalFileName)

        then:
        result != null
        result.id() != null
        result.sha256Hash() != null
        result.validationStatus() == ValidationStatus.QUEUED

        and:"do not perform new file upload if file was already persisted"
        when:
        def sameResult = underTest.uploadFile(tempFile, "anotherFileName.tmp")

        then:
        sameResult == result

        where:
        fileName << ['test_docx.docx', 'test_xls.xlsx']
    }

    def "should throw FileTypeValidationException"() {
        given:
        def file = createFile("test_png.xlsx")

        when:
        underTest.uploadFile(file, originalFileName)

        then:
        thrown(FileTypeValidationException)
    }

    def "should retrieve upload file result by id"() {
        given:
        def tempFile = createFile('test_xls.xlsx')
        def created = underTest.uploadFile(tempFile, originalFileName)
        assert created.id() != null

        when:
        def result = underTest.getById(created.id())

        then:
        result.isPresent()
        result.get() == created
    }

    def "should return empty optional if file is not present"() {
        when:
        def result = underTest.getById(UUID.randomUUID())

        then:
        result.isEmpty()
    }

    def "should retrieve upload file result by hash"() {
        given:
        def tempFile = createFile('test_xls.xlsx')
        def created = underTest.uploadFile(tempFile, originalFileName)
        assert created.id() != null

        when:
        def result = underTest.getBySha256Hash(created.sha256Hash())

        then:
        result.isPresent()
        result.get() == created
    }

    def "should return empty optional if file is not present with such hash"() {
        when:
        def result = underTest.getBySha256Hash("some_hash")

        then:
        result.isEmpty()
    }

    def createFile(String fileName) {
        return FileUtils.toFile(this.class.getResource(fileName))
    }
}
