package com.myob.payslip.generator.util;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.myob.payslip.generator.model.Payslip;
import com.myob.payslip.generator.model.TaxSlab;

public class GeneratePayslipUtilsTest {

	private Payslip payslip1;
	private Payslip payslip2;
	private Payslip payslip3;

	@Before
	public void setup() {
		payslip1 = new Payslip();
		payslip1.setEmpFirstName("TestFirstName1");
		payslip1.setEmpLastName("TestLastName1");
		payslip1.setAnnualSalary(120000);

		payslip2 = new Payslip();
		payslip2.setEmpFirstName("TestFirstName2");
		payslip2.setEmpLastName("TestLastName2");
		payslip2.setAnnualSalary(60050);
		payslip2.setSuperRate(9);

		payslip3 = new Payslip();
		payslip3.setEmpFirstName("TestFirstName2");
		payslip3.setEmpLastName("TestLastName2");
		payslip3.setAnnualSalary(130401);
	}

	@Test
	public void grossIncomeTest() {
		assertEquals(10000, GeneratePayslipUtils.calculateGrossIncome(payslip1.getAnnualSalary()), 0.0);
	}

	@Test
	public void grossIncomeRoundDownTest() {
		assertEquals(5004, GeneratePayslipUtils.calculateGrossIncome(payslip2.getAnnualSalary()), 0.0);
	}

	@Test
	public void grossIncomeRoundUpTest() {
		assertEquals(10867, GeneratePayslipUtils.calculateGrossIncome(payslip3.getAnnualSalary()), 0.0);
	}

	@Test
	public void calculateTaxTest() {
		assertEquals(922, GeneratePayslipUtils.calculateIncomeTax(payslip2.getAnnualSalary()), 0.0);
	}

	@Test
	public void calculateNetIncomeTest() {
		assertEquals(4082,
				GeneratePayslipUtils.calculateNetIncome(
						GeneratePayslipUtils.calculateGrossIncome(payslip2.getAnnualSalary()),
						GeneratePayslipUtils.calculateIncomeTax(payslip2.getAnnualSalary())),
				0.0);
	}

	@Test
	public void calculateSuperTest() {
		assertEquals(450,
				GeneratePayslipUtils.calculateSuper(
						GeneratePayslipUtils.calculateGrossIncome(payslip2.getAnnualSalary()), payslip2.getSuperRate()),
				0.0);
	}
	
	@Test
	public void getSuitableTaxSlabTest() {
		assertNotNull(GeneratePayslipUtils.getSuitableTaxSlab(payslip2.getAnnualSalary()));
		assertThat(GeneratePayslipUtils.getSuitableTaxSlab(payslip2.getAnnualSalary()), instanceOf(TaxSlab.class));

	}
}
