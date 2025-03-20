package no.fun.stuff.game.tool.ploygon.plotter;

import java.util.HashMap;
import java.util.Map;

interface Subscriber {
    void update(String event, Object data);
}
public class EventPublisher {
    private Map<String, Subscriber> subscriberList = new HashMap<>();
    public void publishEvent(String name, Object obj) {
        Subscriber subscriber = subscriberList.get(name);
        if(subscriber == null) {
            return;
        }
        subscriber.update(name, obj);
    }
    public void subscribe(String event, final Subscriber subscriber) {
        Subscriber subscriber1 = subscriberList.get(event);
        if(subscriber1 == null) {
            subscriberList.put(event, subscriber);
        }
    }
}
