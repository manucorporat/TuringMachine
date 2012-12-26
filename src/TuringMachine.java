
class TuringMachine
{
	public final static int valueBLANK = -2;
	public final static int valueANY = -3;
	public final static int valueEND = -4;
	
	public final static int valueA = -5;
	public final static int valueB = -6;
	public final static int valueC = -7;
	public final static int valueX = -8;
	public final static int valueY = -9;


	protected Rule[] rules;
	protected int [] tape;
	protected int nuRules;
	protected int state; 
	protected int position;
	protected int size;
	
	TuringMachine (String filename, int t)
	{
		assert(t > 0);
		
		// allocate tape
		this.tape = new int [t];
		this.size = t;
		this.position = this.size/2;
		
		// reset machine
		reset();
		
		// load and parse program.
        Parser p = new Parser (filename);
        if(p.hasRules())
        	loadRules(p.getRules(), p.getNumberOfRules());
	}
	
	
	public int[] getTape()
	{
		return this.tape;
	}
	
	
	public int getPosition()
	{
		return this.position;
	}
	
	
	public void reset()
	{
		// reset tape, blank state is in all fields.
		this.tape = new int [this.size];
		for(int i = 0; i < this.size; ++i)
			this.tape[i] = valueBLANK;
		
		// reset machine's state.
		this.state = 0;
	}
	
	
	public void loadRules (Rule [] t, int nuRules)
	{
		assert(nuRules > 0);
		assert(t.length >= nuRules);
		
		this.rules = t;
		this.nuRules = nuRules;
	}
	
	
	public boolean loadTape (int [] c)
	{
		if (this.size < c.length)
			return false;
		
		this.position = (this.size-c.length)/2;
		for (int i = 0;i < c.length; ++i)
			this.tape[i+this.position] = c[i];
		
		return true;
	}
	
	
	protected Rule findRule (int state, int symbol)
	{
		// sequential search, matching state and symbol.
		Rule f = null;
		for (int i = 0; i < this.nuRules; ++i) {
			Rule t = this.rules[i];
						
			if(t.machineState==state && t.tapeSymbol==symbol)
				return t;
			
			else if ((t.machineState==state || t.machineState==valueANY) &&
				(t.tapeSymbol==symbol || t.tapeSymbol==valueANY))
				f = t;
		}
		return f; 
	}
	
	
	public int step (int times)
	{	
		int i = 0;
		while(i < times)
		{
			// Find rule for current state and current symbol.
			Rule t = findRule(this.state, this.tape[this.position]);
			
			// if the rule was not found, then we stop the machine.
			if(t == null ) {
				end("Not rule was found. Aborted.");
				times = i; // break; (but we can not use break;)
				
			}else
			{
				/*
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				*/
				
				printMachine();

				// update symbol
				if(t.newSymbol != valueANY)
					this.tape[this.position] = t.newSymbol;
				
				
				// update machine's position
				if (t.direction == -1) 
					this.position--;
				else if(t.direction == 1)
					this.position++;
				
				
				// check if the machine is out of memory
				if(this.position < 0 || this.position >= this.size) {
					this.position = 0;
					end("Tape overflow, turing machine aborted.");
					times = i; // break; (but we can not use break;)
					
				}else
				{
					// update machine's state
					if(t.newState == valueEND) {
						end("End symbol found. Stopped succesfully.");
						times = i; // break; (but we can not use break;)
						
					}else if(t.newState != valueANY)
						this.state = t.newState;
				}
			}
			++i;
		}
		return i;
	}
	
	
	protected void end(String message)
	{
		printMachine();

		// At the end, we reset the machine's state to 0.
		this.state = 0;
		System.out.println("E: 0 -> END: \""+message+"\"");
	}
	
	
	private void printMachine()
	{
		System.out.printf("E:%2d",this.state);

		for(int e = 0; e < this.size; ++e) {
			if(this.position == e)
				System.out.print("|");
			else
				System.out.print(" ");

			printValue(this.tape[e]);
		}
		System.out.println();
	}
	
	
	public static void printValue(int value)
	{
		switch(value) {
		case valueA:
			System.out.print(Parser.charA); break;
		case valueB:
			System.out.print(Parser.charB); break;
		case valueC:
			System.out.print(Parser.charC); break;
		case valueX:
			System.out.print(Parser.charX); break;
		case valueY:
			System.out.print(Parser.charY); break;
		case valueANY:
			System.out.print(Parser.charANY); break;
		case valueBLANK:
			System.out.print(Parser.charBLANK); break;
		case valueEND:
			System.out.print(Parser.charEND); break;
		default:
			System.out.print(value);break;
		}
	}
	
	
	public void help()
	{
		System.out.println("***** HELP *****");
		System.out.println("Syntax: <current state> <current symbol> <new state> <new symbol> <direction>");
		System.out.printf("%d: Blank value.\n", valueBLANK);
		System.out.printf("%d: It's a wildcard: it matches any symbol/state.\n", valueANY);
		System.out.printf("%d: End value.\n", valueEND);
		System.out.println("****************\n");
	}
}
