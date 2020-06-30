package com.github.byakkili.bim.autoconfigure;

import com.github.byakkili.bim.core.BimNettyServer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.Lifecycle;
import org.springframework.context.Phased;

/**
 * B-IM关闭的生命周期, 优先关闭
 *
 * @author Guannian Li
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BimCloseLifecycle implements Phased, Lifecycle {
    private volatile boolean running = true;

    private BimNettyServer bimNettyServer;

    @Override
    public synchronized void start() {
    }

    @Override
    public synchronized void stop() {
        if (isRunning()) {
            running = false;
            bimNettyServer.close();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
