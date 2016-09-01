package com.stampery;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.Future;
import org.msgpack.type.Value;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Stampery {

	private List<Consumer> consumers = new ArrayList<Consumer>();
	private String secret;
	private String clientId;
	private String[] apiEndPoint;
	private String[] amqpEndPoint;
	private Client apiClient;
	private boolean auth;
	private Channel channel;

	public Stampery(String secret) {
		this(secret, "prod");
	}
	
	public Stampery(String secret, String branch) {
		this.secret = secret;
		clientId = getMD5(secret).substring(0, 15);
		setEndPoints(branch);
		
	}
	
	/**
	 * Start
	 */
	public void start() {
		apiLogin();
		amqpLogin();
	}

	/**
	 * Function used to receive the proofs
	 * @param consumer Object that implements the interface Consumer
	 */
	public void subscribe(Consumer consumer) {
		consumers.add(consumer);
	}

	/**
	 * Function to hash a string using SHA3 512
	 * @param data string 
	 * @return 
	 */
	public String hash(String data) {
		final DigestSHA3 sha3 = new DigestSHA3(512);

        sha3.update(data.getBytes());

        return digestToString(sha3.digest()).toUpperCase();
	}
	
	/**
	 * Stamp 
	 * @param data String with the data to be Stamped
	 */
	public void stamp(String data) {
		System.out.println("\nStamping \n" + data);
		try{
			apiClient.callApply("stamp", new Object[]{data.toUpperCase()});
		} catch (Exception e) {
			// Message pack returns 0 even if everything is ok
			// only report error if exception doesn't end with 0
			if(!e.getMessage().endsWith("0"))
				emitError(e.getMessage());
		}
	}    
	
	private void apiLogin(){
		try {
			apiClient = new Client(apiEndPoint[0], Integer.parseInt(apiEndPoint[1]));
			Future<Value> req = apiClient.callAsyncApply("stampery.3.auth", new Object[]{clientId, secret});
		    req.join();
			auth = req.getResult().asBooleanValue().getBoolean();
			if(auth)
				System.out.println("logged " + clientId);
			
		} catch (UnknownHostException e) {
			System.err.println("couldn't connect to API");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void amqpLogin(){
		ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(amqpEndPoint[0]);
        factory.setPort(Integer.parseInt(amqpEndPoint[1]));
        factory.setUsername(amqpEndPoint[2]);
        factory.setPassword(amqpEndPoint[3]);
        factory.setVirtualHost(amqpEndPoint[4]);
        Connection connection;
		try {
			connection = factory.newConnection();
			System.out.println("[QUEUE] Connected to Rabbit!");
			channel = connection.createChannel();
			emitReady();
			handleQueue();
		} catch (IOException e) {
			emitError(e.getMessage());
		} catch (TimeoutException e) {
			emitError(e.getMessage());
		}
	}
	
	private void handleQueue(){
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				
				String hash = envelope.getRoutingKey();
				org.msgpack.core.MessageUnpacker msgpack = org.msgpack.core.MessagePack.newDefaultUnpacker(body);
							
				emitProof(hash, new Proof(msgpack.unpackValue().asArrayValue()));
			}
		};
		try {
			channel.basicConsume(clientId + "-clnt", true, consumer);
		} catch (IOException e) {
			emitError(e.getMessage());
		}
	}
	
	private String getMD5(String data){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			return digestToString(md.digest(data.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String digestToString(byte[] hash) {
        StringBuffer buff = new StringBuffer();

        for (byte b : hash) {
            buff.append(String.format("%02x", b & 0xFF));
        }

        return buff.toString();
    }
	private void setEndPoints(String branch) {
		if("prod".equalsIgnoreCase(branch)){
			apiEndPoint = new String[]{ "api.stampery.com", "4000"};
			amqpEndPoint = new String[]{ "young-squirrel.rmq.cloudamqp.com", "5672", "consumer", "9FBln3UxOgwgLZtYvResNXE7", "ukgmnhoi"};
		} else {
			apiEndPoint = new String[]{ "api-beta.stampery.com", "4000"};
			amqpEndPoint = new String[]{ "young-squirrel.rmq.cloudamqp.com", "5672", "consumer", "9FBln3UxOgwgLZtYvResNXE7", "beta"};
		}
	}
	
	private void emitReady(){
		for (Consumer consumer : consumers) {
			consumer.onReady();
		}
	}
	private void emitError(String err){
		for (Consumer consumer : consumers) {
			consumer.onError(err);
		}
	}
	private void emitProof(String hash, Proof proof){
		for (Consumer consumer : consumers) {
			consumer.onProof(hash, proof);
		}
	}	
}
