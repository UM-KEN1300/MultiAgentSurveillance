package nl.maastrichtuniversity.dke.agents;

import lombok.Getter;
import lombok.Setter;
import nl.maastrichtuniversity.dke.agents.modules.movement.IMovement;
import nl.maastrichtuniversity.dke.agents.modules.spawn.ISpawnModule;
import nl.maastrichtuniversity.dke.agents.modules.vision.IVisionModule;

public class Intruder extends Agent {
    private @Getter @Setter boolean alive;
    public Intruder(ISpawnModule spawnModule, IMovement movement, IVisionModule visionModule) {
        super(spawnModule,movement, visionModule);
        this.alive= true; //all the intruders are alive at start
    }

}
