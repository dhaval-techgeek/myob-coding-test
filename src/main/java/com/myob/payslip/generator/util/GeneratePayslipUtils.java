package com.myob.payslip.generator.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.myob.payslip.generator.model.Payslip;
import com.myob.payslip.generator.model.TaxSlab;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class GeneratePayslipUtils {

	public static final String PERCENTAGE_SIGN = "%";
	public static final Logger LOGGER = Logger.getLogger(GeneratePayslipUtils.class);
	static List<TaxSlab> taxSlabs = null;
	static {
		taxSlabs = new ArrayList<TaxSlab>();
		taxSlabs.add(new TaxSlab(0, 18200, 0, 0, 0));
		taxSlabs.add(new TaxSlab(18201, 37000, 18200, 0, 19));
		taxSlabs.add(new TaxSlab(37001, 80000, 37000, 3572, 32.5));
		taxSlabs.add(new TaxSlab(80001, 180000, 80000, 17547, 37));
		taxSlabs.add(new TaxSlab(180001, -1, 180000, 54547, 45));
	}

	public static double calculateGrossIncome(double annualSalary) {
		LOGGER.debug(String.format("Calculating gross income for annual salary {}", annualSalary));
		return Math.round(annualSalary / 12);
	}

	public static double calculateIncomeTax(double annualSalary) {
		LOGGER.debug(String.format("Calculating income tax for annual salary {}", annualSalary));
		TaxSlab taxSlab = getSuitableTaxSlab(annualSalary);
		LOGGER.debug(String.format("Tax slab applied for annual salary {} is {}", annualSalary, taxSlab));
		return Math.round((taxSlab.getFixedTax()
				+ (annualSalary - taxSlab.getThreshold()) * (taxSlab.getPerDollarTaxOverThreshold() / 100)) / 12);
	}

	public static double calculateNetIncome(double grossIncome, double incomeTax) {
		return grossIncome - incomeTax;
	}

	public static double calculateSuper(double grossIncome, double superRate) {
		return Math.round(grossIncome * superRate / 100);
	}

	public static TaxSlab getSuitableTaxSlab(double annualSalary) {
		return taxSlabs.stream().filter(taxSlab -> {
			if (annualSalary >= 180001) {
				return taxSlab.getIncomeRangeStart() >= annualSalary;
			} else {
				return annualSalary >= taxSlab.getIncomeRangeStart() && annualSalary <= taxSlab.getIncomeRangeEnd();
			}
		}).findAny().get();
	}

	public static double getSuperRateFromCsvElement(String superRateStr) {
		double superRate;
		if (superRateStr.endsWith(PERCENTAGE_SIGN)) {
			superRate = Double.parseDouble(superRateStr.substring(0, superRateStr.length() - 1));
		} else {
			superRate = Double.parseDouble(superRateStr);
		}
		return superRate;
	}

	public static void preparePayslip(List<Payslip> payslipList) {
		payslipList.stream().forEach(payslip -> {
			double annualSalary = payslip.getAnnualSalary();
			double grossIncome = calculateGrossIncome(annualSalary);
			double incomeTax = calculateIncomeTax(annualSalary);
			double netIncome = calculateNetIncome(grossIncome, incomeTax);
			payslip.setGrossIncome(grossIncome);
			payslip.setIncomeTax(incomeTax);
			payslip.setNetIncome(netIncome);
			payslip.setSuperAmount(calculateSuper(grossIncome, payslip.getSuperRate()));
		});
	}

	public static void writePayslipOutputToCSV(CSVWriter writer, List<Payslip> payslipList) {
		List<String[]> outputDataForCsv = new ArrayList<String[]>();
		for (Payslip payslip : payslipList) {
			// @formatter:off
			outputDataForCsv.add(new String[] {payslip.getEmpFirstName()+" "+payslip.getEmpLastName(), 
					   payslip.getPayPeriod(), 
					   String.valueOf((int)payslip.getGrossIncome()), 
					   String.valueOf((int)payslip.getIncomeTax()), 
					   String.valueOf((int)payslip.getNetIncome()), 
					   String.valueOf((int)payslip.getSuperAmount())}); 
			// @formatter:on
		}
		writer.writeAll(outputDataForCsv);
	}

	public static List<Payslip> preparePayslipInputAsList(CSVReader reader) throws IOException, CsvValidationException {
		String[] line;
		List<Payslip> payslipList = new ArrayList<Payslip>();
		while ((line = reader.readNext()) != null) {
			Payslip payslip = new Payslip();
			payslip.setEmpFirstName(line[0]);
			payslip.setEmpLastName(line[1]);
			payslip.setAnnualSalary(Double.parseDouble(line[2]));
			payslip.setSuperRate(GeneratePayslipUtils.getSuperRateFromCsvElement(line[3]));
			payslip.setPayPeriod(line[4]);
			payslipList.add(payslip);
		}
		return payslipList;
	}
}
