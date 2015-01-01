package com.ojcoleman.ahni.experiments.logicgates;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jgapcustomised.BulkFitnessFunction;
import org.jgapcustomised.Chromosome;

import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;
import com.anji.util.Configurable;
import com.anji.util.Properties;
import com.ojcoleman.ahni.hyperneat.HyperNEATEvolver;

public abstract class LogicGateFitness extends BulkFitnessFunction implements Configurable {

	private static final long serialVersionUID = 1L;
	
	protected final static Logger logger = Logger.getLogger(LogicGateFitness.class);
	private ActivatorTranscriber factory;
	
	private final double[][] logic = { { 0.0d, 0.0d, 1.0d }, { 0.0d, 1.0d, 1.0d }, { 1.0d, 0.0d, 1.0d }, { 1.0d, 1.0d, 1.0d } };

	@Override
	public void init(Properties props) throws Exception {
		factory = (ActivatorTranscriber) props.singletonObjectProperty(ActivatorTranscriber.class);
	}
	
	@Override
	public void evaluate(List<Chromosome> subjects) {
		double max_fitness = 0.0d;
		Iterator<Chromosome> it = subjects.iterator();
		Activator winner = null;
		while (it.hasNext()) {
			Chromosome c = it.next();
			try {
				Activator activator = factory.newActivator(c);
				double mean_fitness = evaluate(activator);
				if (mean_fitness > max_fitness) {
					max_fitness = mean_fitness;
					winner = activator;
				}
				c.setFitnessValue(mean_fitness);
				c.setFitnessValue(mean_fitness, 0);
				c.setPerformanceValue(mean_fitness);
			} catch (Throwable e) {
				System.out.println("error evaluating chromosome " + c.toString());
				c.setFitnessValue(-1);
				c.setFitnessValue(-1, 0);
				c.setPerformanceValue(-1);
			}		
		}
		
	}

	private double evaluate(Activator network) {
		double sum_fitness = 0.0d;
		for (int i = 0; i < 4; i++) {
			network.reset();
			sum_fitness += singleTrial(i, network);
		}
		double fitness = 4.0f - sum_fitness;
		double sq_fit = fitness * fitness;
		sq_fit /= 16.0f;
		return sq_fit;
	}

	
	private double singleTrial(int index, Activator controller) {	
		double target = correctOutput((logic[index][0] > 0.5d), (logic[index][1] > 0.5d)) ? 1.0d : 0.0d;
		double output = controller.next(logic[index])[0];
		double difference = Math.abs(output - target);
		double fitness = 1 - difference; 
		return fitness;
	}
	
	public abstract boolean correctOutput(boolean input_a, boolean input_b);

	@Override
	public boolean endRun() {
		return false;
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void evolutionFinished(HyperNEATEvolver evolver) {
		
	}

}
