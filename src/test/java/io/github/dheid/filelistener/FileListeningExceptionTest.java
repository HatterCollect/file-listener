package io.github.dheid.filelistener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FileListeningExceptionTest {

    private static final String MESSAGE = "message";

    @Mock
    private Throwable cause;

    @Test
    public void passesMessageAndCauseToSuperClass() {
        FileListeningException exception = new FileListeningException("message", cause);
        assertThat(exception.getMessage(), is(MESSAGE));
        assertThat(exception.getCause(), is(cause));
    }

}