package br.com.roanrobersson.schedulinghttprequests;

import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class SchedulingHttpRequestsApplication {

	private static final Logger log = LoggerFactory.getLogger(RequestTask.class);

	@Value("${shr.request.scheduler.rate}")
	private Long requestRate;

	@Autowired
	private TaskScheduler taskScheduler;

	@Autowired
	private RequestTask task;

	private ScheduledFuture<?> taskState;

	public static void main(String[] args) {
		SpringApplication.run(SchedulingHttpRequestsApplication.class, args);
		log.info("APPLICATION STARTED");
	}

	@Scheduled(cron = "${shr.start.scheduler.cron}")
	public void startTask() {
		log.info("TASK STARTED");
		taskState = taskScheduler.scheduleWithFixedDelay(task, requestRate);
	}

	@Scheduled(cron = "${shr.end.scheduler.cron}")
	public void endTask() {
		log.info("TASK FINISHED");
		taskState.cancel(false);
	}
}
