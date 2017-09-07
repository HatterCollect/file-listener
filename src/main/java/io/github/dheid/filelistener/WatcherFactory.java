package io.github.dheid.filelistener;

import java.nio.file.Path;
import java.nio.file.WatchService;

public class WatcherFactory {

    public Watcher create(WatchService watchService, Path filePath) {
        return new Watcher(watchService, filePath);
    }

}
