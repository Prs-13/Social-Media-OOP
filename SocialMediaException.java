public class SocialMediaException extends Exception {
    public SocialMediaException(String message) {
        super(message);
    }
}

class UserNotFoundException extends SocialMediaException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

class WrongEntryNumberException extends SocialMediaException {
    public WrongEntryNumberException(String message) {
        super(message);
    }
}

class AuthException extends SocialMediaException {
    public AuthException(String message) {
        super(message);
    }
}

class PermissionDeniedException extends SocialMediaException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}