package com.dataslab.vscan


import spock.lang.Specification

import java.time.Clock
import java.time.ZoneOffset

@IntegrationTest
class BaseIT extends Specification {

    def setupSpec() {
        Clock.system(ZoneOffset.UTC)
    }
}