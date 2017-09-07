package io.github.dheid.filelistener;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class FileListenerFactoryTest {

    private Path filePath;

    @Before
    public void initFilePath() throws IOException {
        filePath = Files.createTempFile(FileListenerTest.class.getSimpleName(), ".txt");
    }

    @Test
    public void createsFileListener() {
        FileListener fileListener = FileListenerFactory.create(filePath);
        assertThat(fileListener, is(CoreMatchers.notNullValue()));
    }

}