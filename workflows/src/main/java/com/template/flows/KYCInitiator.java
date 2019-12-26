package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.KYCContract;
import com.template.states.KYCState;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.util.Arrays;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@InitiatingFlow
@StartableByRPC
public class KYCInitiator extends FlowLogic<SignedTransaction> {
    private final String name;
    private final String dl;

    public KYCInitiator(String name, String dl) {
        this.name = name;
        this.dl = dl;
    }

    private final ProgressTracker progressTracker = new ProgressTracker();

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // We choose our transaction's notary (the notary prevents double-spends).
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        // We get a reference to our own identity.
        Party initiator = getOurIdentity();

        /* ============================================================================
         *         TODO 1 - Create our TokenState to represent on-ledger tokens!
         * ===========================================================================*/
        // We create our new TokenState.
        KYCState kycState = new KYCState(initiator, name, dl);
        KYCContract.Commands.Initiate command = new KYCContract.Commands.Initiate();

        /* ============================================================================
         *      TODO 3 - Build our token issuance transaction to update the ledger!
         * ===========================================================================*/
        // We build our transaction.
        TransactionBuilder transactionBuilder = new TransactionBuilder();
        transactionBuilder.setNotary(notary);
        transactionBuilder.addOutputState(kycState, KYCContract.ID);
        transactionBuilder.addCommand(command, initiator.getOwningKey());
        /* ============================================================================
         *          TODO 2 - Write our TokenContract to control token issuance!
         * ===========================================================================*/
        // We check our transaction is valid based on its contracts.
        transactionBuilder.verify(getServiceHub());

       // FlowSession session = initiateFlow(initiator);

        // We sign the transaction with our private key, making it immutable.
        SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(transactionBuilder);

        // The counterparty signs the transaction
        //SignedTransaction fullySignedTransaction = subFlow(new CollectSignaturesFlow(signedTransaction, singletonList(session)));

        // We get the transaction notarised and recorded automatically by the platform.
        return subFlow(new FinalityFlow(signedTransaction, emptyList()));
    }
}