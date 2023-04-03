package program.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import program.behaviour.ReceiveMessageBehaviour;
import program.configuration.JadeAgent;
import program.model.CookersList;
import program.model.Menu;

import java.io.File;
import java.io.IOException;

@JadeAgent(number = 1)
public class FoodAgent  extends Agent {
    @Override
    protected void setup() {
        System.out.println("Hello from " + getAID().getName());

        // Register the book-selling service in the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("test-squad");
        sd.setName("JADE-test");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }


        addBehaviour(new ReceiveMessageBehaviour());
    }

    @Override
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        // Print out a dismissal message
        System.out.println("EquipmentAgent " + getAID().getName() + " terminating");
    }

    public void parser() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Menu menu = objectMapper.readValue(new File("./src/main/files/menu.txt"), Menu.class);
    }
}