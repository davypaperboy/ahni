package com.ojcoleman.ahni.experiments.logicgates;

public class XORFitness extends LogicGateFitness {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean correctOutput(boolean input_a, boolean input_b) {
		if (input_a == false && input_b == false) return false;
		if (input_a == true && input_b == true) return false;
		if (input_a == true && input_b == false) return true;
		if (input_a == false && input_b == true) return true;
		logger.info("Error with inputs: " + (input_a ? "T" : "F") + " " + (input_b ? "T" : "F"));
		return false;
	}
	
}
