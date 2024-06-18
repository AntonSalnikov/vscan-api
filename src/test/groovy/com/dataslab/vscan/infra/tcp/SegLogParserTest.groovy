package com.dataslab.vscan.infra.tcp

import spock.lang.Specification

class SegLogParserTest extends Specification {

    def "should successfully parse with different ESAAMPVerdict"() {
        given:
        def payload = "<22>Jun 17 18:06:02 mriya.organic-invest-llc.com mail_cef_tcp: CEF:0|Cisco|C100V Secure Email Gateway Virtual|15.5.1-055|ESA_CONSOLIDATED_LOG_EVENT|Consolidated Log Event|5|deviceExternalId=564D15CFD52D6783CD9A-FCF6ABEB9F39 ESAMID=39 ESAICID=42 ESAAMPVerdict=$ESAAMPVerdict ESAAVVerdict=UNDEFINED ESACFVerdict=MATCH ESAAttachmentDetails={'aaca4544-92da-4870-9e11-019adba09fff': {'IA': {'Score': 0}, 'AMP': {'Verdict': 'FILE UNKNOWN', 'fileHash': 'c4e536294f58642c952b2fa5b3af6b703212d76e8a29643c781a99160df3e514'}, 'BodyScanner': {}}} msg='Scanning file. File id: aaca4544-92da-4870-9e11-019adba09fff'}"

        when:
        def result = SegLogParser.parse(payload)

        then:
        noExceptionThrown()
        result != null

        result.hash() == "c4e536294f58642c952b2fa5b3af6b703212d76e8a29643c781a99160df3e514"
        result.messageId() == UUID.fromString("aaca4544-92da-4870-9e11-019adba09fff")

        with(result.verdict()) {
            it.fileVerdict() == "FILE UNKNOWN"
            it.ESAAMPVerdict() == ESAAMPVerdict
            it.ESAAVVerdict() == "NO_VERDICT"
        }
        
        where:
        ESAAMPVerdict << ["NOT EVALUATED","CLEAN","FA_PENDING","UNKNOWN","SKIPPED","UNSCANNABLE","LOW_RISK","MALICIOUS"]
    }

    def "should successfully parse with different ESAAVVerdict"() {
        given:
        def payload = "<22>Jun 17 18:06:02 mriya.organic-invest-llc.com mail_cef_tcp: CEF:0|Cisco|C100V Secure Email Gateway Virtual|15.5.1-055|ESA_CONSOLIDATED_LOG_EVENT|Consolidated Log Event|5|deviceExternalId=564D15CFD52D6783CD9A-FCF6ABEB9F39 ESAMID=39 ESAICID=42 ESAAMPVerdict=UNDEFINED ESAAVVerdict=$ESAAVVerdict ESACFVerdict=MATCH ESAAttachmentDetails={'aaca4544-92da-4870-9e11-019adba09fff': {'IA': {'Score': 0}, 'AMP': {'Verdict': 'FILE UNKNOWN', 'fileHash': 'c4e536294f58642c952b2fa5b3af6b703212d76e8a29643c781a99160df3e514'}, 'BodyScanner': {}}} msg='Scanning file. File id: aaca4544-92da-4870-9e11-019adba09fff'}"

        when:
        def result = SegLogParser.parse(payload)

        then:
        noExceptionThrown()
        result != null

        result.hash() == "c4e536294f58642c952b2fa5b3af6b703212d76e8a29643c781a99160df3e514"
        result.messageId() == UUID.fromString("aaca4544-92da-4870-9e11-019adba09fff")

        with(result.verdict()) {
            it.fileVerdict() == "FILE UNKNOWN"
            it.ESAAMPVerdict() == "NO_VERDICT"
            it.ESAAVVerdict() == ESAAVVerdict
        }

        where:
        ESAAVVerdict << ["NOT EVALUATED","NEGATIVE","REPAIRED","ENCRYPTED","UNSCANNABLE","POSITIVE"]
    }
}
