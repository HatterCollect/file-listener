package io.github.dheid.filelistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Watcher extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(Watcher.class);

    private final WatchService watchService;

    private final List<Runnable> runnables = Collections.synchronizedList(new ArrayList<>());

    private final Path filePath;

    private boolean enabled;

    private boolean running;

    public Watcher(WatchService watchService, Path filePath) {

        if (watchService == null) {
            throw new IllegalArgumentException("Watch service must not be null");
        }

        if (filePath == null) {
            throw new IllegalArgumentException("Path must not be null");
        }

        this.watchService = watchService;
        this.filePath = filePath;
    }

    public void add(Runnable runnable) {
        runnables.add(runnable);
    }

    @Override
    public void run() {
        running = true;
        while (enabled) {
            try {

                WatchKey watchKey = watchService.take();

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    Path changedPath = (Path) event.context();
                    LOGGER.debug("Checking if changed file {} is the candidate", changedPath);
                    if (changedPath.equals(filePath.getFileName())) {
                        synchronized (runnables) {
                            for (Runnable runnable : runnables) {
                                LOGGER.debug("Executing runnable on file {}", changedPath);
                                runnable.run();
                            }
                        }
                    }
                }

                watchKey.reset();

            } catch (InterruptedException e) {
                String message = "Interrupted while retrieving watch key";
                LOGGER.error(message, e);
                throw new WatchException(message, e);
            }

        }
        running = false;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isRunning() {
        return running;
    }

}
