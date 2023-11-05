package me.pixlent;

import me.pixlent.phasemachine.Service;

public class CleanupService implements Service {
    @Override
    public void start(Context context) {
        context.completePhase();
    }
}
