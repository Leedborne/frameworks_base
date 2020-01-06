/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.notification.row;

import static com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.FLAG_CONTENT_VIEW_CONTRACTED;
import static com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.FLAG_CONTENT_VIEW_EXPANDED;
import static com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.FLAG_CONTENT_VIEW_HEADS_UP;

import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder.InflationFlag;

/**
 * Parameters for {@link RowContentBindStage}.
 */
final class RowContentBindParams {

    private boolean mUseLowPriority;
    private boolean mUseChildInGroup;
    private boolean mUseIncreasedHeight;
    private boolean mUseIncreasedHeadsUpHeight;
    private @InflationFlag int mContentViews;
    private boolean mViewsNeedReinflation;

    /**
     * Content views that are out of date and need to be rebound.
     *
     * TODO: This should go away once {@link NotificationContentInflater} is broken down into
     * smaller stages as then the stage itself would be invalidated.
     */
    private @InflationFlag int mDirtyContentViews;

    /**
     * Set whether content should use a low priority version of its content views.
     */
    public void setUseLowPriority(boolean useLowPriority) {
        if (mUseLowPriority != useLowPriority) {
            mDirtyContentViews |= (FLAG_CONTENT_VIEW_CONTRACTED | FLAG_CONTENT_VIEW_EXPANDED);
        }
        mUseLowPriority = useLowPriority;
    }

    public boolean useLowPriority() {
        return mUseLowPriority;
    }

    /**
     * Set whether content should use group child version of its content views.
     */
    public void setUseChildInGroup(boolean useChildInGroup) {
        if (mUseChildInGroup != useChildInGroup) {
            mDirtyContentViews |= (FLAG_CONTENT_VIEW_CONTRACTED | FLAG_CONTENT_VIEW_EXPANDED);
        }
        mUseChildInGroup = useChildInGroup;
    }

    public boolean useChildInGroup() {
        return mUseChildInGroup;
    }

    /**
     * Set whether content should use an increased height version of its contracted view.
     */
    public void setUseIncreasedHeight(boolean useIncreasedHeight) {
        if (mUseIncreasedHeight != useIncreasedHeight) {
            mDirtyContentViews |= FLAG_CONTENT_VIEW_CONTRACTED;
        }
        mUseIncreasedHeight = useIncreasedHeight;
    }

    public boolean useIncreasedHeight() {
        return mUseIncreasedHeight;
    }

    /**
     * Set whether content should use an increased height version of its heads up view.
     */
    public void setUseIncreasedHeadsUpHeight(boolean useIncreasedHeadsUpHeight) {
        if (mUseIncreasedHeadsUpHeight != useIncreasedHeadsUpHeight) {
            mDirtyContentViews |= FLAG_CONTENT_VIEW_HEADS_UP;
        }
        mUseIncreasedHeadsUpHeight = useIncreasedHeadsUpHeight;
    }

    public boolean useIncreasedHeadsUpHeight() {
        return mUseIncreasedHeadsUpHeight;
    }

    /**
     * Set whether the specified content views should be bound. See {@link InflationFlag}.
     */
    public void setShouldContentViewsBeBound(
            @InflationFlag int contentViews,
            boolean shouldBeBound) {
        if (shouldBeBound) {
            @InflationFlag int newContentViews = contentViews &= ~mContentViews;
            mContentViews |= contentViews;
            mDirtyContentViews |= newContentViews;
        } else {
            mContentViews &= ~contentViews;
            mDirtyContentViews &= ~contentViews;
        }
    }

    public @InflationFlag int getContentViews() {
        return mContentViews;
    }

    /**
     * Clears all dirty content views so that they no longer need to be rebound.
     */
    void clearDirtyContentViews() {
        mDirtyContentViews = 0;
    }

    public @InflationFlag int getDirtyContentViews() {
        return mDirtyContentViews;
    }

    /**
     * Set whether all content views need to be reinflated even if cached.
     *
     * TODO: This should probably be a more global config on {@link NotifBindPipeline} since this
     * generally corresponds to a Context/Configuration change that all stages should know about.
     */
    public void setNeedsReinflation(boolean needsReinflation) {
        mViewsNeedReinflation = needsReinflation;
        @InflationFlag int currentContentViews = mContentViews;
        mDirtyContentViews |= currentContentViews;
    }

    public boolean needsReinflation() {
        return mViewsNeedReinflation;
    }
}
