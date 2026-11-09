package com.example.myspringbootprojectsession12.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HistoryLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger("HISTORY");

    private HistoryLogger() {
    }

    public static void log(String pattern, Object... arguments) {
        LOGGER.info(pattern, arguments);
    }
}
