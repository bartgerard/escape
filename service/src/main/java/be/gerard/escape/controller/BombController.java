package be.gerard.escape.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MissionController
 *
 * @author bartgerard
 * @version v0.0.1
 */
@RestController
@RequestMapping("bombs")
public class BombController {

    private boolean activated;

    @GetMapping("activated")
    public boolean isActivated() {
        return this.activated;
    }

    @PutMapping("activate")
    public void activate() {
        this.activated = true;
    }

    @PutMapping("deactivate")
    public void deactivate() {
        this.activated = false;
    }

}
