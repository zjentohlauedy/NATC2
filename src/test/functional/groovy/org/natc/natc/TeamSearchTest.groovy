package org.natc.natc

import groovyx.net.http.RESTClient
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureEmbeddedDatabase
class TeamSearchTest extends Specification {

    @LocalServerPort
    def port;

    @Value('${spring.security.user.name}')
    def appUsername;

    @Value('${spring.security.user.password}')
    def appPassword;

    def 'health check'() {
        when: 'a request is sent to the health endpoint'
        def restClient = new RESTClient("http://localhost:${port}")
        restClient.auth.basic appUsername, appPassword
        def response = restClient.get(path: '/actuator/health', contentType: "application/json")

        then: 'the status should be UP'
        with(response) {
            status == 200
            data.status == "UP"
        }
    }
}
