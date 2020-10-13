package com.myob.payslip.generator;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.myob.payslip.generator.model.Payslip;
import com.myob.payslip.generator.util.GeneratePayslipUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class GeneratePayslipAppTest {

	private CSVReader csvReader;
	private CSVWriter csvWriter;
	private CSVReader outputCsvReader;
	private int numberOfInputRecordsInCSV = 2;
	public static final String INPUT_CSV_FILE_NAME = "test-input.csv";
	public static final String OUTPUT_CSV_FILE_NAME = "test-output.csv";

	@Before
	public void setup() throws URISyntaxException, IOException {
		URL inputResource = getClass().getClassLoader().getResource("test-input.csv");
		URL outputResource = getClass().getClassLoader().getResource("test-output.csv");
		csvReader = new CSVReader(new FileReader(new File(inputResource.toURI())));
		csvWriter = new CSVWriter(new FileWriter(new File(outputResource.toURI())));
		outputCsvReader = new CSVReader(new FileReader(new File(outputResource.toURI())));
	}
	
	@Test
	public void preparePayslipInputAsListTest() throws CsvValidationException, IOException
	{
		List<Payslip> payslipList =  GeneratePayslipUtils.preparePayslipInputAsList(csvReader);
		assertThat(payslipList, instanceOf(ArrayList.class));
		assertEquals(numberOfInputRecordsInCSV, payslipList.size());
	}
	
	@Test
	public void preparePayslipTest() throws CsvValidationException, IOException
	{
		List<Payslip> payslipList =  GeneratePayslipUtils.preparePayslipInputAsList(csvReader);
		GeneratePayslipUtils.preparePayslip(payslipList);
		assertEquals(numberOfInputRecordsInCSV, payslipList.size());
	}
	
	@Test
	public void writePayslipOutputToCSVTest() throws CsvValidationException, IOException
	{
		List<Payslip> payslipList =  GeneratePayslipUtils.preparePayslipInputAsList(csvReader);
		GeneratePayslipUtils.preparePayslip(payslipList);
		GeneratePayslipUtils.writePayslipOutputToCSV(csvWriter, payslipList);
		csvWriter.close();
		List<Payslip> payslipOutputList =  GeneratePayslipUtils.preparePayslipInputAsList(outputCsvReader);
		assertEquals(payslipList.size(), payslipOutputList.size());
	}
	
	
}
