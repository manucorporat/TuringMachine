
class TuringMachine
{
	public final static int valueSTART = 0;
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
	
	
	TuringMachine (int t)
	{
		assert(t > 0);
		
		// allocate tape
		this.tape = new int [t];
		this.size = t;
		
		// reset machine
		reset();
	}
	
	
	TuringMachine (String filename, int t)
	{
		this(t);

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
		this.state = valueSTART;
		this.position = this.size/2;
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
			
			boolean A = t.machineState==state;
			boolean B = t.tapeSymbol==symbol;

			if(A && B)
				return t;
			
			else if ((B || t.tapeSymbol==valueANY) &&
					(A || t.machineState==valueANY))
				f = t;
		}
		return f; 
	}
		
	
	public int run () {
		return step(Integer.MAX_VALUE);
	}
	
	
	public int step (int times)
	{	
		int i = 0;
		Rule t = null;
		
		while(i < times)
		{
			// Find rule for current state and current symbol.
			t = findRule(this.state, this.tape[this.position]);
			
			// if the rule was not found, then we stop the machine.
			if(t == null ) {
				end("Not rule was found. turing machine aborted.");
				times = i; // break; (but we can not use break;)
				
			}else
			{
				printMachine();

				// update symbol
				if(t.newSymbol != valueANY)
					this.tape[this.position] = t.newSymbol;
				
				
				// update machine's position
				if (t.direction == -1) 
					--this.position;
				else if(t.direction == 1)
					++this.position;
				
				
				
				// check if the machine is out of memory
				if(this.position < 0 || this.position >= this.size) {
					
					this.position = 0;
					end("Tape overflow, turing machine aborted.");
					times = i; // break; (but we can not use break;)
					
				// check if new status is END value.
				}else if(t.newState == valueEND) {
					
					end("End symbol found. Stopped succesfully. "+i+" steps.");
					times = i; // break; (but we can not use break;)
					
				}else if(t.newState != valueANY)
						this.state = t.newState;
			}
			++i;
		}
		return i;
	}
	
	
	protected void end(String message)
	{
		printMachine();

		// At the end, we reset the machine's state to 0.
		this.state = valueSTART;
		System.out.println("E: 0 -> END: \""+message+"\"");
	}
	
	
	public void printMachine()
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
