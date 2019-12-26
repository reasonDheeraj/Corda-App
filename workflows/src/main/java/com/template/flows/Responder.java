package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.flows.Initiator;
import net.corda.core.flows.*;
import net.corda.core.node.StatesToRecord;

@InitiatedBy(Initiator.class)
public class Responder extends FlowLogic<Void> {
    private final FlowSession counterpartySession;

    public Responder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Receive the transaction and store all its states.
        // If we don't pass `ALL_VISIBLE`, only the states for which the node is one of the `participants` will be stored.
        subFlow(new ReceiveTransactionFlow(counterpartySession, true, StatesToRecord.ALL_VISIBLE));

        return null;
    }
}