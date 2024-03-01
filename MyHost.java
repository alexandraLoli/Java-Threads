/* Implement this class. */

import java.sql.SQLOutput;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class MyHost extends Host {

    Task currentTask = null;
    long sleepEndTime;
    PriorityBlockingQueue<Task> queueTaks = new PriorityBlockingQueue<Task>(1, Comparator.
            comparing(Task::getPriority).reversed().thenComparing(Task::getId));
    boolean shut = false;
    @Override
    public void run() {
        while(queueTaks.peek() == null && shut){
        }

        do {
            currentTask = queueTaks.poll();
            if (currentTask != null) {
                long start = System.currentTimeMillis();
                try {
                    start = System.currentTimeMillis();
                    sleepEndTime = System.currentTimeMillis() + currentTask.getLeft();
                    Thread.sleep(currentTask.getLeft());
                    currentTask.finish();
                } catch (InterruptedException e) {
                    long end = System.currentTimeMillis();
                    currentTask.setLeft(currentTask.getDuration() - (end - start));
                    queueTaks.add(currentTask);
                }
            }
        } while (!shut);
    }

    @Override
    public void addTask(Task task) {
        queueTaks.add(task);
        if(currentTask != null) {
            if (currentTask.isPreemptible() && currentTask.getPriority() < task.getPriority()) {
                this.interrupt();
            }
        }
    }

    @Override
    public int getQueueSize() {
        if (currentTask == null) {
            return queueTaks.size();
        } else {
            return queueTaks.size() + 1;
        }
    }

    @Override
    public long getWorkLeft() {
        long work = 0;
        for(Task task : queueTaks) {
            work += task.getLeft();
        }
        return getRemainingSleepTime() + work;
    }

    @Override
    public void shutdown() {
        shut = true;
    }

    public long getRemainingSleepTime() {
        return Math.max(0, sleepEndTime - System.currentTimeMillis());
    }
}
