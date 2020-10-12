package com.myob.payslip.generator.model;

public class TaxSlab {
	private double incomeRangeStart;
	private double incomeRangeEnd;
	private double threshold;
	private double fixedTax;
	private double perDollarTaxOverThreshold;

	public TaxSlab(double incomeRangeStart, double incomeRangeEnd, double threshold, double fixedTax,
			double perDollarTaxOverThreshold) {
		super();
		this.incomeRangeStart = incomeRangeStart;
		this.incomeRangeEnd = incomeRangeEnd;
		this.threshold = threshold;
		this.fixedTax = fixedTax;
		this.perDollarTaxOverThreshold = perDollarTaxOverThreshold;
	}

	public double getIncomeRangeStart() {
		return incomeRangeStart;
	}

	public void setIncomeRangeStart(double incomeRangeStart) {
		this.incomeRangeStart = incomeRangeStart;
	}

	public double getIncomeRangeEnd() {
		return incomeRangeEnd;
	}

	public void setIncomeRangeEnd(double incomeRangeEnd) {
		this.incomeRangeEnd = incomeRangeEnd;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getFixedTax() {
		return fixedTax;
	}

	public void setFixedTax(double fixedTax) {
		this.fixedTax = fixedTax;
	}

	public double getPerDollarTaxOverThreshold() {
		return perDollarTaxOverThreshold;
	}

	public void setPerDollarTaxOverThreshold(double perDollarTaxOverThreshold) {
		this.perDollarTaxOverThreshold = perDollarTaxOverThreshold;
	}

}
