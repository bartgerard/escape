package be.gerard.escape.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * Team
 *
 * @author bartgerard
 * @version v0.0.1
 */
@Data
@Builder(toBuilder = true)
public class Team {
    private TeamId teamId;
    private String name;
    private String password;

    private MissionResult currentMission;

    @Singular
    private List<MissionResult> results;

    public long getScore() {
        return results.stream()
                      .map(MissionResult::getScore)
                      .reduce(0L, Long::sum)
                +
                results.stream()
                       .map(MissionResult::getBonus)
                       .reduce(0L, Long::sum);
    }

    public long getNbMissions() {
        return results.stream()
                      .filter(result -> result.getStatus() == MissionStatus.FINISHED)
                      .count();
    }

    public long getDuration() {
        return results.stream()
                      .map(MissionResult::getDuration)
                      .reduce(0L, Long::sum);
    }

    public int getTotalNbMissions() {
        return results.size();
    }

    public long getTotalDuration() {
        return results.stream()
                      .map(MissionResult::getMission)
                      .map(Mission::getLength)
                      .reduce(0L, Long::sum);
    }

}
