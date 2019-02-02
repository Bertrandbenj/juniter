package juniter.core.exception;


public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = -6715624222174163366L;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
    
}
