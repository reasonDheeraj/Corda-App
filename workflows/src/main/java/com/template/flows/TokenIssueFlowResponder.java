package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.ContractState;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

@InitiatedBy(TokenIssueFlowInitiator.class)
public class TokenIssueFlowResponder extends FlowLogic<Void> {

    private final FlowSession otherSide;

    public TokenIssueFlowResponder(FlowSession otherSide) {
        this.otherSide = otherSide;
    }

    @Override
    @Suspendable
    public Void call() throws FlowException {
        System.out.println("################################################################");
       String obj = otherSide.receive(String.class).unwrap(data -> data);
        System.out.println(obj);
        SignedTransaction signedTransaction = subFlow(new SignTransactionFlow(otherSide) {

            @Suspendable
            @Override
            protected void checkTransaction(SignedTransaction stx) throws FlowException {
                // Implement responder flow transaction checks here
              //  TokenState tokenState = new TokenState();
            //    stx.getTx().getOutputStates().add();
            }
        });
        subFlow(new ReceiveFinalityFlow(otherSide, signedTransaction.getId()));
        return null;
    }
}