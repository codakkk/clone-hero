package com.codalab.platformer.ui.layouts;

import com.codalab.platformer.data.Constants;
import com.codalab.platformer.ui.IClickedCallback;
import com.codalab.platformer.ui.Layout;
import com.codalab.platformer.ui.RetryButton;

public class PlayingLayout extends Layout {

    public PlayingLayout(IClickedCallback onRetry) {
        RetryButton retryButton = new RetryButton();
        retryButton.setPosition(Constants.VIRTUAL_WIDTH - 16 - 4, Constants.VIRTUAL_HEIGHT - 16 - 4);
        retryButton.onClicked = onRetry;

        this.addChild(retryButton);
    }
}
