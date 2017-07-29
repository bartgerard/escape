package be.gerard.escape.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * MissionResult
 *
 * @author bartgerard
 * @version v0.0.1
 */
@RequiredArgsConstructor(staticName = "of")
@Data
public class MissionResult {
    private final Mission mission;

    private MissionStatus status = MissionStatus.PENDING;

    private long bonus = 0L;

    private LocalDateTime start;
    private LocalDateTime end;

    public void start() {
        this.status = MissionStatus.STARTED;
        this.start = LocalDateTime.now();
    }

    public void end() {
        this.status = MissionStatus.FINISHED;
        this.end = LocalDateTime.now();
    }

    public boolean isTooSoon() {
        if (Objects.isNull(start)) {
            return false;
        }

        return LocalDateTime.now()
                            .minusMinutes(3)
                            .isBefore(start);
    }

    public String getMissionName() {
        return mission.getName();
    }

    public long getMissionLength() {
        return mission.getLength();
    }

    public int getMissionId() {
        return mission.ordinal() + 1;
    }

    public long getDuration() {
        if (Objects.isNull(end) || Objects.isNull(start)) {
            return 0L;
        }

        return ChronoUnit.MINUTES.between(start, end);
    }

    public long getScore() {
        if (status != MissionStatus.FINISHED) {
            return 0L;
        }
        return (15 - Math.abs(mission.getLength() - getDuration())) * 10L;
    }

}
