package org.natc.app

import groovy.json.JsonSlurper
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureEmbeddedDatabase
@SpringJUnitConfig
@Sql("/db/migration/V1__Initial_Schema.sql")
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
            restClient = new BasicRestClient("http://localhost:${port}")
        }
    }

    class BasicRestClient {
        def baseUrl
        def encoder = Base64.getEncoder()

        BasicRestClient(def baseUrl) {
            this.baseUrl = baseUrl
        }

        def get(Map args) {
            def urlString = "${baseUrl}${args.path}"
            def parameterCount = 0

            if (args.query) {
                args.query.each { key, value ->
                    urlString += (parameterCount == 0) ? "?" : "&"
                    urlString += "${key}=${value}"
                }
            }

            def request = new URL(urlString).openConnection()
            def authString = new String(encoder.encode("${appUsername}:${appPassword}".getBytes()))

            request.setRequestProperty("Authorization", "Basic ${authString}")
            request.setRequestProperty("Content-Type", args.contentType)

            def responseCode = request.getResponseCode()
            def responseBody = request.getInputStream().getText()
            def responseJson = new JsonSlurper().parseText(responseBody)

            return [status: responseCode, data: responseJson]
        }
    }
}
