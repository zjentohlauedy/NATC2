package org.natc.app.service;

import org.natc.app.entity.domain.Manager;
import org.natc.app.entity.domain.Team;
import org.natc.app.exception.TeamNotFoundException;
import org.natc.app.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository repository;

    @Autowired
    public TeamService(final TeamRepository repository) {
        this.repository = repository;
    }

    public List<Team> generateTeams(final String year) {
        final List<Team> teamList = Arrays.asList(
                Team.builder().teamId(1).year(year).location("Indianapolis").name("Titans").abbreviation("IND.").timeZone("America/Indianapolis").gameTime(965).conference(0).division(0).allstarTeam(0).build(),
                Team.builder().teamId(2).year(year).location("Cincinnati").name("Whirlwind").abbreviation("CIN.").timeZone("America/New_York").gameTime(965).conference(0).division(0).allstarTeam(0).build(),
                Team.builder().teamId(3).year(year).location("Kansas City").name("Flames").abbreviation("K.C.").timeZone("America/Chicago").gameTime(965).conference(0).division(0).allstarTeam(0).build(),
                Team.builder().teamId(4).year(year).location("Dallas").name("Rustlers").abbreviation("DAL.").timeZone("America/Chicago").gameTime(965).conference(0).division(0).allstarTeam(0).build(),
                Team.builder().teamId(5).year(year).location("Washington").name("Olympians").abbreviation("WAS.").timeZone("America/New_York").gameTime(965).conference(0).division(0).allstarTeam(0).build(),
                Team.builder().teamId(6).year(year).location("Minneapolis").name("Marauders").abbreviation("MIN.").timeZone("America/Chicago").gameTime(965).conference(0).division(0).allstarTeam(0).build(),
                Team.builder().teamId(7).year(year).location("New Orleans").name("Tigersharks").abbreviation("N.O.").timeZone("America/Chicago").gameTime(965).conference(0).division(0).allstarTeam(0).build(),
                Team.builder().teamId(8).year(year).location("Oakland").name("Aces").abbreviation("OAK.").timeZone("America/Los_Angeles").gameTime(965).conference(0).division(0).allstarTeam(0).build(),
                Team.builder().teamId(9).year(year).location("Vancouver").name("Comets").abbreviation("VAN.").timeZone("America/Vancouver").gameTime(965).conference(0).division(0).allstarTeam(0).build(),
                Team.builder().teamId(10).year(year).location("Salt Lake City").name("Lightning").abbreviation("SLC.").timeZone("America/Phoenix").gameTime(965).conference(0).division(0).allstarTeam(0).build(),
                Team.builder().teamId(11).year(year).location("Boston").name("Blacks").abbreviation("BOS.").timeZone("America/New_York").gameTime(965).conference(0).division(1).allstarTeam(0).build(),
                Team.builder().teamId(12).year(year).location("Pittsburgh").name("Pirahnas").abbreviation("PIT.").timeZone("America/New_York").gameTime(965).conference(0).division(1).allstarTeam(0).build(),
                Team.builder().teamId(13).year(year).location("San Diego").name("Stingrays").abbreviation("S.D.").timeZone("America/Los_Angeles").gameTime(965).conference(0).division(1).allstarTeam(0).build(),
                Team.builder().teamId(14).year(year).location("Philadelphia").name("Photons").abbreviation("PHI.").timeZone("America/New_York").gameTime(965).conference(0).division(1).allstarTeam(0).build(),
                Team.builder().teamId(15).year(year).location("Detroit").name("Thunder").abbreviation("DET.").timeZone("America/New_York").gameTime(965).conference(0).division(1).allstarTeam(0).build(),
                Team.builder().teamId(16).year(year).location("Atlanta").name("Renegades").abbreviation("ATL.").timeZone("America/New_York").gameTime(965).conference(0).division(1).allstarTeam(0).build(),
                Team.builder().teamId(17).year(year).location("Baltimore").name("Crabbers").abbreviation("BAL.").timeZone("America/New_York").gameTime(965).conference(0).division(1).allstarTeam(0).build(),
                Team.builder().teamId(18).year(year).location("St. Louis").name("Juggernauts").abbreviation("S.L.").timeZone("America/Chicago").gameTime(965).conference(0).division(1).allstarTeam(0).build(),
                Team.builder().teamId(19).year(year).location("Orlando").name("Hurricanes").abbreviation("ORL.").timeZone("America/New_York").gameTime(965).conference(0).division(1).allstarTeam(0).build(),
                Team.builder().teamId(20).year(year).location("Las Vegas").name("Vampires").abbreviation("L.V.").timeZone("America/Los_Angeles").gameTime(965).conference(0).division(1).allstarTeam(0).build(),
                Team.builder().teamId(21).year(year).location("Miami").name("Voyagers").abbreviation("MIA.").timeZone("America/New_York").gameTime(965).conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(22).year(year).location("Houston").name("Hammers").abbreviation("HOU.").timeZone("America/Chicago").gameTime(965).conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(23).year(year).location("Los Angeles").name("Legends").abbreviation("L.A.").timeZone("America/Los_Angeles").gameTime(965).conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(24).year(year).location("New York").name("Knights").abbreviation("N.Y.").timeZone("America/New_York").gameTime(965).conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(25).year(year).location("Chicago").name("Goblins").abbreviation("CHI.").timeZone("America/Chicago").gameTime(965).conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(26).year(year).location("Tampa Bay").name("Terror").abbreviation("T.B.").timeZone("America/New_York").gameTime(965).conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(27).year(year).location("San Francisco").name("Tsunami").abbreviation("S.F.").timeZone("America/Los_Angeles").gameTime(965).conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(28).year(year).location("Montreal").name("Dragons").abbreviation("MON.").timeZone("America/New_York").gameTime(965).conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(29).year(year).location("New Jersey").name("Phantoms").abbreviation("N.J.").timeZone("America/New_York").gameTime(965).conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(30).year(year).location("Mexico City").name("Aztecs").abbreviation("MEX.").timeZone("America/Mexico_City").gameTime(965).conference(1).division(2).allstarTeam(0).build(),
                Team.builder().teamId(31).year(year).location("Buffalo").name("Icers").abbreviation("BUF.").timeZone("America/New_York").gameTime(965).conference(1).division(3).allstarTeam(0).build(),
                Team.builder().teamId(32).year(year).location("Cleveland").name("Scorpions").abbreviation("CLE.").timeZone("America/New_York").gameTime(965).conference(1).division(3).allstarTeam(0).build(),
                Team.builder().teamId(33).year(year).location("Denver").name("Nukes").abbreviation("DEN.").timeZone("America/Denver").gameTime(965).conference(1).division(3).allstarTeam(0).build(),
                Team.builder().teamId(34).year(year).location("Seattle").name("Psychotics").abbreviation("SEA.").timeZone("America/Los_Angeles").gameTime(965).conference(1).division(3).allstarTeam(0).build(),
                Team.builder().teamId(35).year(year).location("Phoenix").name("Eclipse").abbreviation("PHX.").timeZone("America/Phoenix").gameTime(965).conference(1).division(3).allstarTeam(0).build(),
                Team.builder().teamId(36).year(year).location("Milwaukee").name("Warriors").abbreviation("MIL.").timeZone("America/Chicago").gameTime(965).conference(1).division(3).allstarTeam(0).build(),
                Team.builder().teamId(37).year(year).location("Kingston").name("Outlaws").abbreviation("KIN.").timeZone("America/Jamaica").gameTime(965).conference(1).division(3).allstarTeam(0).build(),
                Team.builder().teamId(38).year(year).location("Toronto").name("Overlords").abbreviation("TOR.").timeZone("America/New_York").gameTime(965).conference(1).division(3).allstarTeam(0).build(),
                Team.builder().teamId(39).year(year).location("Charlotte").name("Serpents").abbreviation("CHA.").timeZone("America/New_York").gameTime(965).conference(1).division(3).allstarTeam(0).build(),
                Team.builder().teamId(40).year(year).location("Portland").name("Rhinos").abbreviation("POR.").timeZone("America/Los_Angeles").gameTime(965).conference(1).division(3).allstarTeam(0).build(),
                Team.builder().teamId(41).year(year).location("Greene").name("All Stars").abbreviation("GRN.").timeZone("America/New_York").gameTime(965).conference(0).division(0).allstarTeam(1).build(),
                Team.builder().teamId(42).year(year).location("Davis").name("All Stars").abbreviation("DVS.").timeZone("America/New_York").gameTime(965).conference(0).division(1).allstarTeam(1).build(),
                Team.builder().teamId(43).year(year).location("Smith").name("All Stars").abbreviation("SMI.").timeZone("America/New_York").gameTime(965).conference(1).division(2).allstarTeam(1).build(),
                Team.builder().teamId(44).year(year).location("Lawrence").name("All Stars").abbreviation("LAW.").timeZone("America/New_York").gameTime(965).conference(1).division(3).allstarTeam(1).build()
        );

        repository.saveAll(teamList);

        return teamList;
    }

    public void updateTeamsForNewSeason(final String previousYear, final String newYear) {
        repository.copyTeamsForNewYear(previousYear, newYear);
    }

    public Boolean willTeamReleaseManager(final Manager manager) {
        if (manager.getSeasons() < 3) return false;

        final String previousYear = decrementYear(manager.getYear());
        final String twoYearsBack = decrementYear(previousYear);

        final Optional<Team> previousYearTeamOpt = repository.findOne(Example.of(Team.builder().teamId(manager.getTeamId()).year(previousYear).build()));
        final Optional<Team> twoYearsBackTeamOpt = repository.findOne(Example.of(Team.builder().teamId(manager.getTeamId()).year(twoYearsBack).build()));

        if (previousYearTeamOpt.isEmpty()) return false;
        if (twoYearsBackTeamOpt.isEmpty()) return false;

        final Team previousYearTeam = previousYearTeamOpt.get();
        final Team twoYearsBackTeam = twoYearsBackTeamOpt.get();

        if (previousYearTeam.getPlayoffRank() > twoYearsBackTeam.getPlayoffRank()) return false;
        if (previousYearTeam.getWins() > twoYearsBackTeam.getWins()) return false;

        return (manager.getPerformanceRating() < previousYearTeam.getExpectation());
    }

    private String decrementYear(final String year) {
        return String.valueOf(Integer.parseInt(year) - 1);
    }

    public Team getTeamByTeamIdAndYear(final Integer teamId, final String year) throws TeamNotFoundException {
        return repository.findOne(Example.of(Team.builder().teamId(teamId).year(year).build()))
                .orElseThrow(TeamNotFoundException::new);
    }
}
