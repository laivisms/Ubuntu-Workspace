package edu.yu.distributed.DistributedCalculator;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WorkerLogicHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
	
	static final String BAD_REQUEST = "Error: Request incorrectly formatted.";
	static final String KEY_NOT_EXIST = "Error: the specified key does not exist";
	static final String INSERT = "insert";
	static final String RETRIEVE = "retrieve";
	static final String DELETE = "delete";
	static final String CALCULATE = "calculate";
	static final String VALID_TEXT = "(\\p{Alpha})+";
	static final String VALID_VALUE = "(\\d)+";
	;
	
	public static String handleRequest(String workerName, byte[] req, WorkerData data) {
		
		String request = new String(req);
		
		if(request.length() < 6)
			return BAD_REQUEST;
		else if(request.substring(0,6).toLowerCase().equals(INSERT))
			return handlePut(workerName, request, data);
		
		else if(request.length()>8 && request.substring(0,8).toLowerCase().equals(RETRIEVE)) 
			return handleGet(workerName, request, data);
		
		else if(request.substring(0,6).toLowerCase().equals(DELETE))
			return handleDelete(workerName, request, data);
		
		else if(request.length()>9 && request.substring(0,9).toLowerCase().equals(CALCULATE))
			return handleCalculate(workerName, request, data);
		return BAD_REQUEST;
	}
	
	
	private static String handlePut(String workerName, String request, WorkerData data) {
		String result = null;
	
			int firstSpace = request.indexOf(' ');
			int lastSpace = request.lastIndexOf(' ');
			String key = request.substring(firstSpace + 1, lastSpace);
			Integer value = Integer.parseInt(request.substring(lastSpace+1));
			data.put(key, value);
			result = request + " " + String.valueOf(value);
			
			LOG.info("Worker " + workerName + " now contains " + value + " under key: " + key);
		
		 
		return result;
	}
	
	private static String handleGet(String workerName, String request, WorkerData data) {
		String result = null;
		String key = null;
		Integer value = null;
		
		if(!Pattern.matches(RETRIEVE + "\\s" + VALID_TEXT, request))
			result = BAD_REQUEST;
		else {
			int space = request.indexOf(" ");
			key = request.substring(space+1);
			if(!data.contains(key))
				result = KEY_NOT_EXIST;
			else {
				value = data.get(key);
				result = request + " " + String.valueOf(value);
			}
		}
		
		LOG.info("Worker " + workerName + " has accessed key " + key + " containing value: " + value);
		
		return result;
	}
	
	private static String handleDelete(String workerName, String request, WorkerData data) {
		String result = null;
		String key = null;
		Integer value = null;
		
		if(!Pattern.matches(DELETE + "\\s" + VALID_TEXT, request))
			result = BAD_REQUEST;
		else {
			int space = request.indexOf(" ");
			key = request.substring(space+1);
			
			if(data.contains(key))
				value = data.get(key);
				data.remove(key);
			result = request + " " + String.valueOf(value);
		}
		
		LOG.info("Worker " + workerName + " has deleted key " + key + " containing value: " + value);
		
		return result;
	}
	
	private static String handleCalculate(String workerName, String request, WorkerData data) {
		String result = null;
		
		int firstSpace = request.indexOf(" ");
		int secondSpace = request.indexOf(" ", firstSpace+1);
		
		String orderNumber = request.substring(firstSpace + 1, secondSpace);
		String operator = request.substring(secondSpace+1,secondSpace+2);
		String operands = request.substring(secondSpace+3);
		String[] operandList = operands.split(" ");
		
		String opValues = data.get(operandList[0]) + "";
		
		int solution = data.get(operandList[0]);
		
		int value;
		String op;
		
		for(int i=1; i<operandList.length; i++) {
			op = operandList[i];
			value = data.get(op);
			opValues += " " + value;
			if((!op.equals("/") && !op.equals("-")) || orderNumber.equals("1"))//cannot compute / or - operations (because pemdas) unless they are the first ones in the list
				solution = performOp(operator, solution, value);
		}
		
		result = "calculate " + orderNumber + " " + operator + " " + opValues + " " + solution;
		
		return result;
	}
	
	private static int performOp(String operator, int a, int b) {
		int solution = 0;
		
		switch (operator) {
		case("+"):
			solution = a + b;
			break;
		
		case("-"):
			solution = a - b;
			break;
		
		case("*"):
			solution = a * b;
			break;
		
		case("/"):
			if(b==0 || a==0)
				solution = 0;
			else
				solution = a / b;
			break;
		}
		
		return solution;
	}
	
	
	
	
}
