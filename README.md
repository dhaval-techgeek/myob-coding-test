# myob-coding-test
payslip-generator

This is a project developed using Maven build tool. To compile this project, use below command:

mvn clean install

On successful execution of the command, below things will happen:
1. Execute all test cases
2. Compile the code and generate two artifacts under `target` folder. The artifacts are JAR file: `myob-payslip-generator.jar` and `myob-payslip-generator-jar-with-dependencies.jar`. These Jar files are with and without third-party dependencies, respectively.

To run the artifact:
1. The artifact named `myob-payslip-generator-jar-with-dependencies.jar` is the one which is an executable artifact. Use below command to execute artifact:

`java -jar myob-payslip-generator-jar-with-dependencies.jar <<INPUT_FILE_LOCATION>> <<OUTPUT_FILE_LOCATION>>`

example:
`java -jar myob-payslip-generator-jar-with-dependencies.jar F:\data\input.csv F:\data\output.csv`

Assumption:
1. OpenCSV dependency is used to read and write CSV files
2. JUnit dependency is used to execute test cases
3. Log4J dependency is used for log generation
4. The input CSV should follow below structure (Columns are: first name, last name, annual salary, super rate (%), payment start date). The input CSV should be without header row in CSV. In the super rate, percentage sign(%) is optional:

  David,Rudd,60050,9%,01 March – 31 March<br />
  Ryan,Chen,120000,10%,01 March – 31 March

5. The output will be follow below structure (Columns are: name, pay period, gross income, income tax, net income, super)

  David Rudd,01 March – 31 March,5004,922,4082,450<br />
  Ryan Chen,01 March – 31 March,10000,2696,7304,1000

6. To add new tax slab, we need a code change for now. It's not configurable externally.
7. When the artifact will run, it will generate the log file at the same place where the JAR is located