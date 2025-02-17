package nl.maastrichtuniversity.dke.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Timer;

import nl.maastrichtuniversity.dke.logic.Game;
import nl.maastrichtuniversity.dke.logic.agents.Agent;
import nl.maastrichtuniversity.dke.logic.agents.modules.communication.CommunicationMark;
import nl.maastrichtuniversity.dke.logic.scenario.Smell;
import nl.maastrichtuniversity.dke.logic.scenario.Sound;
import nl.maastrichtuniversity.dke.logic.scenario.environment.Environment;
import nl.maastrichtuniversity.dke.logic.scenario.Scenario;
import nl.maastrichtuniversity.dke.logic.scenario.environment.Tile;
import nl.maastrichtuniversity.dke.logic.scenario.environment.TileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameComponent extends JComponent {

    private final Scenario scenario;
    private Environment environment;

    private int textureSize;
    private int panningX;
    private int panningY;
    private int agentMapNum;
    private boolean agentMapNumBoolean = false;


    private int frame = 0; //Current animation frame


    private static final Logger logger = LoggerFactory.getLogger(GameComponent.class);

    /**
     * Constructor for agent Memory map.
     */
    public GameComponent(Scenario scenario, Environment environment) {
        this.scenario = scenario;
        this.environment = environment;
        this.textureSize = (int) (scenario.getScaling() * 100) / 2;
    }

    /**
     * Constructor for regular Game Component.
     */
    public GameComponent() {
        this.scenario = Game.getInstance().getScenario();
        this.environment = scenario.getEnvironment();
        this.textureSize = (int) (scenario.getScaling() * 100);

        startGameSystem();
    }

    public void paintComponent(Graphics g) {
        drawEnvironment(g);
        drawMarks(g);
        drawSounds(g);
        drawGuards(g);
        drawIntruders(g);
        updateFrames();
    }

    private final ImageFactory imageFactory = ImageFactory.getInstance();

    private void drawEnvironment(Graphics g) {
        drawAreas(g, environment.get(TileType.WALL), imageFactory.get("wallTexture"));
        drawAreas(g, environment.get(TileType.TELEPORT), imageFactory.get("teleportTexture"));
        drawAreas(g, environment.get(TileType.SPAWN_GUARDS), imageFactory.get("spawnAreaTexture"));
        drawAreas(g, environment.get(TileType.SPAWN_INTRUDERS), imageFactory.get("spawnAreaTexture"));
        drawAreas(g, environment.get(TileType.WINDOW), imageFactory.get("windowTexture"));
        drawAreas(g, environment.get(TileType.DOOR), imageFactory.get("doorTexture"));
        drawAreas(g, environment.get(TileType.SENTRY), imageFactory.get("sentryTowerTexture"));
        drawAreas(g, environment.get(TileType.TARGET), imageFactory.get("targetTexture"));
        drawAreas(g, environment.get(TileType.SHADED), imageFactory.get("shadedTexture"));
        drawAreas(g, environment.get(TileType.UNKNOWN), imageFactory.get("unknownTexture"));
        drawAreas(g, environment.get(TileType.DESTINATION_TELEPORT), imageFactory.get("teleportDestination"));
    }

    private void drawGuards(Graphics g) {
        for (Agent agent : scenario.getGuards()) {
            drawAgent(g, agent);
        }
    }

    private void drawIntruders(Graphics g) {
        for (Agent agent : scenario.getIntruders()) {
            drawAgent(g, agent);
        }
    }

    private void drawAgent(Graphics g, Agent agent) {
        switch (agent.getDirection()) {
            case NORTH -> drawAgent(g, agent, "guardback");
            case SOUTH -> drawAgent(g, agent, "guard");
            case EAST -> drawAgent(g, agent, "guardright");
            case WEST -> drawAgent(g, agent, "guardleft");
            default -> logger.error("Unknown direction given for agent!");
        }
    }

    private void drawAgent(Graphics g, Agent agent, String textureName) {
        g.drawImage(
                getFramedAgentTexture(textureName),
                panningX + agent.getPosition().getX() * textureSize,
                panningY + agent.getPosition().getY() * textureSize,
                textureSize,
                textureSize,
                null
        );
    }

    private void drawAreas(Graphics g, List<Tile> tiles, BufferedImage image) {
        for (Tile tile : tiles) {
            drawArea(g, tile, image);
        }
    }

    private void drawArea(Graphics g, Tile tile, BufferedImage image) {
        g.drawImage(
                image,
                panningX + tile.getPosition().getX() * (textureSize),
                panningY + tile.getPosition().getY() * (textureSize),
                textureSize,
                textureSize,
                null
        );
    }

    private void drawSmells(Graphics g) {
        for (int i = 0; i < scenario.getSmellMap().size(); i++) {
            drawSmell(g, scenario.getSmellMap().get(i));
        }
    }

    private void drawSmell(Graphics g, Smell smell) {
        g.drawImage(
                imageFactory.get("smellTexture"),
                panningX + smell.getPosition().getX() * (textureSize),
                panningY + smell.getPosition().getY() * (textureSize),
                textureSize,
                textureSize,
                null
        );
    }

    private void drawSounds(Graphics g) {
        if (!agentMapNumBoolean) {
            for (int i = 0; i < scenario.getSoundMap().size(); i++) {
                Sound sound = scenario.getSoundMap().get(i);
                drawSound(g, sound);
            }
        }
    }

    private void drawSound(Graphics g, Sound sound) {
        g.drawImage(
                imageFactory.get("soundTexture"),
                panningX + sound.getPosition().getX() * (textureSize),
                panningY + sound.getPosition().getY() * (textureSize),
                textureSize,
                textureSize,
                null
        );
    }

    private void drawMarks(Graphics g) {
        for (CommunicationMark cm : scenario.getCommunicationMarks()) {
            drawMark(g, cm);
        }
    }

    private void drawMark(Graphics g, CommunicationMark cm) {
        g.setColor(cm.getColor());
        g.fillOval(
                panningX + (cm.getPosition().getX() * (textureSize)),
                panningY + (cm.getPosition().getY() * (textureSize)),
                textureSize,
                textureSize
        );
    }

    private BufferedImage getFramedAgentTexture(String name) {
        name += Integer.toString(frame + 1);
        return imageFactory.get(name);
    }

    private void updateFrames() {
        if (frame < 3) {
            frame++;
        } else {
            frame = 0;
        }
    }

    public void startGameSystem() {
        Game system = Game.getInstance();
        Timer timer = new Timer(0, e -> {
            system.update();
            repaint();
        });

        timer.start();
    }

    public void panning(int x, int y) {
        panningX += x / 15;
        panningY += y / 15;
    }

    public void resize() {
        textureSize = (int) (scenario.getScaling() * 100);
        panningX = 0;
        panningY = 0;
    }

    public void zoomIn() {
        textureSize = textureSize + 1;
    }

    public void zoomOut() {
        textureSize = textureSize - 1;
    }

    public void setEnvironment(Environment environment, int agentNum) {
        this.environment = environment;
        agentMapNum = agentNum;
        agentMapNumBoolean = true;
    }

    public void setUnionMap() {
        for (int i = 0; i < environment.getTileMap().length; i++) {
            for (int j = 0; j < environment.getTileMap()[0].length; j++) {
                if (environment.getTileMap()[i][j].getType() == TileType.UNKNOWN) {
                    for (Agent agent : scenario.getGuards()) {
                        Tile[][] map = agent.getMemoryModule().getMap().getTileMap();
                        if (!(map[i][j].getType() == TileType.UNKNOWN)) {
                            environment.getTileMap()[i][j] = map[i][j];
                        }
                    }
                }
            }
        }
        agentMapNumBoolean = false;
    }


}
