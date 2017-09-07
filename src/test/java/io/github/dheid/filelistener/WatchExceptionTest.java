package io.github.dheid.filelistener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WatchExceptionTest {

    private static final String MESSAGE = "message";

    @Mock
    private Throwable cause;

    @Test
    public void delegatesMessageToSuper() throws Exception {

        WatchException watchException = new WatchException(MESSAGE);
        assertThat(watchException.getMessage(), is(MESSAGE));

    }

    @Test
    public void delegatesMessageAndCauseToSuper() throws Exception {

        WatchException watchException = new WatchException(MESSAGE, cause);
        assertThat(watchException.getMessage(), is(MESSAGE));
        assertThat(watchException.getCause(), is(cause));

    }

}