package com.comp2042.logic;

import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;

/**
 * This class represents a movement event triggered either by the user or the system.
 *
 * <p>This class stores both the event type and the source of the event.</p>
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Creates a new {@code MoveEvent} with a specified type and source.
     *
     * @param eventType the type of event that occurred
     * @param eventSource who triggered the event
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }


    /**
     * Returns the type of movement event.
     *
     * @return the event type
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Returns the source who triggered the event.
     *
     * @return the event source
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
