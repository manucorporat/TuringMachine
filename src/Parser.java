
import java.io.*;
import java.util.Scanner;
import java.io.File;



class Rule
{
	public int machineState; // q
	public int tapeSymbol; // e
	
	public int newState; // p
	public int newSymbol; // f
	public int direction; // m
	
	Rule()
	{
		this.machineState = 0;
		this.tapeSymbol = 0;
		this.newState = 0;
		this.newSymbol = 0;
		this.direction = 0;
	}
}


class Parser
{
	private final int maxRules = 200;
	public final static int charANY = '*';
	public final static int charEND = 'h';
	public final static int charBLANK = 'b';

	
	protected Rule [] rules;
	protected int nuRules;

	
	Parser(String filename)
	{
		// allocate array of references
		this.rules = new Rule[maxRules];
		readFile(filename);
	}
	
	
	public boolean hasRules()
	{
		return (this.nuRules > 0);
	}
	
	
	public boolean hasTape()
	{
		return false;
	}
	
	
	public Rule[] getRules()
	{
		return this.rules;
	}
	
	
	public int getNumberOfRules()
	{
		return this.nuRules;
	}
	

	public boolean readFile(String filename)
	{
		System.out.println("Parsing \""+filename+"\"");
		
		this.nuRules = 0;
		try{
			Scanner sc = new Scanner(new File(filename));
			
			while (sc.hasNextLine()) {
				
				if(this.nuRules >= maxRules) {
					// not enough memory
					throw new OutOfMemoryError("Too many lines to parse.");
				}
				
				// get line
				String strLine = sc.nextLine();
				
				// allocate rule
				Rule t = new Rule();
				this.rules[this.nuRules] = t;
				
				try {
					parseLine(strLine, t);
					print(t);
				} catch(Exception e) {
					System.out.println("Error at line "+(this.nuRules+1));
					// the exception is rethrown.
					throw e;
				}
				++this.nuRules;
			}
			
		}catch (Exception e){ //Catch exception if any
			System.err.println("Error: " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	
	protected int parseZone(String subZone, int min, int max)
	{
		char first = subZone.charAt(0);
		switch(first) {
		case charANY:
			return TuringMachine.valueANY;
			
		case charEND:
			return TuringMachine.valueEND;
			
		case charBLANK:
			return TuringMachine.valueBLANK;
			
		default:
			int r = Integer.parseInt(subZone);
			if(r < min || r > max)
				throw new IllegalStateException("Out of range ["+min+", "+max+"]");
			
			return r;
		}
	}
	
	
	protected void parseLine(String line, Rule t)
	{		
		String[] zonas = line.split("\\s+");

		if(zonas.length < 5)
			throw new IllegalStateException("Missing 5-tuplas rule.");
		
		
		t.machineState = parseZone(zonas[0], 0, 100000);
		t.tapeSymbol = parseZone(zonas[1], 0, 1);
		t.newState = parseZone(zonas[2], 0, 10000);
		t.newSymbol = parseZone(zonas[3], 0, 6);
		t.direction = parseZone(zonas[4], -1, 1);		
	}
	
	
	protected void print(Rule t)
	{
		System.out.printf("%3d %3d %3d %3d %3d \n",
				t.machineState,
				t.tapeSymbol,
				t.newState,
				t.newSymbol,
				t.direction);
	}
}

