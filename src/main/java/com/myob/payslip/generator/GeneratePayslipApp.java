package com.myob.payslip.generator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.myob.payslip.generator.model.Payslip;
import com.myob.payslip.generator.util.GeneratePayslipUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class GeneratePayslipApp {

	public static final Logger LOGGER = Logger.getLogger(GeneratePayslipApp.class);

	public static void main(String[] args) {
		LOGGER.info("Generating payslips.");
		CSVReader reader = null;
		CSVWriter writer = null;
		try {
			reader = new CSVReader(new FileReader(new File(args[0])));
			List<Payslip> payslipList = GeneratePayslipUtils.preparePayslipInputAsList(reader);
			GeneratePayslipUtils.preparePayslip(payslipList);
			writer = new CSVWriter(new FileWriter(new File(args[1])), 
													CSVWriter.DEFAULT_SEPARATOR, 
													CSVWriter.NO_QUOTE_CHARACTER,
													CSVWriter.NO_ESCAPE_CHARACTER,
													CSVWriter.DEFAULT_LINE_END);
			GeneratePayslipUtils.writePayslipOutputToCSV(writer, payslipList);
		} catch (IOException | CsvValidationException e) {
			LOGGER.error("Something went while generating payslip records", e);
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					LOGGER.error("Something went wrong while closing writer object for output CSV file", e);
				}
			}
		}
		LOGGER.info("Payslip generation completed.");
	}
}
