package com.mygdx.game.views.fleet;

import com.badlogic.gdx.scenes.scene2d.Group;
import nl.maastrichtuniversity.dke.logic.agents.Guard;
import nl.maastrichtuniversity.dke.logic.agents.Intruder;
import nl.maastrichtuniversity.dke.logic.scenario.Scenario;

import java.util.List;

public class FleetView extends Group {

    private final List<Guard> guards;
    private final List<Intruder> intruders;

    public FleetView(Scenario scenario) {
        this.guards = scenario.getGuards();
        this.intruders = scenario.getIntruders();

        addAgents();
    }

    private void addAgents() {
        for (Guard guard : guards) {
            addActor(new AgentView(guard));
        }
        for (Intruder intruder : intruders) {
            addActor(new AgentView(intruder));
        }
    }

}
