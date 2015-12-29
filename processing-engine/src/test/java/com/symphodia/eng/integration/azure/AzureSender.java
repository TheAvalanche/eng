package com.symphodia.eng.integration.azure;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Random;

public class AzureSender {
    private Connection connection;
    private Session sendSession;
    private MessageProducer sender;
    private static Random randomGenerator = new Random();

    public AzureSender() throws Exception {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.amqp_1_0.jms.jndi.PropertiesFileInitialContextFactory");
        env.put(Context.PROVIDER_URL, Resources.getResource("servicebus.properties").toURI().toString());
        Context context = new InitialContext(env);

        ConnectionFactory cf = (ConnectionFactory) context.lookup("SBCF");
        Destination topic = (Destination) context.lookup("TOPIC");

        connection = cf.createConnection();
        sendSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        sender = sendSession.createProducer(topic);
    }

    public static void main(String[] args) throws Exception {
        AzureSender simpleSenderReceiver = new AzureSender();
        System.out.println("Press [enter] to send a message. Type 'exit' + [enter] to quit.");
        BufferedReader commandLine = new java.io.BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String s = commandLine.readLine();
            if (s.equalsIgnoreCase("exit")) {
                simpleSenderReceiver.close();
                System.exit(0);
            } else {
                simpleSenderReceiver.sendMessage();
            }
        }
    }

    private void sendMessage() throws JMSException, IOException {
        TextMessage message = sendSession.createTextMessage();
        long randomMessageID = randomGenerator.nextLong() >>> 1;
        message.setJMSMessageID("ID:" + randomMessageID);
        message.setText(Resources.toString(Resources.getResource("json/create-order.json"), Charsets.UTF_8));
        sender.send(message);
        System.out.println("Sent message with JMSMessageID = " + message.getJMSMessageID());
    }

    public void close() throws JMSException {
        connection.close();
    }

}
