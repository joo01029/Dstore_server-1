package gg.jominsubyungsin.lib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class Log {
	public void error(String errorMessage) {
		String str = "\n----------------------------------------------------------------------------------------------"+
						"\n[" + new Date().toString() + "]" + "  |  " + errorMessage +
						"\n----------------------------------------------------------------------------------------------";
		log.error(str);


	}

	public void info(String message) {
		String str = "\n----------------------------------------------------------------------------------------------"+
				"\n[" + new Date().toString() + "]" + "  |  " + message +
				"\n----------------------------------------------------------------------------------------------";
		log.info(str);
	}
}
