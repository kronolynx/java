package com.stampery;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;


public class Stampery {
	private List<Client> clients = new ArrayList<Client>();
	private String secret;
	private String branch;

	public Stampery(String secret) {
		this.secret = secret;
		this.branch = "prod";
	}
	
	public Stampery(String secret, String branch) {
		this.secret = secret;
		this.branch = branch;
	}

	public void start() {
		for (Client client : clients) {
			client.onProof("hey", "you");
		}
		
	}

	public void subscribe(Client client) {
		clients.add(client);
	}

	public String hash(String string) {
		final DigestSHA3 sha3 = new DigestSHA3();

        sha3.update(input.getBytes());

        return TestSha3.hashToString(sha3);
	}

	public void stamp(String digest) {
		
	}    
	
	private void apiLogin(){
		
	}
	
	private void amqpLogin(){
		
	}
	
	private void handleQueue(){
		
	}
}
