import info.gridworld.actor.ActorWorld;


class TuringMachine
{
	final static int valueANY = -4;
	final static int valueEND = -3;
	final static int valueBLANK = -2;

	Rule[] rules;
	int [] tape;
	int nuRules;
	int state; 
	int position;
	int size;
	
	TuringMachine (String filename, int t)
	{
		this.tape = new int [t];
		for(int i = 0; i < t; ++i)
			this.tape[i] = valueBLANK;
		
		this.size = t; 
		this.state = 0;
		
        Parser p = new Parser (filename);
        if(p.hasRules())
        	loadRules(p.getRules(), p.getNumberOfRules());
        
        //if(p.hasTape()) {
        //	cargarCinta(p.cinta);
        //}   
	}
	
	
	void loadRules (Rule [] t, int nuRules)
	{
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
		for(int i = 0; i < times; i++)
		{
			Rule t = findRule(this.state, this.tape[this.position]);
			
			if (t == null) {
				end ();
				break;
			
			}else 
			{		
				update();

				// update symbol
				if(t.newSymbol != valueANY)
					this.tape[this.position] = t.newSymbol;
				
				
				// update machine's position
				if (t.direction == -1) 
					this.position--;
				else if(t.direction == 1)
					this.position++;
				
				
				// update machine's state
				if(t.newState == valueEND) {
					end();
					break;
					
				}else if(t.newState != valueANY)
					this.state = t.newState;
			}
		}
	}
	
	
	void end ()
	{
		// At the end, reset state
		this.state = 0;
		System.out.println("END");
	}
	
	
	void update ()
	{
		System.out.print("E:"+this.state);

		for(int e = 0; e < this.size; ++e) {
			if(this.position == e)
				System.out.print("|");
			else
				System.out.print(" ");

			if(this.tape[e] == -2)
				System.out.print("_");
			else
				System.out.print(this.tape[e]);

		}
		System.out.println();
	}
}

