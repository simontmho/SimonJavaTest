package com.thisisnoble.javatest.processors;

import com.thisisnoble.javatest.Event;
import com.thisisnoble.javatest.Orchestrator;
import com.thisisnoble.javatest.events.MarginEvent;
import com.thisisnoble.javatest.events.ShippingEvent;
import com.thisisnoble.javatest.events.TradeEvent;

import static com.thisisnoble.javatest.util.TestIdGenerator.shipEventId;

public class ShippingProcessor extends AbstractProcessor {

    public ShippingProcessor(Orchestrator orchestrator) {
        super(orchestrator);
    }

    @Override
    public boolean interestedIn(Event event) {
        return event instanceof TradeEvent;
    }

    @Override
    protected Event processInternal(Event event) {
//    	System.out.println("** Shipping Processor receive events:\tId: " + event.getId());
        String parId = event.getId();
        if (event instanceof TradeEvent)
            // return new MarginEvent(shipEventId(parId), parId, calculateTradeShipping(event));
        	return new ShippingEvent(shipEventId(parId), parId, calculateTradeShipping(event));		// Simon: it seem a mistake for raising wrong event
        throw new IllegalArgumentException("unknown event for shipping " + event);
    }

    private double calculateTradeShipping(Event te) {
        return ((TradeEvent) te).getNotional() * 0.2;
    }
}
