package pl.atena.library.producers;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class LoggerProducer {

	@Produces
	public Logger produceLogger(InjectionPoint iPoint) {
		return Logger.getLogger(iPoint.getMember().getDeclaringClass().getName());
	}

}
