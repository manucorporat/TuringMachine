/*
 * TuringMachine
 *
 * Copyright (c) 2012 Manuel Mart’nez-Almeida
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

import java.util.Scanner;

public class Turing
{
	public static Parser getProgramFrom(Scanner in)
	{
		Parser parse;
        boolean isOk;
        do {
            System.out.print("Introduce el programa: ");
            String filename = in.nextLine();
            parse = new Parser();
            isOk = parse.readFile(filename);
        } while( !isOk );
        
        return parse;
	}
	
	
	public static int[] getTapeFrom(Scanner in)
	{
        int [] tape;
        boolean isOk;

        do {
            System.out.print("Introduce la cinta: ");
            String cinta = in.nextLine();
    		String[] numbers = cinta.split("\\s+");
    		tape = new int[numbers.length];
        	isOk = true;

    		try {
        		for(int i = 0; i < numbers.length; ++i)
        			tape[i] = Parser.parseZone(numbers[i], 0, 1, false);
        		
    		} catch(Exception e) {
    			System.out.println("Error parsing tape: " + e.getMessage());
    			isOk = false;
    		}
        } while( !isOk );
        
        return tape;
	}
	
	
    public static void main (String[] args)
    {   
    	// CREATE SCANNER
        Scanner in = new Scanner (System.in);
        
        // GET AND PARSE PROGRAM
        Parser parse = getProgramFrom(in);
        
        // GET TAPE AND VALIDATE
        int [] tape = getTapeFrom(in);
        
        
        // CREATE MACHINE FROM PARSER
        TuringMachine machine = new TuringMachine(parse, 100);
        
        // LOAD TAPE
        machine.loadTape(tape);
        
        //machine.run();
        
        // CREATE GRIDWORLD INTERFACE
        // this connects the TuringMachine with Gridworld
        GridManager manager = new GridManager(machine, -1);
        
        // MAKE WORLD VISIBLE
    	manager.show();
    }
}
