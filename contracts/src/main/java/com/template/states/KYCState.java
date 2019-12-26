package com.template.states;

import com.template.contracts.KYCContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/* Our state, defining a shared fact on the ledger.
 * See src/main/java/examples/ArtState.java for an example. */
@BelongsToContract(KYCContract.class)
public class KYCState implements ContractState{

    private String name;
    private String dl;
    private Party initiator;

    public String getName() {
        return name;
    }

    public String getDl() {
        return dl;
    }

    public KYCState(Party initiator, String name, String dl) {
        this.name = name;
        this.dl = dl;
        this.initiator = initiator;
    }

    public Party getInitiator() {
        return initiator;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        List<AbstractParty> participants = new ArrayList<>();
        participants.add(initiator);
        return participants;
    }
}