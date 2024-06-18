package com.dataslab.vscan.service.file

import com.dataslab.vscan.dto.ScanResult
import com.dataslab.vscan.service.domain.Verdict
import spock.lang.Specification

class ScanResultResolverTest extends Specification {

    def "should return OK"() {
        given:
        def verdict = new Verdict("NO_FILE", ESAAVVerdict, ESAAMPVerdict)

        expect:
        ScanResultResolver.resolve(verdict) == ScanResult.OK

        where:
        ESAAVVerdict    | ESAAMPVerdict
        "NEGATIVE"      | "NO_VERDICT"
        "NO_VERDICT"    | "CLEAN"
        "UNKNOWN"       | "LOW_RISK"
        "NOT EVALUATED" | "UNKNOWN"
    }

    def "should return FAILED"() {
        given:
        def verdict = new Verdict("NO_FILE", ESAAVVerdict, ESAAMPVerdict)

        expect:
        ScanResultResolver.resolve(verdict) == ScanResult.FAILED

        where:
        ESAAVVerdict    | ESAAMPVerdict
        "POSITIVE"      | "NO_VERDICT"
        "NO_VERDICT"    | "MALICIOUS"
        "POSITIVE"      | "MALICIOUS"
    }
}
