package com.template.contracts;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;
//import org.springframework.web.bind.annotation.RestController;


/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class KYCContract implements Contract {
    public static String ID = "com.template.contracts.KYCContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {

    }

    //public static class Issue implements CommandData {}

    public interface Commands extends CommandData {
        class Initiate implements Commands { }
        class Request implements Commands { }
    }

}