package com.symphodia.eng.integration.azure;

import com.google.common.io.Resources;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

public class AzureListenerOne implements MessageListener {

    public AzureListenerOne() throws Exception {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.amqp_1_0.jms.jndi.PropertiesFileInitialContextFactory");
        env.put(Context.PROVIDER_URL, Resources.getResource("servicebus.properties").toURI().toString());
        Context context = new InitialContext(env);

        ConnectionFactory cf = (ConnectionFactory) context.lookup("SBCF");
        Topic topic = (Topic) context.lookup("TOPIC");

        Connection connection = cf.createConnection();

        Session receiveSession = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        MessageConsumer receiver = receiveSession.createDurableSubscriber(topic, "engsub");
        receiver.setMessageListener(this);
        connection.start();
    }

    public static void main(String[] args) throws Exception {
            new AzureListenerOne();
    }

    public void onMessage(Message message) {
        try {
            System.out.println("Received message with JMSMessageID = " + message.getJMSMessageID());
            message.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
