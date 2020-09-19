package UtilPackage;

public class Uno {
    public static class IllegalHandException extends Exception {
        public IllegalHandException(String err) {
            super(err);
        }
    }

    public static class IllegalPlayerTurn extends Exception {
        public IllegalPlayerTurn(String err) { super(err);}
    }

    /*
    public static class InvalidTurnAfterGameEnd extends Exception {
        public InvalidTurnAfterGameEnd(String err) { super(err); }
    }
    */
}
