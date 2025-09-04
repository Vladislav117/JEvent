package ru.vladislav117.jevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Система событий.
 *
 * @param <BaseEventType> Базовый класс события.
 */
public class EventSystem<BaseEventType> {
    protected Map<Object, ArrayList<Consumer<? extends BaseEventType>>> eventHandlers = new HashMap<>();

    /**
     * Добавление обработчика события.
     *
     * @param eventType   Тип события
     * @param handler     Обработчик события
     * @param <EventType> Класс события
     * @return Эта же система событий.
     */
    @SuppressWarnings("UnusedReturnValue")
    public <EventType extends BaseEventType> EventSystem<BaseEventType> addHandler(Class<EventType> eventType, Consumer<EventType> handler) {
        if (!eventHandlers.containsKey(eventType)) eventHandlers.put(eventType, new ArrayList<>());
        eventHandlers.get(eventType).add(handler);
        return this;
    }

    /**
     * Обработка события.
     *
     * @param event       Событие
     * @param <EventType> Класс события
     * @return Эта же система событий.
     */
    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    public <EventType extends BaseEventType> EventSystem<BaseEventType> handle(EventType event) {
        Class<?> eventClass = getOriginalClass(event.getClass());
        if (!eventHandlers.containsKey(eventClass)) return this;
        eventHandlers.get(eventClass).forEach(handler -> ((Consumer<EventType>) handler).accept(event));
        return this;
    }

    /**
     * Получение оригинального класса.
     *
     * @param cls Класс
     * @return Оригинальный класс.
     */
    static Class<?> getOriginalClass(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return cls.getInterfaces().length == 0 ? cls.getSuperclass() : cls.getInterfaces()[0];
        } else {
            return cls;
        }
    }
}
