package nl.vu.labs.phoenix.ap;

//public class APException extends Exception {
//
//    private static final long serialVersionUID = 1L;
//
//    public APException (String s) {
//        super(s);
//    }
//}
class APException extends Exception{
    APException(String s){
        super(s);
    }
}

class StatementException extends APException {
    StatementException(String s) {
        super("Incorrect statement: "+s);
    }
}

class IdentifierException extends APException {
    IdentifierException(String s) {
        super("Incorrect identifier: "+s);
    }
}

class ExpressionException extends APException {
    ExpressionException(String s) {
        super("Incorrect expression: "+s);
    }
}

class NumberException extends APException {
    NumberException(String s){
        super("Incorrect number: "+s);

    }
}

class NoSuchElementException extends APException {
    NoSuchElementException(String s){
        super("Memory doesn't contain:"+s);
    }
}