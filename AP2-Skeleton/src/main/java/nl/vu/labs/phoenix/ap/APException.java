package nl.vu.labs.phoenix.ap;

//public class APException extends Exception {
//
//    private static final long serialVersionUID = 1L;
//
//    public APException (String s) {
//        super(s);
//    }
//}
public class APException extends Exception{
    APException(String s){
        super(s);
    }
}

class StatementException extends APException {
    public StatementException(String s) {
        super("Incorrect statement: "+s);
    }
}

class IdentifierException extends APException {
    public IdentifierException(String s) {
        super("Incorrect identifier: "+s);
    }
}