package be.kdg.sokoban.model;

import be.kdg.sokoban.model.Objects.FieldObject;
import be.kdg.sokoban.model.Objects.Player;

/**
 * @author Niels Van Reeth
 * @version 1.0 3/6/2017 11:40 AM
 */
public class MoveAction {
    public static final int ACTION_MOVE = 0, ACTION_PUSH = 1, ACTION_NULL = 2;
    private int direction;
    private Player player;
    private boolean wasGoal;
    private int actionType;
    private FieldObject nextObject;

    /**
     * Creates a moveAction
     * @param direction of the movement
     * @param player in current level
     * @param actionType moveActionType describing the type of event
     * @param wasGoal if player was on goal
     * @param nextObject object in front of the player
     */
    MoveAction(int direction, Player player, int actionType, boolean wasGoal, FieldObject nextObject) {
        this.direction = direction;
        this.player = player;
        this.actionType = actionType;
        this.nextObject = nextObject;
        this.wasGoal = wasGoal;
    }

    public int getDirection() {
        return direction;
    }

    public Player getPlayer() {
        return player;
    }

    @SuppressWarnings("unused")
    public boolean wasGoal() {
        return wasGoal;
    }

    public FieldObject getNextObject() {
        return nextObject;
    }

    public int getActionType() {
        return actionType;
    }
}
