package io.github.dheid.filelistener;

public class FileListeningException extends RuntimeException {

    public FileListeningException(String message) {
        super(message);
    }

    public FileListeningException(String message, Throwable cause) {
        super(message, cause);
    }
}
