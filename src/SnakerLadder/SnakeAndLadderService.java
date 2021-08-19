package SnakerLadder;

import java.util.*;

public class SnakeAndLadderService {
    private SnakeAndLadderBoard snakeAndLadderBoard;
    private int initialNumberOfPlayers;
    private Queue<Player>players;
    private boolean isGameCompleted;

    private int noOfDices; //Optional Rule 1
    private boolean shouldGameContinueTillLastPlayer; //Optional Rule 3
    private boolean shouldAllowMultipleDiceRollOnSix; //Optional Rule 4

    private static final int DEFAULT_BOARD_SIZE=100;
    private static final int DEFAULT_NO_OF_DICES=1;

    public SnakeAndLadderService(int boardSize){
        this.snakeAndLadderBoard = new SnakeAndLadderBoard(boardSize);
        this.players = new LinkedList<>();
        this.noOfDices = SnakeAndLadderService.DEFAULT_NO_OF_DICES;
    }

    public SnakeAndLadderService(){
        this(SnakeAndLadderService.DEFAULT_BOARD_SIZE);
    }

    public void setNoOfDices(int noOfDices) {
        this.noOfDices = noOfDices;
    }

    public void setShouldGameContinueTillLastPlayer(boolean shouldGameContinueTillLastPlayer) {
        this.shouldGameContinueTillLastPlayer = shouldGameContinueTillLastPlayer;
    }

    public void setShouldAllowMultipleDiceRollOnSix(boolean shouldAllowMultipleDiceRollOnSix) {
        this.shouldAllowMultipleDiceRollOnSix = shouldAllowMultipleDiceRollOnSix;
    }


    public void setPlayers(List<Player>players){
        this.players = new LinkedList<>();
        this.initialNumberOfPlayers = players.size();
        Map<String,Integer> playerPieces = new HashMap<>();
        for(Player player:players)
        {
            this.players.add(player);
            playerPieces.put(player.getId(),0);
        }
        snakeAndLadderBoard.setPlayerPieces(playerPieces);
    }

    public void setSnakes(List<Snake>snakes){
        snakeAndLadderBoard.setSnakes(snakes);
    }

    public void setLadders(List<Ladder>ladders){
        snakeAndLadderBoard.setLadders(ladders);
    }

    //Note this method is marked as private
    private int getNewPositionAfterGoingThroughSnakesAndLadders(int newPosition){
        int prevPosition;
        do{
            prevPosition=newPosition;

            for(Ladder ladder: snakeAndLadderBoard.getLadders()){
                if(ladder.getStart()==newPosition)
                    newPosition=ladder.getEnd();
            }

            for(Snake snake : snakeAndLadderBoard.getSnakes()){
                if(snake.getStart()==newPosition)
                    newPosition=snake.getEnd();
            }


        }while (prevPosition!=newPosition);
        return newPosition;
    }

    private void movePlayer(Player player,int positions){
        int oldPosition = snakeAndLadderBoard.getPlayerPieces().get(player.getId());
        int newPosition = oldPosition+positions;

        int boardSize = snakeAndLadderBoard.getSize();

        if(newPosition>boardSize){
            newPosition = oldPosition;
        }else{
            newPosition = getNewPositionAfterGoingThroughSnakesAndLadders(newPosition);
        }

        snakeAndLadderBoard.getPlayerPieces().put(player.getId(),newPosition);
        System.out.println(player.getName()+" rolled a "+positions+" and moved from "+oldPosition+" to "+newPosition);
    }

    private int getTotalValueAfterDiceRolls(){
        return DiceService.roll();
    }

    private boolean hasPlayerWon(Player player){

        int playerPosition = snakeAndLadderBoard.getPlayerPieces().get(player.getId());
        int winningPosition = snakeAndLadderBoard.getSize();
        return  winningPosition==playerPosition;
    }

    private boolean isGameCompleted(){

        int currentNumberOfPlayers = players.size();
        return currentNumberOfPlayers<initialNumberOfPlayers;
    }

    public void startGame(){
        while(!isGameCompleted()){
            int totalDiceValue = getTotalValueAfterDiceRolls();
            Player currentPlayer = players.poll();
            movePlayer(currentPlayer,totalDiceValue);
            if(hasPlayerWon(currentPlayer)){
                System.out.println(currentPlayer.getName()+" wins the Game");
                snakeAndLadderBoard.getPlayerPieces().remove(currentPlayer.getId());
            }else {
                players.add(currentPlayer);
            }

        }
    }


}
