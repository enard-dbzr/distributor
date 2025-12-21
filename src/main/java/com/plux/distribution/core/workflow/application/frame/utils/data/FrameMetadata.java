package com.plux.distribution.core.workflow.application.frame.utils.data;

import com.plux.distribution.core.workflow.application.serializer.JsonDataSerializer;

public class FrameMetadata {
    private int enterCount = 0;
    private int handleCount = 0;
    private int exitCount = 0;

    public int getEnterCount() {
        return enterCount;
    }

    public void incrementEnterCount() {
        enterCount++;
    }

    public int getHandleCount() {
        return handleCount;
    }

    public void incrementHandleCount() {
        handleCount++;
    }

    public int getExitCount() {
        return exitCount;
    }

    public void incrementExitCount() {
        exitCount++;
    }

    public static class FrameMetadataSerializer extends JsonDataSerializer<FrameMetadata> {

        public FrameMetadataSerializer() {
            super(FrameMetadata.class);
        }
    }
}
