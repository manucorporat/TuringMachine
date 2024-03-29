/*
 * TuringMachine
 *
 * Copyright (c) 2012 Manuel Mart�nez-Almeida
 * Copyright (c) 2012 Jose Maria Pinilla
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

class TuringMachine
{
	public final static int valueSTART = 1;
	public final static int valueBLANK = -2;
	public final static int valueANY = -3;
	public final static int valueEND = -4;
	
	public final static int valueA = -5;
	public final static int valueB = -6;
	public final static int valueC = -7;
	public final static int valueD = -8;
	public final static int valueX = -9;
	public final static int valueY = -10;

	protected Rule[] rules;
	protected int [] tape;
	protected int nuRules;
	protected int state; 
	protected int position;
	protected int size;
	public boolean inHaltState;
	
	
	TuringMachine (int t)
	{
		assert(t > 0);
		
		// allocate tape
		this.tape = new int [t];
		this.size = t;
		
		// reset machine
		reset();
	}
	
	
	TuringMachine (Parser parser, int t)
	{
		this(t);

        if(parser.hasRules())
        	loadRules(parser.getRules(), parser.getNumberOfRules());
	}
	
	
	TuringMachine (String filename, int t)
	{
		this(new Parser (filename), t);
	}
	
	
	public int[] getTape()
	{
		return this.tape;
	}
	
	
	public int getPosition()
	{
		return this.position;
	}
	
	
	public int getState()
	{
		return this.state;
	}
	
	
	public void reset()
	{
		// reset tape, blank state is in all fields.
		this.tape = new int [this.size];
		for(int i = 0; i < this.size; ++i)
			this.tape[i] = valueBLANK;
		
		// reset machine's state.
		this.inHaltState = false;
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
	
	
	public void setPosition(int position)
	{
		this.position = position;
	}
	
	
	public boolean loadTape (int [] c)
	{
		if (c == null || this.size < c.length)
			return false;
		
		this.position = (this.size-c.length)/2;
		for (int i = 0; i < c.length; ++i)
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
		
	
	public int run ()
	{
		long timestamp = System.currentTimeMillis();
		int steps = step(Integer.MAX_VALUE);
		float s = (System.currentTimeMillis() - timestamp)/1000.0f;
		System.out.println(s+" segundos.");
		return steps;
	}
	
	
	public int step (int times)
	{	
		assert(times >= 0);
		
		int step = 0;
		Rule rule = null;
		inHaltState = false;
		
		while(step < times)
		{
			// Find rule for current state and current symbol.
			rule = findRule(this.state, this.tape[this.position]);
			
			// if the rule was not found, then we stop the machine.
			if(rule == null ) {
				end("Not rule was found. turing machine aborted.");
				times = step; // break; (but we can not use break;)
				
			}else
			{
				rule.explain();

				//printMachine();

				// update symbol
				if(rule.newSymbol != valueANY)
					this.tape[this.position] = rule.newSymbol;
				
				
				// update machine's position
				if (rule.direction == -1) 
					--this.position;
				else if(rule.direction == 1)
					++this.position;
				// else: don't move
				
				
				
				// check if the machine is out of memory
				if(this.position < 0 || this.position >= this.size) {
					
					this.position = 0;
					end("Tape overflow, turing machine aborted.");
					times = step; // break; (but we can not use break;)
					
				// check if new status is END value.
				}else if(rule.newState == valueEND) {
					
					end("End symbol found. Stopped succesfully. "+step+" steps.");
					times = step; // break; (but we can not use break;)
					
				}else if(rule.newState != valueANY)
						this.state = rule.newState;
			}
			++step;
		}
		return step;
	}
	
	
	protected void end(String message)
	{
		inHaltState = true;
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
		case valueD:
			System.out.print(Parser.charD); break;
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
