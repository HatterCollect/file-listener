package io.github.dheid.filelistener;

import java.nio.file.Path;

public class FileListenerFactory {

    public static FileListener create(Path filePath) {
        WatcherFactory watcherFactory = new WatcherFactory();
        return new FileListener(filePath, watcherFactory);
    }

}
