package com.comp2042.interfaces;

import com.comp2042.logic.DownData;
import com.comp2042.logic.MoveEvent;
import com.comp2042.view.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    ViewData onHoldEvent(MoveEvent event);

    void createNewGame();
}
