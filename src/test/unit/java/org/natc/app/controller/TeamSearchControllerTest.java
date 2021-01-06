package org.natc.app.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.natc.app.entity.request.TeamSearchRequest;
import org.natc.app.entity.response.ResponseEnvelope;
import org.natc.app.entity.response.ResponseStatus;
import org.natc.app.entity.response.TeamResponse;
import org.natc.app.service.search.NATCSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamSearchControllerTest {

    @Mock
    private NATCSearchService teamSearchService;

    @InjectMocks
    private TeamSearchController teamSearchController;

    @Nested
    class Search {

        @Test
        public void shouldReturnOKResponse() {
            final ResponseEntity<ResponseEnvelope<TeamResponse>> response = teamSearchController.search(null, null, null, null, null);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        public void shouldReturnEnvelopeWithNotFoundStatusWhenNoRecordsAreFound() {
            final ResponseEntity<ResponseEnvelope<TeamResponse>> response = teamSearchController.search(null, null, null, null, null);

            assertEquals(ResponseStatus.NOT_FOUND, response.getBody().getStatus());
        }

        @Test
        public void shouldCallTeamSearchService() {
            teamSearchController.search(null, null, null, null, null);

            verify(teamSearchService).fetchAll(any(TeamSearchRequest.class));
        }

        @Test
        public void shouldConstructRequestObjectForTeamSearchServiceUsingRequestParameters() {
            final Integer teamId = 123;
            final String year = "1999";
            final Integer conferenceId = 1;
            final Integer divisionId = 2;
            final Boolean allstarTeam = true;
            final ArgumentCaptor<TeamSearchRequest> captor = ArgumentCaptor.forClass(TeamSearchRequest.class);

            teamSearchController.search(teamId, year, conferenceId, divisionId, allstarTeam);

            verify(teamSearchService).fetchAll(captor.capture());

            final TeamSearchRequest request = captor.getValue();

            assertEquals(teamId, request.getTeamId());
            assertEquals(year, request.getYear());
            assertEquals(conferenceId, request.getConferenceId());
            assertEquals(divisionId, request.getDivisionId());
            assertEquals(allstarTeam, request.getAllstarTeam());
        }

        @Test
        public void shouldRespondWithEnvelopContainingTeamsReturnedBySearchService() {
            final List<TeamResponse> teamList = Collections.singletonList(new TeamResponse());

            when(teamSearchService.fetchAll(any(TeamSearchRequest.class))).thenReturn(teamList);

            final ResponseEntity<ResponseEnvelope<TeamResponse>> response = teamSearchController.search(null, null, null, null, null);

            assertEquals(teamList, response.getBody().getResources());
        }

        @Test
        public void shouldReturnEnvelopeWithSuccessStatusWhenRecordsAreFound() {
            final List<TeamResponse> teamList = Collections.singletonList(new TeamResponse());

            when(teamSearchService.fetchAll(any(TeamSearchRequest.class))).thenReturn(teamList);

            final ResponseEntity<ResponseEnvelope<TeamResponse>> response = teamSearchController.search(null, null, null, null, null);

            assertEquals(ResponseStatus.SUCCESS, response.getBody().getStatus());
        }
    }
}