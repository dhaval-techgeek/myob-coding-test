package com.myob.payslip.generator.model;

public class Payslip {
	private String empFirstName;
	private String empLastName;
	private double annualSalary;
	private double superRate;
	private String payPeriod;
	private double grossIncome;
	private double incomeTax;
	private double netIncome;
	private double superAmount;

	public String getEmpFirstName() {
		return empFirstName;
	}

	public void setEmpFirstName(String empFirstName) {
		this.empFirstName = empFirstName;
	}

	public String getEmpLastName() {
		return empLastName;
	}

	public void setEmpLastName(String empLastName) {
		this.empLastName = empLastName;
	}

	public double getAnnualSalary() {
		return annualSalary;
	}

	public void setAnnualSalary(double annualSalary) {
		this.annualSalary = annualSalary;
	}

	public double getSuperRate() {
		return superRate;
	}

	public void setSuperRate(double superRate) {
		this.superRate = superRate;
	}

	public String getPayPeriod() {
		return payPeriod;
	}

	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}

	public double getGrossIncome() {
		return grossIncome;
	}

	public void setGrossIncome(double grossIncome) {
		this.grossIncome = grossIncome;
	}

	public double getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(double incomeTax) {
		this.incomeTax = incomeTax;
	}

	public double getNetIncome() {
		return netIncome;
	}

	public void setNetIncome(double netIncome) {
		this.netIncome = netIncome;
	}

	public double getSuperAmount() {
		return superAmount;
	}

	public void setSuperAmount(double superAmount) {
		this.superAmount = superAmount;
	}

}
