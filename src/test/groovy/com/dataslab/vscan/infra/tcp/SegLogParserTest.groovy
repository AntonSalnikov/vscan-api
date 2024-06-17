package com.dataslab.vscan.infra.tcp

import spock.lang.Specification

class SegLogParserTest extends Specification {

    def payload = "<22>Jun 17 18:06:02 mriya.organic-invest-llc.com mail_cef_tcp: CEF:0|Cisco|C100V Secure Email Gateway Virtual|15.5.1-055|ESA_CONSOLIDATED_LOG_EVENT|Consolidated Log Event|5|deviceExternalId=564D15CFD52D6783CD9A-FCF6ABEB9F39 ESAMID=39 ESAICID=42 ESAAMPVerdict=UNKNOWN ESAAVVerdict=NEGATIVE ESACFVerdict=MATCH ESAAttachmentDetails={'aaca4544-92da-4870-9e11-019adba09fff': {'IA': {'Score': 0}, 'AMP': {'Verdict': 'FILE UNKNOWN', 'fileHash': 'c4e536294f58642c952b2fa5b3af6b703212d76e8a29643c781a99160df3e514'}, 'BodyScanner': {}}} msg='Scanning file. File id: aaca4544-92da-4870-9e11-019adba09fff'}"
    def "should successfully parse"() {
        when:
        def result = SegLogParser.parse(payload)

        then:
        noExceptionThrown()
        result != null

        result.hash() == "c4e536294f58642c952b2fa5b3af6b703212d76e8a29643c781a99160df3e514"
        result.verdict() == "FILE UNKNOWN"
        result.messageId() == UUID.fromString("aaca4544-92da-4870-9e11-019adba09fff")
    }
}
