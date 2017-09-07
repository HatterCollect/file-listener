package io.github.dheid.filelistener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FileListenerTest {

    @Mock
    private Runnable runnable;

    @Mock
    private WatcherFactory watcherFactory;

    @Mock
    private Watcher watcher;

    private Path filePath;

    private FileListener fileListener;

    @Before
    public void initFilePath() throws IOException {
        filePath = Files.createTempFile(FileListenerTest.class.getSimpleName(), ".txt");
        given(watcherFactory.create(any(WatchService.class), eq(filePath))).willReturn(watcher);
        fileListener = new FileListener(filePath, watcherFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfNullFilePath() {
        new FileListener(null, watcherFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfNullWatcherThreadFactory() {
        new FileListener(filePath, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfFilePathIsDirectory() {
        Path dirPath = filePath.getParent();
        new FileListener(dirPath, watcherFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsIfNullAdded() {
        fileListener.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addsRunnableToWatcherThread() {
        fileListener.add(runnable);
        verify(watcher).add(runnable);
    }

    @Test
    public void startsWatcherThread() throws Exception {

        whenStarts();

        thenStartsWatcherThread();

    }

    @Test(expected = FileListeningException.class)
    public void failsIfStartedTwice() throws Exception {

        givenWatcherIsRunning();

        whenStarts();

    }

    @Test(expected = FileListeningException.class)
    public void failsOnStopIfNotStarted() throws Exception {

        whenStops();

    }

    @Test
    public void stopsWatcherThread() throws Exception {

        givenWatcherIsRunning();

        whenStops();

        thenDisablesWatcherThread();

    }

    private void givenWatcherIsRunning() {
        given(watcher.isRunning()).willReturn(true);
    }

    private void whenStarts() {
        fileListener.start();
    }

    private void whenStops() {
        fileListener.stop();
    }

    private void thenDisablesWatcherThread() {
        verify(watcher).disable();
    }

    private void thenStartsWatcherThread() {
        verify(watcher).start();
    }

}
