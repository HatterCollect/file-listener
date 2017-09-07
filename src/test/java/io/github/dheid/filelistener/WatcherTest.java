package io.github.dheid.filelistener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WatcherTest {

    @Mock
    private WatchService watchService;

    @Mock
    private Path filePath;

    @Mock
    private Path fileName;

    @Mock
    private WatchKey watchKey;

    @Mock
    private WatchEvent<Path> watchEvent;

    @Mock
    private Runnable runnable;

    @InjectMocks
    private Watcher watcher;

    @Test
    public void isNotRunningInitially() {
        assertThat(watcher.isRunning(), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failIfNullWatchService() {
        new Watcher(null, filePath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failIfNullFilePath() {
        new Watcher(watchService, null);
    }

    @Test
    public void activatesRunnableIfFileNameMatches() throws Exception {

        givenWatchKey();
        givenWatchEvent();
        givenFileName();
        givenContext();
        watcher.add(runnable);

        watcher.enable();
        watcher.start();
        while (!watcher.isRunning()) {
        }
        watcher.disable();

        verify(watchService).take();
        verify(runnable).run();
        verify(watchKey).reset();

    }

    private void givenContext() {
        given(watchEvent.context()).willReturn(fileName);
    }

    private void givenFileName() {
        given(filePath.getFileName()).willReturn(fileName);
    }

    private void givenWatchEvent() {
        given(watchKey.pollEvents()).willReturn(Collections.singletonList(watchEvent));
    }

    private void givenWatchKey() throws InterruptedException {
        given(watchService.take()).willReturn(watchKey);
    }


}