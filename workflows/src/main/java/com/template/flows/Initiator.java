package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.states.TokenState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.ProgressTracker;

import java.util.Collections;
import java.util.UUID;

@InitiatingFlow
@StartableByRPC
public class Initiator extends FlowLogic<Void> {
    private final String stateId;
    private final Party otherParty;

    private final ProgressTracker progressTracker = new ProgressTracker();

    public Initiator(String stateId, Party otherParty) {
        this.stateId = stateId;
        this.otherParty = otherParty;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Find the correct state.
//        QueryCriteria.LinearStateQueryCriteria criteria = new QueryCriteria.LinearStateQueryCriteria(null, Collections.singletonList(stateId));
//        Vault.Page<TokenState> queryResults = getServiceHub().getVaultService().queryBy(TokenState.class, criteria);
//        if (queryResults.getStates().size() != 1)
//            throw new IllegalStateException("Not exactly one match for the provided ID.");
//        StateAndRef<TokenState> stateAndRef = queryResults.getStates().get(0);

        // Find the transaction that created this state.
        SecureHash creatingTransactionHash = SecureHash.parse(stateId);//stateAndRef.getRef().getTxhash();
        SignedTransaction creatingTransaction = getServiceHub().getValidatedTransactions().getTransaction(creatingTransactionHash);

        // Send the transaction to the counterparty.
        final FlowSession counterpartySession = initiateFlow(otherParty);
        subFlow(new SendTransactionFlow(counterpartySession, creatingTransaction));

        return null;
    }
}