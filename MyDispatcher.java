/* Implement this class. */

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class MyDispatcher extends Dispatcher {

    int i;

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
        i = 0;
    }

    @Override
    synchronized public void addTask(Task task) {
        switch (algorithm) {
            case ROUND_ROBIN -> {
                hosts.get(i).addTask(task);
                i = (i + 1) % hosts.size();
            }
            case SHORTEST_QUEUE -> {
                PriorityQueue<Host> queue = new PriorityQueue<>(Comparator.comparing(Host::getQueueSize));
                queue.addAll(hosts);
                queue.poll().addTask(task);
            }
            case LEAST_WORK_LEFT -> {
                PriorityQueue<Host> queue = new PriorityQueue<>(Comparator.comparing(Host::getWorkLeft));
                queue.addAll(hosts);
                queue.poll().addTask(task);
            }
            case SIZE_INTERVAL_TASK_ASSIGNMENT -> {
                switch (task.getType()) {
                    case SHORT -> {
                        hosts.get(0).addTask(task);
                    }
                    case MEDIUM -> {
                        hosts.get(1).addTask(task);
                    }
                    case LONG -> {
                        hosts.get(2).addTask(task);
                    }
                    default -> {
                        break;
                    }
                }
            }
            default -> {
                break;
            }
        }
    }
}
