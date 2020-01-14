package juniter.core.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ResponseStatus;

import static juniter.core.exception.UCode.EXCEPTION;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DuniterException extends RuntimeException {

    @Getter
    private UCode code;

    public DuniterException(@NonNull UCode msg) {
        super(msg.getMessage());
        code = msg;
    }


    public DuniterException(@NonNull String msg) {
        super(msg);
        code = new UCode(EXCEPTION.getNumber(),msg);
    }

    public DuniterException(@NonNull Exception ex) {
        super(ex.getMessage());
        code = new UCode(EXCEPTION.getNumber(), ex.getMessage());
    }
}