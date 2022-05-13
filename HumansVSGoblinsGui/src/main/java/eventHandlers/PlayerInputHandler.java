package eventHandlers;

import com.example.gamegui.Actor;
import com.example.gamegui.Goblin;
import com.example.gamegui.HVG;
import com.example.gamegui.Human;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import misc.Dice;

import java.util.Queue;


public class PlayerInputHandler implements EventHandler<KeyEvent>{
    private HVG game;
    private Dice dice;


    public PlayerInputHandler(HVG game){
        this.game = game;
        dice = new Dice();


    }

    private void handleMovement(KeyEvent key){
        if(!game.isEngagedInCombat() && !game.getPlayer().isDead()){// Prevents movement if game.getPlayer() is dead or in combat
            switch (key.getCode()){
                case UP:
                    System.out.println(game.getPlayer().moveActor("n",game.getGameWorld().getWorldMap()));
                    break;
                case DOWN:
                    System.out.println(game.getPlayer().moveActor("s",game.getGameWorld().getWorldMap()));
                    break;
                case LEFT:
                    System.out.println(game.getPlayer().moveActor("w",game.getGameWorld().getWorldMap()));
                    break;
                case RIGHT:
                    System.out.println(game.getPlayer().moveActor("e",game.getGameWorld().getWorldMap()));
                    break;
            }
        }
    }
    private void handleCombat(KeyEvent key){
        if(game.isEngagedInCombat() && !game.getPlayer().isDead()){// Locks input if game.getPlayer() not in combat
            switch (key.getCode()){
                case DIGIT1: // Attack
                    System.out.println(game.getPlayer().attack(game.getEngagedEnemy(), dice.rollD10()) + " on turn " + game.incrementTurns());
                    break;
                case DIGIT2: // Heal
                    System.out.println(game.getPlayer().heal(5) + " on turn " + game.incrementTurns());
                    break;
                case DIGIT3: // Leave
                    System.out.println("You chicken out and leave.");
                    game.endCombat();
                    break;
                default:
                    System.out.println("Invalid Input");
            }
        }
    }


    @Override
    public void handle(KeyEvent key) {
        handleMovement(key);

        if (!game.isEngagedInCombat()) {
            if(game.checkEnemyPosOverlap()){// If game.getPlayer() and enemy position overlap after movement
                System.out.println("Combat has started with " + game.getEngagedEnemy());
                System.out.println(game.initCombat(game.getDice().rollD20(), game.getDice().rollD20()));
                game.incrementTurns();

                System.out.println(game.getPlayer() +":"+ game.getPlayer().getHealthRatio());// Displays player health bar
                System.out.println(game.getEngagedEnemy() +":"+game.getEngagedEnemy().getHealthRatio());// Displays goblin health bar
                //game.combatLoop();
            }
        }else if(game.isEngagedInCombat()){

            if(game.getCombatTurnQueue().peek().isPlayable()){
                handleCombat(key);
            }else{
                System.out.println(game.goblinCombatTurn());
            }
            System.out.println(game.getPlayer() +":"+ game.getPlayer().getHealthRatio());// Displays player health bar
            System.out.println(game.getEngagedEnemy() +":"+game.getEngagedEnemy().getHealthRatio());// Displays goblin health bar


            game.getCombatTurnQueue().add(game.getCombatTurnQueue().remove());

            System.out.println(game.deathHandler(game.getPlayer()));// Ends combat and game if player has died
            System.out.println(game.deathHandler(game.getEngagedEnemy()));// Ends combat if goblin has died
        }





    }

}
