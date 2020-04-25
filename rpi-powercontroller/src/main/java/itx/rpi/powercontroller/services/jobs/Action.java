package itx.rpi.powercontroller.services.jobs;

public interface Action {

    String getType();

    String getDescription();

    ExecutionStatus getStatus();

    void execute() throws Exception;

    void shutdown();

}
