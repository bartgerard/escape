package be.gerard.escape.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Mission
 *
 * @author bartgerard
 * @version v0.0.1
 */
@RequiredArgsConstructor
@Getter
public enum Mission {
    BLACK_MAMBA("Black Mamba", 15, 10),
    SPYGLASS("Spyglass", 15, 10),
    DIGITAL_FORTRESS("Digital Fortress", 15, 10),
    MINOTAUR("Minotaur", 15, 10);

    private final String name;
    private final long length;
    private final long points;

}
