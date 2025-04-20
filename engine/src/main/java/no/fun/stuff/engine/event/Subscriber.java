package no.fun.stuff.engine.event;

public interface Subscriber {
    void update(String event, Object data);

}
