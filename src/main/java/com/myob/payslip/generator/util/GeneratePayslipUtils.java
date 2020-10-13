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

/**
 * 
 * @author Dhaval Shah
 * @version 1.0
 * @since 13/October/2020
 * 
 * This class contains all the utilities to do the calculations regarding salary slip generation
 * 
 *
 */
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

	/**
	 * 
	 * @param annualSalary
	 * @return grossIncome
	 */
	public static double calculateGrossIncome(double annualSalary) {
		LOGGER.debug(String.format("Calculating gross income for annual salary %s", annualSalary));
		return Math.round(annualSalary / 12);
	}

	/**
	 * @param annualSalary
	 * @return incomeTax
	 */
	public static double calculateIncomeTax(double annualSalary) {
		LOGGER.debug(String.format("Calculating income tax for annual salary %s", annualSalary));
		TaxSlab taxSlab = getSuitableTaxSlab(annualSalary);
		LOGGER.debug(String.format("Tax slab applied for annual salary %s is %s", annualSalary, taxSlab));
		return Math.round((taxSlab.getFixedTax()
				+ (annualSalary - taxSlab.getThreshold()) * (taxSlab.getPerDollarTaxOverThreshold() / 100)) / 12);
	}
	
	/**
	 * 
	 * @param grossIncome
	 * @param incomeTax
	 * @return netIncome
	 */
	public static double calculateNetIncome(double grossIncome, double incomeTax) {
		LOGGER.debug(String.format("Calculating net income for gross income %s and income tax %s", grossIncome, incomeTax));
		return grossIncome - incomeTax;
	}

	/**
	 * 
	 * @param grossIncome
	 * @param superRate
	 * @return superAmount
	 */
	public static double calculateSuper(double grossIncome, double superRate) {
		LOGGER.debug(String.format("Calculating super for gross income %s and super rate %s", grossIncome, superRate));
		return Math.round(grossIncome * superRate / 100);
	}

	/**
	 * 
	 * @param annualSalary
	 * @return TaxSlab
	 */
	public static TaxSlab getSuitableTaxSlab(double annualSalary) {
		LOGGER.debug(String.format("Getting suitable Tax Slab for annual salary %s", annualSalary));
		return taxSlabs.stream().filter(taxSlab -> {
			if (annualSalary >= 180001) {
				return taxSlab.getIncomeRangeStart() >= annualSalary;
			} else {
				return annualSalary >= taxSlab.getIncomeRangeStart() && annualSalary <= taxSlab.getIncomeRangeEnd();
			}
		}).findAny().get();
	}

	/**
	 * 
	 * @param superRateStr
	 * @return superRate
	 */
	public static double getSuperRateFromCsvElement(String superRateStr) {
		LOGGER.debug(String.format("Getting superRate from CSV element %s", superRateStr));
		double superRate;
		if (superRateStr.endsWith(PERCENTAGE_SIGN)) {
			superRate = Double.parseDouble(superRateStr.substring(0, superRateStr.length() - 1));
		} else {
			superRate = Double.parseDouble(superRateStr);
		}
		LOGGER.debug(String.format("Super rate is %s", superRate));

		return superRate;
	}

	/**
	 * 
	 * @param payslipList
	 */
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

	/**
	 * 
	 * @param writer
	 * @param payslipList
	 */
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

	/**
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 * @throws CsvValidationException
	 */
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
