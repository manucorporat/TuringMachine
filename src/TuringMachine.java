
class TuringMachine
{
	final static int valueBLANK = -2;
	final static int valueANY = -3;
	final static int valueEND = -4;
	
	final static int valueA = -5;
	final static int valueB = -6;
	final static int valueX = -7;
	final static int valueY = -8;


	Rule[] rules;
	int [] tape;
	int nuRules;
	int state; 
	int position;
	int size;
	
	TuringMachine (String filename, int t)
	{
		assert(t > 0);

		help();
		
		// Allocate tape
		this.tape = new int [t];
		this.size = t;
		
		// Reset machine
		reset();
		
		// Load and parse program.
        Parser p = new Parser (filename);
        if(p.hasRules())
        	loadRules(p.getRules(), p.getNumberOfRules());
	}
	
	
	void reset()
	{
		// Reset tape, blank state is in all fields.
		this.tape = new int [this.size];
		for(int i = 0; i < this.size; ++i)
			this.tape[i] = valueBLANK;
		
		// Reset machine's state.
		this.state = 0;
	}
	
	
	void loadRules (Rule [] t, int nuRules)
	{
		assert(nuRules > 0);
		assert(t.length >= nuRules);
		
		this.rules = t;
		this.nuRules = nuRules;
	}
	
	
	boolean loadTape (int [] c)
	{
		if (this.size < c.length)
			return false;
		
		this.position = (this.size-c.length)/2;
		for (int i = 0;i < c.length; ++i)
			this.tape[i+this.position] = c[i];
		
		return true;
	}
	
	
	Rule findRule (int state, int symbol)
	{
		// sequential search, matching state and symbol.
		for (int i = 0; i < this.nuRules; ++i) {
			Rule t = this.rules[i];
			if ((t.machineState==state || t.machineState==valueANY) &&
				(t.tapeSymbol==symbol || t.tapeSymbol==valueANY))
				return t;
		}
		return null; 
	}
	
	
	void step (int times)
	{	
		int i = 0;
		while(i < times)
		{
			// Find rule for current state and current symbol.
			Rule t = findRule(this.state, this.tape[this.position]);
			
			// if the rule was not found, then we stop the machine.
			if(t == null ) {
				end();
				i = times; // break; (but we can not use break;)
				
			}else
			{
				// updates gridworld and prints logs in console.
				update();

				// update symbol
				if(t.newSymbol != valueANY)
					this.tape[this.position] = t.newSymbol;
				
				
				// update machine's position
				if (t.direction == -1) 
					this.position--;
				else if(t.direction == 1)
					this.position++;
				
				
				if(this.position < 0 || this.position >= this.size) {
					System.out.println("Not enought memory, turing machine aborted.");
					this.position = 0;
					end();
					i = times; // break; (but we can not use break;)
					
				}else
				{
					// update machine's state
					if(t.newState == valueEND) {
						end();
						i = times; // break; (but we can not use break;)
						
					}else if(t.newState != valueANY)
						this.state = t.newState;
				}
			}
			++i;
		}
	}
	
	
	void end ()
	{
		// At the end, we reset the machine's state to 0.
		this.state = 0;
		update ();
		System.out.println("END");
	}
	
	
	void update ()
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
	
	void help()
	{
		System.out.println("***** HELP *****");
		System.out.printf("%d: Blank value.\n", valueBLANK);
		System.out.printf("%d: It's a wildcard: it matches any symbol/state.\n", valueANY);
		System.out.printf("%d: End value.\n", valueEND);
		System.out.println("****************\n");
	}
}
