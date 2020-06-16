package com.github.byakkili.bim.spring;

import com.github.byakkili.bim.core.BimServerBootstrap;
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

    private BimServerBootstrap bimServerBootstrap;

    @Override
    public synchronized void start() {
    }

    @Override
    public synchronized void stop() {
        if (isRunning()) {
            running = false;
            bimServerBootstrap.close();
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
