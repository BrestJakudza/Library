package pl.atena.library.job;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.inject.Inject;

@Startup
@Singleton
public class LibraryJob {

	@Inject
	private Logger log;

	@Schedule(second = "*/10", minute = "*", hour = "*")
	public void execute(Timer timer) {
		log.info("===> Library Job ->  " + timer.getNextTimeout());
	}

	@PostConstruct
	public void init() {
		log.info("===> Library Job -> Start...");
	}
}
