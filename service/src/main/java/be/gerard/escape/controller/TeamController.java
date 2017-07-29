package be.gerard.escape.controller;

import be.gerard.escape.model.Mission;
import be.gerard.escape.model.MissionResult;
import be.gerard.escape.model.MissionStatus;
import be.gerard.escape.model.Team;
import be.gerard.escape.model.TeamId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * TeamController
 *
 * @author bartgerard
 * @version v0.0.1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("teams")
public class TeamController {

    private final MissionController missionController;

    private final Map<TeamId, Team> teams = new HashMap<>();

    @PostConstruct
    public void setUp() {
        this.teams.put(
                TeamId.ALFA,
                Team.builder()
                    .teamId(TeamId.ALFA)
                    .name("Alfa")
                    .password("a1a1")
                    .result(MissionResult.of(Mission.BLACK_MAMBA))
                    .result(MissionResult.of(Mission.SPYGLASS))
                    .result(MissionResult.of(Mission.DIGITAL_FORTRESS))
                    .result(MissionResult.of(Mission.MINOTAUR))
                    .build()
        );
        this.teams.put(
                TeamId.BRAVO,
                Team.builder()
                    .teamId(TeamId.BRAVO)
                    .name("Bravo")
                    .password("r2d2")
                    .result(MissionResult.of(Mission.MINOTAUR))
                    .result(MissionResult.of(Mission.BLACK_MAMBA))
                    .result(MissionResult.of(Mission.SPYGLASS))
                    .result(MissionResult.of(Mission.DIGITAL_FORTRESS))
                    .build()
        );
        this.teams.put(
                TeamId.DELTA,
                Team.builder()
                    .teamId(TeamId.DELTA)
                    .name("Delta")
                    .password("3333")
                    .result(MissionResult.of(Mission.DIGITAL_FORTRESS))
                    .result(MissionResult.of(Mission.MINOTAUR))
                    .result(MissionResult.of(Mission.BLACK_MAMBA))
                    .result(MissionResult.of(Mission.SPYGLASS))
                    .build()
        );
        this.teams.put(
                TeamId.ECHO,
                Team.builder()
                    .teamId(TeamId.ECHO)
                    .name("Echo")
                    .password("echo")
                    .result(MissionResult.of(Mission.SPYGLASS))
                    .result(MissionResult.of(Mission.DIGITAL_FORTRESS))
                    .result(MissionResult.of(Mission.MINOTAUR))
                    .result(MissionResult.of(Mission.BLACK_MAMBA))
                    .build()
        );
    }

    @GetMapping
    public List<Team> teams() {
        return teams.values()
                    .stream()
                    .map(team -> team.toBuilder()
                                     .clearResults()
                                     .results(team.getResults()
                                                  .stream()
                                                  .sorted(Comparator.comparing(x -> x.getMission()
                                                                                     .ordinal()))
                                                  .collect(Collectors.toList())
                                     )
                                     .build()
                    )
                    .sorted(Comparator.comparing(Team::getScore, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
    }

    @GetMapping("export")
    public List<Team> export() {
        return new ArrayList<>(teams.values());
    }

    @PostMapping("import")
    public void export(@RequestBody final List<Team> teams) {
        this.teams.clear();

        teams.forEach(team -> this.teams.put(team.getTeamId(), team));
    }

    @GetMapping("login")
    public MissionResult login(@RequestParam("password") final String password) {
        final Team team = this.teams.values()
                                    .stream()
                                    .filter(hasPassword(password))
                                    .findFirst()
                                    .orElse(null);

        if (Objects.isNull(team)) {
            return null;
        }

        if (Objects.nonNull(team.getCurrentMission())) {
            if (team.getCurrentMission()
                    .isWithinTimeout()) {
                return team.getCurrentMission();
            }

            team.getCurrentMission()
                .end();
        }

        final Optional<MissionResult> newMission = team.getResults()
                                                       .stream()
                                                       .filter(mission -> mission.getStatus() == MissionStatus.PENDING)
                                                       .findFirst();

        newMission.ifPresent(missionResult -> {
            team.setCurrentMission(missionResult);
            missionResult.start();
        });

        return newMission.orElse(null);
    }

    private Predicate<Team> hasPassword(
            final String password
    ) {
        return (team) -> Objects.equals(team.getPassword(), password);
    }

    @PutMapping("{teamId}/missions/{mission}")
    public void alterResults(
            @PathVariable("teamId") final TeamId teamId,
            @PathVariable("mission") final Mission mission,
            @RequestBody final MissionResult missionResult
    ) {
        this.teams.get(teamId)
                  .getResults()
                  .stream()
                  .filter(x -> x.getMission() == mission)
                  .findFirst()
                  .ifPresent(result -> {
                      result.setBonus(missionResult.getBonus());
                  });
    }

}
