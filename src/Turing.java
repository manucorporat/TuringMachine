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
    public static void main (String[] args)
    {   
        Scanner in = new Scanner (System.in);
        Parser parse;
        int [] tape;
        boolean b;

        
        do {
            // GET AND PARSE PROGRAM
            System.out.print("Introduce el programa: ");
            String filename = in.nextLine();
            parse = new Parser();
            b = parse.readFile(filename);
        } while( !b );
        
        
        do {
            // GET TAPE AND VALIDATE
        	b = true;
            System.out.print("Introduce la cinta: ");
            String cinta = in.nextLine();
    		String[] numbers = cinta.split("\\s+");
    		tape = new int[numbers.length];
    		
    		try {
        		for(int i = 0; i < numbers.length; ++i)
        			tape[i] = Integer.parseInt(numbers[i]);
        		
    		} catch(Exception e) {
    			System.out.println("Tape bad format.");
    			b = false;
    		}
        } while( !b );
        
        
        // CREATE MACHINE FROM PARSER
        TuringMachine machine = new TuringMachine(parse, 50);
        
        // LOAD TAPE
        machine.loadTape(tape);
        
        // CREATE GRIDWORLD INTERFACE
        GridManager manager = new GridManager(machine);
        
        // MAKE WORLD VISIBLE
    	manager.show();
    }
}
