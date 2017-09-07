package io.github.dheid.filelistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;

public class FileListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileListener.class);

    private final Path filePath;
    private Watcher watcher;
    private WatchService watchService;

    public FileListener(Path filePath, WatcherFactory watcherFactory) {

        if (filePath == null) {
            throw new IllegalArgumentException("Given path must not be null");
        }

        if (watcherFactory == null) {
            throw new IllegalArgumentException("Watcher thread factory must not be null");
        }

        if (Files.isDirectory(filePath)) {
            throw new IllegalArgumentException("Given path must point to a file, not a directory");
        }

        this.filePath = filePath;

        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new FileListeningException("Could not create watch service", e);
        }

        watcher = watcherFactory.create(watchService, filePath);
    }

    public void add(Runnable runnable) {

        if (runnable == null) {
            throw new IllegalArgumentException("Runnable must not be null");
        }

        watcher.add(runnable);
    }

    public void start() {

        if (watcher.isRunning()) {
            throw new FileListeningException("File listener is already running");
        }

        Path directoryPath = filePath.getParent();

        try {
            directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            watcher.enable();
            watcher.start();
            LOGGER.debug("Started watching directory {}", directoryPath);
        } catch (IOException e) {
            String message = "Could not register watch service";
            LOGGER.error(message, e);
            throw new FileListeningException(message, e);
        }

    }

    public void stop() {

        if (!watcher.isRunning()) {
            throw new FileListeningException("File listener is not running");
        }

        watcher.disable();
        try {
            watchService.close();
        } catch (IOException e) {
            String message = "Could not close watch service";
            LOGGER.error(message, e);
            throw new FileListeningException(message, e);
        }

    }

}
