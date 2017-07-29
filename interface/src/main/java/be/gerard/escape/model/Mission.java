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
    BLACK_MAMBA("Black Mamba", 15),
    SPYGLASS("Spyglass", 15),
    DIGITAL_FORTRESS("Digital Fortress", 15),
    MINOTAUR("Minotaur", 15);

    private final String name;
    private final long length;

}
