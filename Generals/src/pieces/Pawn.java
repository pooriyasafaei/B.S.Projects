package pieces;

public class Pawn extends Piece{

    public Pawn(int row, int column, boolean playerBlack) {
        super(row, column, playerBlack);
    }

    @Override
    public boolean move(int i, int j) {

        if(!super.move(i, j)) return false;
        int current_i = this.getRow(), current_j = this.getColumn();

        if(!isUpgraded()){
            if (j != current_j) return false;
            if (!((this.isPlayerBlack() && i == current_i - 1) || (!this.isPlayerBlack() && i == current_i + 1)))
                return false;
        } else{
            if(i > current_i + 1 || i < current_i -1 || j > current_j + 1 || j < current_j - 1) return false;
            if((!isPlayerBlack()&&(i == current_i - 1 && (j == current_j + 1 || j == current_j - 1 )))
            ||(isPlayerBlack()&&(i == current_i + 1 && (j == current_j + 1 || j == current_j - 1))))return false;
        }

        return true;
    }

    @Override
    public String toString() {
        if(isPlayerBlack()) return "p";
        return "P";
    }
}
