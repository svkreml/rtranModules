package Other;

import java.util.concurrent.SynchronousQueue;

/**
 * Created by a.chebotareva on 28.03.2017.
 */
public class WorkExchange {
    private SynchronousQueue<StopType> q1 = new SynchronousQueue<>();
    private SynchronousQueue<R_machine> q2 = new SynchronousQueue<>();

    public R_machine sendWork(StopType stopType) throws InterruptedException {
        q1.put(stopType);
        return q2.take();
    }
    public StopType getWork() throws InterruptedException {
        return q1.take();
    }
    public void sendResult(R_machine r_machine) throws InterruptedException {
        q2.put(r_machine);
    }
}
