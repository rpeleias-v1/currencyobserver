package com.rodrigopeleias.currencyobserver.main;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.rodrigopeleias.currencyobserver.job.ConversionCalculatorJob;

import static org.quartz.JobBuilder.*; 
import static org.quartz.SimpleScheduleBuilder.*; 
import static org.quartz.TriggerBuilder.*; 

public class CurrencyObserverMain {
	
	public static void main(String[] args) throws SchedulerException {
		
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.start();
		JobDetail job = newJob(ConversionCalculatorJob.class)
				.withIdentity("currencyConversionJob", "guiato")
				.build();
		Trigger trigger = newTrigger()
				.withIdentity("conversionTime", "guiato")
				.startNow()
				.withSchedule(simpleSchedule()
						.withIntervalInSeconds(5)
						.repeatForever())
				.build();		
		sched.scheduleJob(job, trigger);
	}

}
