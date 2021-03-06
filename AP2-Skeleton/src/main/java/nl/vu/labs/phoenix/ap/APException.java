package nl.vu.labs.phoenix.ap;

class APException extends Exception{
    APException(String s){
        super(s);
    }
}

class AssignmentException extends APException {
    AssignmentException(String s) {
        super("Incorrect assignment: "+s+"\n");
    }
}

class IdentifierException extends APException {
    IdentifierException(String s) {
        super("Incorrect identifier: "+s+"\n");
    }
}

class ExpressionException extends APException {
    ExpressionException(String s) {
        super("Incorrect expression: "+s+"\n");
    }
}

class NumberException extends APException {
    NumberException(String s){
        super("Incorrect number: "+s+"\n");

    }
}

class NoSuchElementException extends APException {
    NoSuchElementException(String s){
        super("Memory doesn't contain: "+s+"\n");
    }
}

class SyntaxException extends APException {
    SyntaxException(String s){
        super("Invalid syntax: "+s+"\n");
    }
}

class FactorException extends APException {
    FactorException(String s){
        super("Factor exception: "+s+"\n");
    }
}

class ComplexFactorException extends APException {
    ComplexFactorException(String s){
        super("Complex factor exception: "+s+"\n");
    }
}