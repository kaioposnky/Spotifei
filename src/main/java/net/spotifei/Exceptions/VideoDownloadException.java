package net.spotifei.Exceptions;

public class VideoDownloadException extends RuntimeException {
    public VideoDownloadException(String message) {
        super(message);
    }
}
