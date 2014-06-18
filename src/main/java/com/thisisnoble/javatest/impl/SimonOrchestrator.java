package com.thisisnoble.javatest.impl;

import java.util.ArrayList;
import java.util.List;

import com.thisisnoble.javatest.Event;
import com.thisisnoble.javatest.Orchestrator;
import com.thisisnoble.javatest.Processor;
import com.thisisnoble.javatest.Publisher;
import com.thisisnoble.javatest.events.TradeEvent;
import com.thisisnoble.javatest.util.TestIdGenerator;

public class SimonOrchestrator implements Orchestrator {

	public SimonOrchestrator() {
		registeredProcessor = new ArrayList<Processor>();
	}

	private List<Processor> registeredProcessor=null;
	private Publisher mPublisher=null;
	private CompositeEvent compositeEvent=null;
	
	@Override
	public void register(Processor processor) {
		registeredProcessor.add(processor);
	}

	@Override
	public void receive(Event event) {
		System.out.println("SimonOrchestrator event received...\tId: " + event.getId());
				
		if (event.isRoot())
		{
			compositeEvent = new CompositeEvent(TestIdGenerator.tradeEventId(), event);	
		}
		else
		{
			// make sure only 1 thread can access the sahred composite event and update shared publisher last event
			synchronized (compositeEvent) {
				compositeEvent.addChild(event);		// not trade safe
				mPublisher.publish(compositeEvent);
			}
			
		}
		
		System.out.println("Start dispatching event...\tId: " + event.getId() + " ClassName: " + event.getClass().getName());
		for (Processor processor : registeredProcessor)
		{
			processor.process(event);
		}
	}

	@Override
	public void setup(Publisher publisher) {
		mPublisher = publisher;
	}

	
}
