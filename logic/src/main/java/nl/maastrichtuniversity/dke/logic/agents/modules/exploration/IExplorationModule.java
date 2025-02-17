package nl.maastrichtuniversity.dke.logic.agents.modules.exploration;

import nl.maastrichtuniversity.dke.logic.agents.util.Direction;
import nl.maastrichtuniversity.dke.logic.agents.util.MoveAction;
import nl.maastrichtuniversity.dke.logic.scenario.util.Position;

public interface IExplorationModule {

    MoveAction explore(Position currentPosition, Direction currentDirection);

    boolean isDoneExploring();

}
