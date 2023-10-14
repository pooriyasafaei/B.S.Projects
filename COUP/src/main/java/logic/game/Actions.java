package logic.game;

public enum Actions {
    coup(false), murder(true), change(false),
    choose(false), take1(false), take2(true),
    take3(false), block(false), challenge(false),
    steal(true), skip(false);

    private final boolean blocking;

    Actions(boolean blocking){
        this.blocking = blocking;
    }

    public boolean isBlocking(){
        return blocking;
    }
}
