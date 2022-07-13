package be.gerard.escape.controller;

import be.gerard.escape.model.Mission;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MissionController
 *
 * @author bartgerard
 * @version v0.0.1
 */
@RestController
@RequestMapping("missions")
public class MissionController {

    @GetMapping
    public Mission[] missions() {
        return Mission.values();
    }

}
