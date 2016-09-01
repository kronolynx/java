package com.stampery;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.msgpack.value.ArrayValue;
import org.msgpack.value.ImmutableArrayValue;

public class Proof {
	private int version;
	private String[] siblings;
	private String root;
	private Anchor anchor;
	
	public Proof(ArrayValue proof) {
		version = proof.get(0).asIntegerValue().asInt();
		if(proof.get(1).isArrayValue())
			siblings = getSiblings(proof.get(1).asArrayValue());
		else
			siblings = new String[]{};
		root = proof.get(2).toString();
		anchor = new Anchor(proof.get(3).asArrayValue());
	} 
	

	public int getVersion() {
		return version;
	}


	public String[] getSiblings() {
		return siblings;
	}


	public String getRoot() {
		return root;
	}


	public Anchor getAnchor() {
		return anchor;
	}


	@Override
	public String toString() {
		return "\nVersion: " + version + "\nSiblings:\n" + 
				Arrays.toString(siblings) + 
				"\nRoot: " + root + "\n" + anchor;
	}
	
	private String[] getSiblings(ArrayValue arrayValue){
		String[] temp = new String[arrayValue.size()];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = arrayValue.get(i).toString();
		}
		return temp;
	}
}
