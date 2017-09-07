package io.github.dheid.filelistener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.WatchService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WatcherFactoryTest {

    @Mock
    private WatchService watchService;

    @Mock
    private Path filePath;

    @Test
    public void createsWatcher() throws Exception {

        WatcherFactory watcherFactory = new WatcherFactory();
        Watcher watcher = watcherFactory.create(watchService, filePath);

        assertThat(watcher, is(notNullValue()));

    }
}