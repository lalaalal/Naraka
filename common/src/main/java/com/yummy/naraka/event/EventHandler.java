package com.yummy.naraka.event;

import com.yummy.naraka.invoker.MethodInvoker;

public abstract class EventHandler {
    public static void prepare() {
        MethodInvoker.of(EventHandler.class, "prepare")
                .withParameterTypes(PlatformEventAccess.class)
                .invoke(new PlatformEventAccess());
    }

    public static class PlatformEventAccess {
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
