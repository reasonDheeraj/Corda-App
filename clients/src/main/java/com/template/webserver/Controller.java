package com.template.webserver;

import com.template.flows.KYCInitiator;
import com.template.flows.TokenIssueFlowInitiator;
import com.template.states.KYCState;
import net.corda.core.crypto.SecureHash;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Set;


@RestController
@RequestMapping("/")
public class Controller {
    private final CordaRPCOps proxy;
    private final static Logger logger = LoggerFactory.getLogger(Controller.class);

    public Controller(NodeRPCConnection rpc) {
        this.proxy = rpc.proxy;
    }

    @GetMapping(value = "/getFlows", produces = "text/plain")
    private String getFlows() {
        System.out.println(proxy.registeredFlows().toString());
        return proxy.registeredFlows().toString();
    }
    @GetMapping(value = "/test", produces = "text/plain")
    private String test(@RequestParam String id) {
        System.out.println("Application is Up and Running :"+ id);
        return "Application is Up and Running."+ id ;
    }

    @GetMapping(value = "/startFlow", produces = "text/plain")
    private String startFlow() {
        Set<Party> owner = proxy.partiesFromName("PartyB", true);
        Party PartyA = (Party) owner.toArray()[0];
        int amount = 1237;
        proxy.startFlowDynamic(TokenIssueFlowInitiator.class, PartyA, amount);
        return "Flow Complete";
    }

    @GetMapping(value = "/getLedger", produces = "text/plain")
    private String getBlocks() {
        return proxy.vaultQuery(KYCState.class).toString();
    }

    @GetMapping(value = "/initiateKyc", produces = "application/json")
    private String initiateKyc(@RequestParam String name, @RequestParam String drivingLicense) {
        //Set<Party> owner = proxy.partiesFromName("PartyB", true);
      //  Party PartyA = (Party) owner.toArray()[0];
    //    int amount = 1237;
      //  KYCState inputState =  new KYCState(null, "Bob", "def321");
        System.out.println("Initiating flow with: name"+ name+ " & driving license: "+drivingLicense);
        proxy.startFlowDynamic(KYCInitiator.class, name, drivingLicense);
        return "";
    }

    @GetMapping(value = "/uploadDoc", produces = "text/plain")
    private String uploadFile() throws FileNotFoundException, FileAlreadyExistsException {
        //Set<Party> owner = proxy.partiesFromName("PartyB", true);
        //  Party PartyA = (Party) owner.toArray()[0];
        //    int amount = 1237;
        //  KYCState inputState =  new KYCState(null, "Bob", "def321");
        FileInputStream file = new FileInputStream("C:\\Users\\innovation\\Desktop\\CorDapp\\yo-cordapp-master\\yo-cordapp-master\\src\\main\\kotlin\\net\\corda\\yo\\Client.zip");
      //  proxy.startFlowDynamic(KYCInitiator.class, "Bob", "def456");
        proxy.uploadAttachment(file);
        return "Flow Complete";
    }

    @GetMapping(value = "/downloadFile", produces = "text/plain")
    private InputStreamResource downloadFile() throws FileNotFoundException, FileAlreadyExistsException {
        //Set<Party> owner = proxy.partiesFromName("PartyB", true);
        //  Party PartyA = (Party) owner.toArray()[0];
        //    int amount = 1237;
        //  KYCState inputState =  new KYCState(null, "Bob", "def321");
        InputStreamResource inputStream = new InputStreamResource(proxy.openAttachment(SecureHash.parse("7ADFFCA91BA36F4BD33DE838642704D074E68B5DBBC1A57CB36F5D94CA868C14")));

       // FileInputStream file = new FileInputStream("C:\\Users\\innovation\\Desktop\\CorDapp\\yo-cordapp-master\\yo-cordapp-master\\src\\main\\kotlin\\net\\corda\\yo\\Yo.zip");
        //  proxy.startFlowDynamic(KYCInitiator.class, "Bob", "def456");
        //proxy.uploadAttachment(file);
        return inputStream;
    }
}