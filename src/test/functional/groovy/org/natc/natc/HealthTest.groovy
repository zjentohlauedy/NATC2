package org.natc.natc

class HealthTest extends NATCFunctionalTest {

    def 'health check'() {
        when: 'a request is sent to the health endpoint'
        def response = restClient.get(path: '/actuator/health', contentType: 'application/json')

        then: 'the status should be UP'
        with(response) {
            status == 200
            data.status == "UP"
        }
    }
}
