package org.natc.natc

import org.natc.natc.entity.domain.GameType
import org.natc.natc.entity.domain.TeamOffenseSummary;
import org.natc.natc.repository.TeamOffenseSummaryRepository
import org.springframework.beans.factory.annotation.Autowired

public class TeamOffenseSummarySearchTest extends NATCFunctionalTest {

    @Autowired
    private TeamOffenseSummaryRepository repository

    def setup() {
        repository.deleteAll()
    }

    def 'team offense summary search endpoint returns team data'() {
        given: 'a team offense summary exists in the database'
        def teamOffenseSummary = TeamOffenseSummary.builder()
                .year("2016")
                .type(GameType.POSTSEASON.getValue())
                .teamId(12)
                .build()

        repository.save(teamOffenseSummary)

        when: 'a request is sent to the team offense summary search endpoint'
        def response = restClient.get(path: '/api/team-offense-summaries/search', contentType: 'application/json')

        then: 'the response should contain the team offense summary'
        with(response) {
            status == 200
            data.status == 'SUCCESS'
            data.resource == null
            data.errors == null
            data.resources.size == 1
            data.resources[0].year == '2016'
            data.resources[0].type == 'POSTSEASON'
            data.resources[0].teamId == 12
        }
    }
}
