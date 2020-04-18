package itx.rpi.powercontroller.config.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import itx.rpi.powercontroller.config.ActionConfiguration;

import java.util.concurrent.TimeUnit;

public class ActionWaitConfig implements ActionConfiguration {

    private final Long delay;
    private final TimeUnit timeUnit;

    @JsonCreator
    public ActionWaitConfig(@JsonProperty("delay") Long delay,
                            @JsonProperty("timeUnit") String timeUnit) {
        this.delay = delay;
        this.timeUnit = TimeUnit.valueOf(timeUnit);
    }

    @Override
    @JsonIgnore
    public Class<? extends ActionConfiguration> getType() {
        return ActionWaitConfig.class;
    }

    public Long getDelay() {
        return delay;
    }

    public String getTimeUnit() {
        return timeUnit.name();
    }

    public TimeUnit getTimeUnitType() {
        return timeUnit;
    }

}
