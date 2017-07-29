package be.gerard.escape.controller;

import be.gerard.escape.model.Mission;
import be.gerard.escape.model.MissionResult;
import be.gerard.escape.model.MissionStatus;
import be.gerard.escape.model.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    private final List<Team> teams = new ArrayList<>();

    @PostConstruct
    public void setUp() {
        this.teams.add(Team.builder()
                           .name("Alfa")
                           .password("a1a1")
                           .result(MissionResult.of(Mission.BLACK_MAMBA))
                           .result(MissionResult.of(Mission.SPYGLASS))
                           .result(MissionResult.of(Mission.DIGITAL_FORTRESS))
                           .result(MissionResult.of(Mission.MINOTAUR))
                           .build()
        );
        this.teams.add(Team.builder()
                           .name("Beta")
                           .password("r2d2")
                           .result(MissionResult.of(Mission.MINOTAUR))
                           .result(MissionResult.of(Mission.BLACK_MAMBA))
                           .result(MissionResult.of(Mission.SPYGLASS))
                           .result(MissionResult.of(Mission.DIGITAL_FORTRESS))
                           .build()
        );
        this.teams.add(Team.builder()
                           .name("Delta")
                           .password("0p3c")
                           .result(MissionResult.of(Mission.DIGITAL_FORTRESS))
                           .result(MissionResult.of(Mission.MINOTAUR))
                           .result(MissionResult.of(Mission.BLACK_MAMBA))
                           .result(MissionResult.of(Mission.SPYGLASS))
                           .build()
        );
        this.teams.add(Team.builder()
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
        return teams.stream()
                    .map(team -> team.builder()
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
        return teams;
    }

    @PostMapping("import")
    public void export(@RequestBody final List<Team> teams) {
        this.teams.clear();
        this.teams.addAll(teams);
    }

    @GetMapping("login")
    public MissionResult login(@RequestParam("password") final String password) {
        final Team team = this.teams.stream()
                                    .filter(hasPassword(password))
                                    .findFirst()
                                    .orElse(null);

        if (Objects.isNull(team)) {
            return null;
        }

        if (Objects.nonNull(team.getCurrentMission())) {
            if (team.getCurrentMission()
                    .isTooSoon()) {
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

}
