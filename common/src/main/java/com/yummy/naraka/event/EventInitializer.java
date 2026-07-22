package com.yummy.naraka.event;

import com.yummy.naraka.service.NarakaServices;

public interface EventInitializer {
    static void initialize() {
        NarakaServices.EVENT_INITIALIZER.initialize(new PlatformEventAccess());
    }

    void initialize(PlatformEventAccess events);

    class PlatformEventAccess {
        public <T> void setPlatformInvoker(Event<T> event, T invoker) {
            if (event instanceof Event.PlatformEvent<T> platformEvent)
                platformEvent.setPlatformInvoker(invoker);
        }

        public <T> T getNarakaInvoker(Event<T> event) {
            if (event instanceof Event.PlatformEvent<T> platformEvent)
                return platformEvent.getNarakaInvoker();
            return event.invoker();
        }
    }
}
