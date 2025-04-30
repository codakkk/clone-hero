package com.codalab.platformer.ui;

import com.badlogic.androidgames.framework.Input;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.utils.Converter;

import java.util.ArrayList;
import java.util.List;

public class Layout extends Widget {
    private final List<Widget> allChildren = new ArrayList<>();


    public void updateChildren(List<Input.TouchEvent> events) {
        if(!enabled) {
            return;
        }

        for (int i = events.size()-1; i >= 0; --i) {
            Input.TouchEvent event = events.get(i);

            if (event.type == Input.TouchEvent.TOUCH_DOWN) {
                for (Widget w : allChildren) {
                    if (!w.isEnabled()) {
                        continue;
                    }

                    if(w instanceof IWidgetClickable) {
                        IWidgetClickable clickable = (IWidgetClickable) w;
                        // Is this really necessary? I think there's a better way to handle sx, sy
                        int sx = (int) Converter.toScreenX(Converter.toWorldX(event.x));
                        int sy = (int) Converter.toScreenX(Converter.toWorldY(event.y));
                        if (w.contains(sx, sy)) {
                            clickable.onClick();
                            // Event has been handled. Don't pass it further
                            events.remove(i);
                            break;
                        }
                    }
                }
            }
        }
    }


    @Override
    public void render(MyGraphics g) {
        if(!enabled) {
            return;
        }

        for (Widget w : allChildren) {
            w.render(g);
        }
    }

    public void addChild(Widget widget) {
        this.allChildren.add(widget);
    }
}
