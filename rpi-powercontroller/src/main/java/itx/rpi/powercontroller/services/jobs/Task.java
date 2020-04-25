package itx.rpi.powercontroller.services.jobs;

import itx.rpi.powercontroller.dto.TaskId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

public class Task implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Task.class);

    private final TaskEventListener taskEventListener;

    private final TaskId id;
    private final String jobId;
    private final String jobName;
    private final Collection<Action> actions;
    private final Date submitted;

    private ExecutionStatus status;
    private boolean stopped;
    private Date started;
    private Long duration;

    public Task(TaskId id, String jobId, String jobName, Collection<Action> actions, Date submitted) {
        this.id = id;
        this.jobId = jobId;
        this.jobName = jobName;
        this.status = ExecutionStatus.WAITING;
        this.actions = actions;
        this.stopped = false;
        this.submitted = submitted;
        this.taskEventListener = (id1, state) -> LOG.info("onTaskStateChange id={} {}", id1.getId(), state);
    }

    public Task(TaskId id, String jobId, String jobName, Collection<Action> actions, Date submitted,
                TaskEventListener taskEventListener) {
        this.id = id;
        this.jobId = jobId;
        this.jobName = jobName;
        this.status = ExecutionStatus.WAITING;
        this.actions = actions;
        this.stopped = false;
        this.submitted = submitted;
        this.taskEventListener = taskEventListener;
        taskEventListener.onTaskStateChange(id, ExecutionStatus.WAITING);
    }

    public TaskId getId() {
        return id;
    }

    public String getJobId() {
        return jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public Collection<Action> getActions() {
        return actions;
    }

    public Date getStarted() {
        return started;
    }

    public Long getDuration() {
        return duration;
    }

    @Override
    public void run() {
        if (!ExecutionStatus.WAITING.equals(status)) {
            return;
        }
        try {
            this.stopped = false;
            this.started = new Date();
            setExecutionStatus(ExecutionStatus.IN_PROGRESS);
            for (Action action : actions) {
                action.execute();
                if (stopped) {
                    this.duration = new Date().getTime() - this.started.getTime();
                    setExecutionStatus(ExecutionStatus.ABORTED);
                    return;
                }
            }
            this.duration = new Date().getTime() - this.started.getTime();
            setExecutionStatus(ExecutionStatus.FINISHED);
        } catch (Exception e) {
            this.duration = new Date().getTime() - this.started.getTime();
            setExecutionStatus(ExecutionStatus.FAILED);
        }
    }

    public void shutdown() {
        if (ExecutionStatus.WAITING.equals(status)) {
            setExecutionStatus(ExecutionStatus.CANCELLED);
        }
        if (!isTerminalExecutionState()) {
            this.stopped = true;
            for (Action action : actions) {
                action.shutdown();
            }
        }
    }

    private void setExecutionStatus(ExecutionStatus executionStatus) {
        this.status = executionStatus;
        this.taskEventListener.onTaskStateChange(id, executionStatus);
    }

    private boolean isTerminalExecutionState() {
        return ExecutionStatus.ABORTED.equals(status) || ExecutionStatus.CANCELLED.equals(status) ||
                ExecutionStatus.FAILED.equals(status) ||  ExecutionStatus.FINISHED.equals(status);
    }

}
