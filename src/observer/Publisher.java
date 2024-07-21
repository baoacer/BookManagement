package observer;

import java.util.ArrayList;
import java.util.List;

public class Publisher {

    private List<Subscriber> subscribers = new ArrayList<Subscriber>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void notifySubscribers() {
        for (Subscriber subscriber : subscribers) {
            subscriber.update();
        }
    }

}
