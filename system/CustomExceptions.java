package system;

public class CustomExceptions {
    public static class AdoptionException extends Exception {
        public AdoptionException(String message) {
            super(message);
        }
    }
    
    public static class CompatibilityException extends Exception {
        public CompatibilityException(String message) {
            super(message);
        }
    }
    
    public static class FosterCareException extends Exception {
        public FosterCareException(String message) {
            super(message);
        }
    }
    
    public static class FileOperationException extends Exception {
        public FileOperationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
