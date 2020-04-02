package org.natc.app

import groovyx.net.http.RESTClient
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureEmbeddedDatabase
class NATCFunctionalTest extends Specification {

    @LocalServerPort
    def port

    @Value('${spring.security.user.name}')
    def appUsername

    @Value('${spring.security.user.password}')
    def appPassword

    def restClient

    def setup() {
        if (!restClient) {
            restClient = new RESTClient("http://localhost:${port}")
            restClient.auth.basic appUsername, appPassword
        }
    }
}
