package mate.academy.exception;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1234567L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
