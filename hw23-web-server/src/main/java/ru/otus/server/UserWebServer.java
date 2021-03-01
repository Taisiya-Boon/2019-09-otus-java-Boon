package ru.otus.server;

public interface UserWebServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
