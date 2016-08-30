package com.stampery;

import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.Future;
import org.msgpack.type.Value;



public class Stampery {

	private List<Consumer> consumers = new ArrayList<Consumer>();
	private String secret;
	private String clientId;
	private String[] apiEndPoint;
	private String[] amqpEndPoint;
	private Client client;
	private boolean auth;

	public Stampery(String secret) {
		this(secret, "prod");
	}
	
	public Stampery(String secret, String branch) {
		this.secret = secret;
		clientId = getMD5(secret).substring(0, 15);
		System.out.println(clientId);
		setEndPoints(branch);
		
	}

	public void start() {
		System.out.println("start");
		apiLogin();
	}

	public void subscribe(Consumer consumer) {
		System.out.println("subscribe");
		consumers.add(consumer);
	}

	public String hash(String data) {
		final DigestSHA3 sha3 = new DigestSHA3(512);

        sha3.update(data.getBytes());

        return digestToString(sha3.digest()).toUpperCase();
	}
	

	public void stamp(String digest) {
		
	}    
	
	private void apiLogin(){
		try {
			client = new Client(apiEndPoint[0], Integer.parseInt(apiEndPoint[1]));
			Future<Value> req = client.callAsyncApply("stampery.3.auth", new Object[]{clientId, secret});
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
		
	}
	
	private void handleQueue(){
		
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
//			apiEndPoint = new String[]{ "api.stampery.com", "4000"};
			apiEndPoint = new String[]{ "localhost", "4000"};
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
}
