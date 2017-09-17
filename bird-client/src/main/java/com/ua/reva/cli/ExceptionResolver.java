package com.ua.reva.cli;

import com.ua.reva.cli.exceptions.BirdCliException;
import com.ua.reva.cli.exceptions.UnexpectedBirdCliException;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.result.TerminalAwareResultHandler;
import org.springframework.stereotype.Component;

/**
 * Advanced CLI application resolver
 */
@Component
public class ExceptionResolver extends TerminalAwareResultHandler<BirdCliException> {

    @Override
    protected void doHandleResult(BirdCliException result) {

        if (result instanceof UnexpectedBirdCliException) {
            terminal.writer().println(new AttributedString(result.getMessage() + " " + result.getCause(),
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).toAnsi());
        } else {
            terminal.writer().println(new AttributedString(result.getMessage(),
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).toAnsi());
        }

    }
}
