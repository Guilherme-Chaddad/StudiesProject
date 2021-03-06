package com.myFuzzyProject.negotiation;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

public class NegotiationExample1 {
	static boolean withUrgency = false;
	static Double initialPrice = 584.0;
	static Double utility = 0.8476;
	
	static Integer round = 1;
	public static void main(String[] args) throws Exception {
				
		Double offerVendor = initialPrice;
		Double offerBuyer = 300.0;
		Double nextOfferVendor = 0.0;
		Double nextOfferBuyer = 0.0;
		boolean buyerTurn = true;
		
		double incrementPercent = initializeFuzzyBuyer(offerBuyer);

		System.out.println(String.format("-------------------ROUND %d-------------------------", round));
		System.out.println("OFFER VENDOR");
		nextOfferBuyer = offerBuyer + (offerBuyer * (incrementPercent / 100));
		//nextOfferBuyer = offerBuyer + (offerBuyer * 0.05);
		boolean thereIsAgreement = makeOffer(offerVendor, nextOfferBuyer, false);
		
		while(!thereIsAgreement) {
			round++;
			System.out.println(String.format("-------------------ROUND %d-------------------------", round));
			if(buyerTurn) {
				System.out.println("OFFER BUYER");
				offerBuyer = nextOfferBuyer;
				nextOfferVendor = offerVendor - (offerVendor * 0.05);
				thereIsAgreement = makeOffer(offerBuyer, nextOfferVendor, true);
			} else {
				System.out.println("OFFER VENDOR");
				offerVendor = nextOfferVendor;
				nextOfferBuyer = offerBuyer + (offerBuyer * (incrementPercent / 100));
				//nextOfferBuyer = offerBuyer + (offerBuyer * 0.05);
				thereIsAgreement = makeOffer(offerVendor, nextOfferBuyer, false);
			}
			buyerTurn = !buyerTurn;
		}
	}

	private static double initializeFuzzyBuyer(Double offerBuyer) {
		String filename = "NegotiationBuyerExample1.fcl";
		FIS fis = FIS.load(filename, true);

		if (fis == null) {
			System.err.println("Can't load file: '" + filename + "'");
			System.exit(1);
		}
		// Get default function block
		FunctionBlock fb = fis.getFunctionBlock(null);

		// Set inputs
		fb.setVariable("price", initialPrice);
		fb.setVariable("utility", utility);

		// Evaluate
		fb.evaluate();

		// Show output variable's chart
		fb.getVariable("increment").defuzzify();

		// Print ruleSet
		//System.out.println(fb);
		double incrementPercent = fb.getVariable("increment").getValue();
		System.out.println("Incremento: " + incrementPercent);
		return incrementPercent;
	}

	private static boolean makeOffer(Double offer, Double valueToAcceptOffer, boolean isBuyer) {
		System.out.println(String.format("Offer: %.2f - Next Offer: %.2f", offer, valueToAcceptOffer));
		return isBuyer ? offer >= valueToAcceptOffer : offer <= valueToAcceptOffer;
	}
}
