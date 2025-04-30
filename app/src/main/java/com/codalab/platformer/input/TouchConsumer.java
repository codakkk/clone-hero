package com.codalab.platformer.input;

import com.badlogic.androidgames.framework.Input;
import com.codalab.platformer.entities.EntityFactory;
import com.codalab.platformer.entities.TextEntity;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.entities.player.PlayerEntity;
import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.utils.Converter;
import com.google.fpl.liquidfun.Body;

/**
 * Takes care of user interaction: pulls objects using a Mouse Joint.
 */
public class TouchConsumer {
    private final GameWorld gameWorld;

    public TouchConsumer(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void consumeTouchEvent(Input.TouchEvent event)
    {
        switch (event.type) {
            case Input.TouchEvent.TOUCH_DOWN:
                consumeTouchDown(event);
                break;
            case Input.TouchEvent.TOUCH_UP:
                consumeTouchUp(event);
                break;
            case Input.TouchEvent.TOUCH_DRAGGED:
                consumeTouchMove(event);
                break;
        }
    }

    private void consumeTouchDown(Input.TouchEvent event) {
        PlayerEntity player = gameWorld.player;

        if(player.shells == 0) {
            Body playerBody = player.getBody();
            Vector2 pos = new Vector2(playerBody.getPositionX(), playerBody.getPositionY() - 1);

            TextEntity entity = EntityFactory.createEntity(TextEntity.class, pos);
            entity.text = "NO AMMO";
        } else {
            player.startCharging();

            updateArrowDirection(event);
        }
    }

    private void consumeTouchUp(Input.TouchEvent event) {

        if(gameWorld.player.isCharging) {
            PlayerEntity player = gameWorld.player;


            Vector2 worldPos = Converter.toWorld(event.x, event.y);
            Vector2 playerPos = new Vector2(player.getBody().getPosition());
            Vector2 direction = worldPos.sub(playerPos).nor();

            // those two ways of calculating direction aren't user friendly
            // (speaking of user experience)
//            Vector2 direction = chargingStartPos.sub(event.x, event.y).nor();
//            Vector2 direction = new Vector2(event.x, event.y).sub(chargingStartPos).nor();

            player.shoot(direction);

            gameWorld.player.stopCharging();
        }

    }

    private void consumeTouchMove(Input.TouchEvent event) {
        if(gameWorld.player.isCharging) {
            updateArrowDirection(event);
        }
    }

    private void updateArrowDirection(Input.TouchEvent event) {
        PlayerEntity player = gameWorld.player;

        Vector2 worldMousePos = Converter.toWorld(event.x, event.y);

        player.mouseWorldPosition.set(worldMousePos);
    }
}
