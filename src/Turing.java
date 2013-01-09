/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2005-2006 Cay S. Horstmann (http://horstmann.com)
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * @author Cay Horstmann
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
        boolean isOk = true;

        do {
            System.out.print("Introduce la cinta: ");
            String cinta = in.nextLine();
    		String[] numbers = cinta.split("\\s+");
    		tape = new int[numbers.length];
    		
    		try {
            	isOk = true;
        		for(int i = 0; i < numbers.length; ++i)
        			tape[i] = Parser.parseZone(numbers[i], 0, 9, false);
        		
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
